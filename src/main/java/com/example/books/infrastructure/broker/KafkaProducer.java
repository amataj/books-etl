package com.example.books.infrastructure.broker;

import com.example.books.shared.ingest.DlqMessage;
import com.example.books.shared.ingest.FileChangeNotification;
import com.example.books.shared.ingest.ParsedPdfDocument;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducer {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaProducer.class);

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void publishFileChange(FileChangeNotification notification) {
        send(KafkaTopicConfiguration.PDF_INGEST_RAW, notification);
    }

    public void publishParsedDocument(ParsedPdfDocument document) {
        send(KafkaTopicConfiguration.PDF_INGEST_PARSED, document);
    }

    public void publishDeadLetter(DlqMessage message) {
        send(KafkaTopicConfiguration.PDF_INGEST_DLQ, message);
    }

    public void publishRawMessage(String message) {
        kafkaTemplate.send(KafkaTopicConfiguration.PDF_INGEST_RAW, message);
    }

    private void send(String topic, Object payload) {
        try {
            String serialized = objectMapper.writeValueAsString(payload);
            kafkaTemplate.send(topic, serialized);
        } catch (JsonProcessingException e) {
            LOG.error("Failed to serialize payload for topic {}", topic, e);
            throw new IllegalStateException("Unable to serialize Kafka payload", e);
        }
    }
}
