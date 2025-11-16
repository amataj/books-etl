package com.example.books.workflow;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface DocumentLifecycleWorkflow {
    @WorkflowMethod
    void process(String documentId);
}
