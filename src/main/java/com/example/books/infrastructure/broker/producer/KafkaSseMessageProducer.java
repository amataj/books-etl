package com.example.books.infrastructure.broker.producer;

import com.example.books.config.KafkaTopicProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

@Component
public class KafkaSseMessageProducer implements Supplier<String> {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaSseMessageProducer.class);

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final String topic;

    public KafkaSseMessageProducer(
        KafkaTemplate<String, String> kafkaTemplate,
        ObjectMapper objectMapper,
        KafkaTopicProperties kafkaTopicProperties
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
        this.topic = kafkaTopicProperties.getSse();
    }

    @Override
    public String get() {
        return "kafka_producer";
    }

    public void publishMessage(String message) {
        send(topic, message);
    }

    private void send(String topic, Object payload) {
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
