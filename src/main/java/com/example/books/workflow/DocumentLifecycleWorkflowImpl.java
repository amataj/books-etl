package com.example.books.workflow;

import com.example.books.workflow.activities.DocumentActivities;
import io.temporal.workflow.Workflow;

public class DocumentLifecycleWorkflowImpl implements DocumentLifecycleWorkflow {

    private final DocumentActivities activities = Workflow.newActivityStub(DocumentActivities.class);

    @Override
    public void process(String documentId) {
        activities.validateMetadata(documentId);
        activities.publishValidatedEvent(documentId);
    }
}
