package com.example.books.adapter.web.rest;

import com.example.books.infrastructure.broker.KafkaConsumer;
import com.example.books.infrastructure.broker.KafkaProducer;
import java.security.Principal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

@RestController
@RequestMapping("/api/books-etl-kafka")
public class BooksEtlKafkaResource {

    private static final Logger LOG = LoggerFactory.getLogger(BooksEtlKafkaResource.class);
    private final KafkaConsumer kafkaConsumer;
    private final KafkaProducer kafkaProducer;

    public BooksEtlKafkaResource(KafkaConsumer kafkaConsumer, KafkaProducer kafkaProducer) {
        this.kafkaConsumer = kafkaConsumer;
        this.kafkaProducer = kafkaProducer;
    }

    @PostMapping("/publish")
    public void publish(@RequestParam("message") String message) {
        LOG.debug("REST request the message : {} to send to Kafka topic ", message);
        kafkaProducer.publishRawMessage(message);
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
