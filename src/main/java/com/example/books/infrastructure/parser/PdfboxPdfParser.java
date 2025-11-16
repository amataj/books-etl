package com.example.books.infrastructure.parser;

import com.example.books.domain.book.PdfParserPort;
import com.example.books.shared.ingest.FileChangeNotification;
import com.example.books.shared.ingest.ParsedPdfDocument;
import com.example.books.shared.ingest.ParsedPdfPage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;

@Component
public class PdfboxPdfParser implements PdfParserPort {

    private static final Logger LOG = LoggerFactory.getLogger(PdfboxPdfParser.class);

    private final Tesseract tesseract;
    private final boolean ocrEnabled;

    public PdfboxPdfParser(ObjectProvider<Tesseract> tesseractProvider) {
        this.tesseract = tesseractProvider.getIfAvailable();
        this.ocrEnabled = this.tesseract != null;
        if (!ocrEnabled) {
            LOG.info("Tesseract not available on classpath - OCR fallback disabled");
        }
    }

    @Override
    public ParsedPdfDocument parse(FileChangeNotification sourceEvent) throws IOException {
        Path pdfPath = Path.of(sourceEvent.path());
        LOG.info("Parsing PDF file: '{}' - sourceEvent: {}", pdfPath, sourceEvent);

        try (PDDocument document = Loader.loadPDF(pdfPath.toFile())) {
            int numberOfPages = document.getNumberOfPages();
            PDFTextStripper textStripper = new PDFTextStripper();
            PDFRenderer renderer = new PDFRenderer(document);
            List<ParsedPdfPage> pages = new ArrayList<>(numberOfPages);

            for (int page = 1; page <= numberOfPages; page++) {
                textStripper.setStartPage(page);
                textStripper.setEndPage(page);
                String text = textStripper.getText(document).trim();
                if (text.isBlank() && ocrEnabled) {
                    text = runOcr(renderer, page);
                }
                pages.add(new ParsedPdfPage(page, text));
            }

            Map<String, String> metadata = extractMetadata(document.getDocumentInformation());

            return new ParsedPdfDocument(
                pdfPath.toAbsolutePath().toString(),
                sourceEvent != null ? sourceEvent.checksum() : null,
                sourceEvent != null ? sourceEvent.sizeBytes() : Files.size(pdfPath),
                numberOfPages,
                sourceEvent != null ? sourceEvent.eventTime() : Instant.now(),
                metadata,
                List.copyOf(pages)
            );
        }
    }

    private String runOcr(PDFRenderer renderer, int page) throws IOException {
        if (!ocrEnabled) {
            return "";
        }
        try {
            return tesseract.doOCR(renderer.renderImageWithDPI(page - 1, 300, ImageType.GRAY)).trim();
        } catch (TesseractException e) {
            LOG.warn("OCR failed for page {}", page, e);
            return "";
        }
    }

    private Map<String, String> extractMetadata(PDDocumentInformation info) {
        Map<String, String> metadata = new LinkedHashMap<>();
        if (info == null) {
            return metadata;
        }
        putIfPresent(metadata, "title", info.getTitle());
        putIfPresent(metadata, "author", info.getAuthor());
        putIfPresent(metadata, "subject", info.getSubject());
        putIfPresent(metadata, "keywords", info.getKeywords());
        putIfPresent(metadata, "creator", info.getCreator());
        putIfPresent(metadata, "producer", info.getProducer());
        if (info.getCreationDate() != null) {
            metadata.put("creationDate", info.getCreationDate().getTime().toString());
        }
        if (info.getModificationDate() != null) {
            metadata.put("modificationDate", info.getModificationDate().getTime().toString());
        }
        return metadata;
    }

    private void putIfPresent(Map<String, String> metadata, String key, String value) {
        if (value != null && !value.isBlank()) {
            metadata.put(key, value);
        }
    }
}
