package com.example.books.etl.infrastructure.broker.consumer;

import static org.springframework.web.servlet.mvc.method.annotation.SseEmitter.event;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Component
public class KafkaBookPdfDocumentEventConsumer {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaBookPdfDocumentEventConsumer.class);

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    public KafkaBookPdfDocumentEventConsumer() {
        LOG.info("Created KafkaConsumer");
    }

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

    @KafkaListener(
        topics = "${application.kafka.topics.raw}",
        containerFactory = "kafkaListenerContainerFactory",
        groupId = "books-etl-app"
    )
    public void consumeRawDocument(Message<String> payload) {
        LOG.info("Kafka Consumer received raw PDF event from Kafka");
        broadcastToSSEEmitters(payload.getPayload(), MediaType.APPLICATION_JSON);
    }

    @KafkaListener(
        topics = "${application.kafka.topics.parsed}",
        containerFactory = "kafkaListenerContainerFactory",
        groupId = "books-etl-app"
    )
    public void consumeParsedDocument(Message<String> payload) {
        LOG.info("Kafka Consumer received parsed PDF event from Kafka");
        broadcastToSSEEmitters(payload.getPayload(), MediaType.APPLICATION_JSON);
    }

    @KafkaListener(
        topics = "${application.kafka.topics.dlq}",
        containerFactory = "kafkaListenerContainerFactory",
        groupId = "books-etl-app-dlq"
    )
    public void consumeDeadLetter(Message<String> payload) {
        LOG.error("Kafka Consumer received message in dead letter queue: {}", payload);
    }

    private void broadcastToSSEEmitters(String payload, MediaType mediaType) {
        LOG.debug("Broadcasting to SSE -payload: {}", payload);
        emitters
            .values()
            .forEach(emitter -> {
                try {
                    emitter.send(event().data(payload, mediaType));
                } catch (Exception e) {
                    LOG.warn("Error sending SSE payload", e);
                }
            });
    }
}
