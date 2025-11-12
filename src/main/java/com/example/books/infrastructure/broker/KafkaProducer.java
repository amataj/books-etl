package com.example.books.infrastructure.broker;

import com.example.books.config.KafkaTopicProperties;
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
    private final String rawTopic;
    private final String parsedTopic;
    private final String dlqTopic;

    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper, KafkaTopicProperties topics) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
        this.rawTopic = topics.getRaw();
        this.parsedTopic = topics.getParsed();
        this.dlqTopic = topics.getDlq();
    }

    public void publishFileChange(FileChangeNotification notification) {
        send(rawTopic, notification);
    }

    public void publishParsedDocument(ParsedPdfDocument document) {
        send(parsedTopic, document);
    }

    public void publishDeadLetter(DlqMessage message) {
        send(dlqTopic, message);
    }

    public void publishRawMessage(String message) {
        kafkaTemplate.send(rawTopic, message);
    }

    private void send(String topic, Object payload) {
        try {
            LOG.info("Sending to topic: '{}' - message: {}", topic, payload.toString());
            String serialized = objectMapper.writeValueAsString(payload);
            kafkaTemplate.send(topic, serialized);
        } catch (JsonProcessingException e) {
            LOG.error("Failed to serialize payload for topic {}", topic, e);
            throw new IllegalStateException("Unable to serialize Kafka payload", e);
        }
    }
}
