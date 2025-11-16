package com.example.books.domain.book;

import com.example.books.shared.ingest.FileChangeNotification;
import com.example.books.shared.ingest.ParsedPdfDocument;
import java.io.IOException;

public interface PdfParserPort {
    ParsedPdfDocument parse(FileChangeNotification sourceEvent) throws IOException;
}
