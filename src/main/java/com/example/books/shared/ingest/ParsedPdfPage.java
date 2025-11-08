package com.example.books.shared.ingest;

import java.io.Serializable;

/**
 * Parsed representation of a single PDF page.
 */
public record ParsedPdfPage(int pageNumber, String text) implements Serializable {}
