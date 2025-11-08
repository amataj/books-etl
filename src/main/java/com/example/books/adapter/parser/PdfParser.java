package com.example.books.adapter.parser;

import com.example.books.shared.ingest.FileChangeNotification;
import com.example.books.shared.ingest.ParsedPdfDocument;
import java.io.IOException;
import java.nio.file.Path;

public interface PdfParser {
    ParsedPdfDocument parse(Path pdfPath, FileChangeNotification sourceEvent) throws IOException;
}
