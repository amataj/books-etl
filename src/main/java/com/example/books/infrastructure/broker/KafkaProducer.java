package com.example.books.infrastructure.broker;

import com.example.books.config.KafkaTopicProperties;
import com.example.books.shared.ingest.DlqMessage;
import com.example.books.shared.ingest.FileChangeNotification;
import com.example.books.shared.ingest.ParsedPdfDocument;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.concurrent.CompletableFuture;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
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

    public void send(String topic, Object payload) {
        try {
            LOG.info("Sending kafka message on topic: '{}' - message: {}", topic, payload.toString());
            String serializedPayload = objectMapper.writeValueAsString(payload);

            ProducerRecord<String, String> producerRecord = new ProducerRecord<>(topic, serializedPayload);
            CompletableFuture<SendResult<String, String>> completableFuture = kafkaTemplate.send(producerRecord);

            completableFuture.whenComplete((result, ex) -> {
                if (ex == null) {
                    LOG.info("Kafka message successfully sent on topic {} and value {}", topic, result.getProducerRecord().value());
                } else {
                    LOG.error("An error occurred while sending kafka message for event with value {}", producerRecord);
                }
            });
        } catch (Exception e) {
            LOG.error("Failed to serialize payload for topic {}", topic, e);
            throw new IllegalStateException("Unable to serialize Kafka payload", e);
        }
    }
}
