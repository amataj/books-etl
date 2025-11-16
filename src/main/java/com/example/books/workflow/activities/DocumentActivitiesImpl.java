package com.example.books.workflow.activities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DocumentActivitiesImpl implements DocumentActivities {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentActivitiesImpl.class);

    @Override
    public void validateMetadata(String documentId) {
        LOG.info("Validating metadata for {}", documentId);
    }

    @Override
    public void publishValidatedEvent(String documentId) {
        LOG.info("Publishing validated event for {}", documentId);
        // TODO: send Kafka \"document.validated\" event
    }
}
