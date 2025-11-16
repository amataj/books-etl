package com.example.books.infrastructure.parser;

import com.example.books.shared.ingest.FileChangeNotification;
import com.example.books.shared.ingest.ParsedPdfDocument;
import java.io.IOException;

public interface PdfParser {
    ParsedPdfDocument parse(FileChangeNotification sourceEvent) throws IOException;
}
