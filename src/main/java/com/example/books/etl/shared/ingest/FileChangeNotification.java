package com.example.books.etl.shared.ingest;

import java.io.Serializable;
import java.time.Instant;

/**
 * Message describing a change detected on disk.
 *
 * @param path absolute path to the file on disk
 * @param changeType type of change that triggered the event
 * @param checksum sha256 checksum for created/updated files, {@code null} for deletions
 * @param sizeBytes file size in bytes when the event was observed
 * @param eventTime instant when the change was detected
 */
public record FileChangeNotification(String path, FileChangeType changeType, String checksum, long sizeBytes, Instant eventTime)
    implements Serializable {
    public boolean hasChecksum() {
        return checksum != null && !checksum.isBlank();
    }
}
