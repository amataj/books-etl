package com.example.books.etl.domain.book;

import com.example.books.etl.shared.ingest.FileChangeNotification;
import com.example.books.etl.shared.ingest.ParsedPdfDocument;
import java.io.IOException;

public interface PdfParserPort {
    ParsedPdfDocument parse(FileChangeNotification sourceEvent) throws IOException;
}
