package com.example.books.workflow.activities;

import io.temporal.activity.ActivityInterface;

@ActivityInterface
public interface DocumentActivities {
    void validateMetadata(String documentId);

    void publishValidatedEvent(String documentId);
}
