package com.example.books.infrastructure.broker;

import static org.springframework.web.servlet.mvc.method.annotation.SseEmitter.event;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Component
public class KafkaConsumer implements Consumer<String> {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaConsumer.class);

    private static final long SSE_TIMEOUT = Long.MAX_VALUE;

    private final Map<String, SseEmitter> emitters = new java.util.concurrent.ConcurrentHashMap<>();

    public SseEmitter register(String key) {
        LOG.debug("Registering sse client for {}", key);
        SseEmitter emitter = new SseEmitter(SSE_TIMEOUT);
        emitter.onCompletion(() -> emitters.remove(key));
        emitter.onTimeout(() -> {
            LOG.debug("sse emitter for {} timed out", key);
            emitters.remove(key);
        });
        emitter.onError(error -> {
            LOG.debug("sse emitter for {} failed", key, error);
            emitters.remove(key);
        });
        emitters.put(key, emitter);
        return emitter;
    }

    public void unregister(String key) {
        LOG.debug("Unregistering sse emitter for: {}", key);
        Optional.ofNullable(emitters.get(key)).ifPresent(SseEmitter::complete);
    }

    @Override
    public void accept(String input) {
        LOG.debug("Got message from kafka stream: {}", input);
        emitters
            .entrySet()
            .stream()
            .map(Map.Entry::getValue)
            .forEach((SseEmitter emitter) -> {
                try {
                    emitter.send(event().data(input, MediaType.TEXT_PLAIN));
                } catch (IOException e) {
                    LOG.debug("error sending sse message, {}", input);
                    emitters.values().remove(emitter);
                    emitter.completeWithError(e);
                }
            });
    }
}
