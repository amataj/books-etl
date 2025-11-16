package com.example.books.adapter.web.rest;

import com.example.books.infrastructure.broker.consumer.KafkaSseMessageConsumer;
import java.security.Principal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

@RestController
@RequestMapping("/api/books-etl-kafka-sse")
public class BooksEtlKafkaSSEResource {

    private static final Logger LOG = LoggerFactory.getLogger(BooksEtlKafkaSSEResource.class);
    private static final String PRODUCER_BINDING_NAME = "binding-out-0";

    private final KafkaSseMessageConsumer kafkaConsumer;
    private final StreamBridge streamBridge;

    public BooksEtlKafkaSSEResource(KafkaSseMessageConsumer kafkaConsumer, StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
        this.kafkaConsumer = kafkaConsumer;
    }

    @PostMapping("/publish")
    public void publish(@RequestParam("message") String message) {
        LOG.debug("REST request the message : {} to send to Kafka topic ", message);
        streamBridge.send(PRODUCER_BINDING_NAME, message);
    }

    @GetMapping("/register")
    public ResponseBodyEmitter register(Principal principal) {
        return kafkaConsumer.register(principal.getName());
    }

    @GetMapping("/unregister")
    public void unregister(Principal principal) {
        kafkaConsumer.unregister(principal.getName());
    }
}
