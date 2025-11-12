package com.example.books.adapter.web.rest;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.common.TopicPartition;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.listener.AbstractMessageListenerContainer;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/kafka")
public class KafkaListenerStatusResource {

    private final KafkaListenerEndpointRegistry registry;

    public KafkaListenerStatusResource(KafkaListenerEndpointRegistry registry) {
        this.registry = registry;
    }

    @GetMapping("/listeners")
    public Map<String, Object> listeners() {
        Collection<MessageListenerContainer> containers = registry.getListenerContainers();
        int total = containers.size();
        int running = 0;
        Map<String, Object> detail = new HashMap<>();

        for (MessageListenerContainer c : containers) {
            boolean isRunning = c.isRunning();
            if (isRunning) running++;
            int assigned = 0;
            if (c instanceof AbstractMessageListenerContainer<?, ?> aml) {
                Collection<TopicPartition> parts = aml.getAssignedPartitions();
                assigned = parts != null ? parts.size() : 0;
            }
            Map<String, Object> info = new HashMap<>();
            info.put("running", isRunning);
            info.put("assignedPartitions", assigned);
            detail.put(c.getListenerId(), info);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("totalContainers", total);
        result.put("runningContainers", running);
        result.put("containers", detail);
        return result;
    }
}
