package com.example.books.shared.ingest;

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
