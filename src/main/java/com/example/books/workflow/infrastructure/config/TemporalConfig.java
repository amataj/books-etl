package com.example.books.workflow.infrastructure.config;

import com.example.books.workflow.DocumentLifecycleWorkflowImpl;
import com.example.books.workflow.activities.DocumentActivitiesImpl;
import io.temporal.client.WorkflowClient;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.serviceclient.WorkflowServiceStubsOptions;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("workflow")
public class TemporalConfig {

    @Value("${TEMPORAL_SERVER_ADDRESS:temporal.temporal.svc.cluster.local:7233}")
    private String temporalAddress;

    @Bean
    public WorkflowServiceStubs workflowServiceStubs() {
        return WorkflowServiceStubs.newInstance(WorkflowServiceStubsOptions.newBuilder().setTarget(temporalAddress).build());
    }

    @Bean
    public WorkflowClient workflowClient(WorkflowServiceStubs workflowServiceStubs) {
        return WorkflowClient.newInstance(workflowServiceStubs);
    }

    @Bean(initMethod = "start", destroyMethod = "shutdown")
    public WorkerFactory workerFactory(WorkflowClient workflowClient) {
        WorkerFactory factory = WorkerFactory.newInstance(workflowClient);
        Worker worker = factory.newWorker("document-lifecycle-task-queue");

        worker.registerWorkflowImplementationTypes(DocumentLifecycleWorkflowImpl.class);
        worker.registerActivitiesImplementations(new DocumentActivitiesImpl());

        return factory;
    }
}
