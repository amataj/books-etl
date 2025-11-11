package com.example.books.infrastructure.broker;

import static org.springframework.web.servlet.mvc.method.annotation.SseEmitter.event;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Component
public class KafkaConsumer {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaConsumer.class);

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter register(String key) {
        LOG.debug("Registering sse client for {}", key);
        SseEmitter emitter = new SseEmitter();
        emitter.onCompletion(() -> emitters.remove(key));
        emitters.put(key, emitter);
        return emitter;
    }

    public void unregister(String key) {
        LOG.debug("Unregistering sse emitter for: {}", key);
        Optional.ofNullable(emitters.get(key)).ifPresent(SseEmitter::complete);
    }

    @KafkaListener(topics = "${application.kafka.topics.parsed}", groupId = "books-etl-sse")
    public void consumeParsedDocument(String payload) {
        LOG.info("Received parsed PDF event from Kafka");
        broadcast(payload, MediaType.APPLICATION_JSON);
    }

    @KafkaListener(topics = "${application.kafka.topics.dlq}", groupId = "books-etl-sse-dlq")
    public void consumeDeadLetter(String payload) {
        LOG.error("Received message in dead letter queue: {}", payload);
    }

    private void broadcast(String payload, MediaType mediaType) {
        emitters
            .values()
            .forEach(emitter -> {
                try {
                    emitter.send(event().data(payload, mediaType));
                } catch (IOException e) {
                    LOG.debug("Error sending SSE payload", e);
                }
            });
    }
}
