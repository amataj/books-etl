package com.example.books.infrastructure.broker;

import java.util.Collection;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.event.ConsumerStartedEvent;
import org.springframework.kafka.event.ConsumerStoppedEvent;
import org.springframework.kafka.listener.AbstractMessageListenerContainer;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.stereotype.Component;

@Component
public class KafkaListenerMonitor {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaListenerMonitor.class);
    private final KafkaListenerEndpointRegistry registry;

    public KafkaListenerMonitor(KafkaListenerEndpointRegistry registry) {
        this.registry = registry;
    }

    @EventListener
    public void onConsumerStarted(ConsumerStartedEvent event) {
        logAggregate("started");
    }

    @EventListener
    public void onConsumerStopped(ConsumerStoppedEvent event) {
        logAggregate("stopped");
    }

    private void logAggregate(String action) {
        int running = 0;
        int assigned = 0;
        for (MessageListenerContainer c : registry.getListenerContainers()) {
            if (c.isRunning()) running++;
            if (c instanceof AbstractMessageListenerContainer<?, ?> aml) {
                Collection<TopicPartition> parts = aml.getAssignedPartitions();
                assigned += parts != null ? parts.size() : 0;
            }
        }
        LOG.info("Kafka consumers {} - runningContainers={}, assignedPartitions={}", action, running, assigned);
    }
}
