package com.example.books.etl.shared.ingest;

/**
 * Type of change observed on the filesystem.
 */
public enum FileChangeType {
    CREATED,
    MODIFIED,
    DELETED;

    public boolean requiresContent() {
        return this != DELETED;
    }
}
