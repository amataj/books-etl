package com.example.books.etl.infrastructure.broker;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.listener.AbstractMessageListenerContainer;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.stereotype.Component;

@Component
public class KafkaListenersHealthIndicator implements HealthIndicator {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaListenersHealthIndicator.class);

    private final KafkaListenerEndpointRegistry registry;

    public KafkaListenersHealthIndicator(KafkaListenerEndpointRegistry registry) {
        this.registry = registry;
    }

    @Override
    public Health health() {
        Collection<MessageListenerContainer> containers = registry.getListenerContainers();
        int total = containers.size();
        int running = 0;
        int assignedPartitions = 0;
        Map<String, Object> details = new HashMap<>();

        for (MessageListenerContainer container : containers) {
            boolean isRunning = container.isRunning();
            if (isRunning) {
                running++;
            }
            int assigned = 0;
            if (container instanceof AbstractMessageListenerContainer<?, ?> aml) {
                Collection<TopicPartition> parts = aml.getAssignedPartitions();
                assigned = parts != null ? parts.size() : 0;
                assignedPartitions += assigned;
            }
            Map<String, Object> c = new HashMap<>();
            c.put("running", isRunning);
            c.put("assignedPartitions", assigned);
            details.put(container.getListenerId(), c);
        }

        Health.Builder builder = (running > 0) ? Health.up() : Health.down();
        Health health = builder
            .withDetail("totalContainers", total)
            .withDetail("runningContainers", running)
            .withDetail("assignedPartitions", assignedPartitions)
            .withDetail("containers", details)
            .build();

        if (running > 0 && assignedPartitions > 0) {
            LOG.info("Kafka listeners UP - running: {}, assigned partitions: {}", running, assignedPartitions);
        }

        return health;
    }
}
