package com.example.books.etl.shared.ingest;

import java.io.Serializable;
import java.time.Instant;

/**
 * Message emitted to the dead letter queue when ingestion fails.
 */
public record DlqMessage(String originalPayload, String error, Instant timestamp) implements Serializable {}
