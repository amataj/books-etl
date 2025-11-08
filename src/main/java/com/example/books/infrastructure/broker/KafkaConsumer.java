package com.example.books.infrastructure.broker;

import static org.springframework.web.servlet.mvc.method.annotation.SseEmitter.event;

import jakarta.annotation.PreDestroy;
import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
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
    private static final Duration HEARTBEAT_INTERVAL = Duration.ofSeconds(15);

    private final Map<String, EmitterContext> emitters = new ConcurrentHashMap<>();
    private final ScheduledExecutorService heartbeatExecutor = Executors.newSingleThreadScheduledExecutor(r -> {
        Thread thread = new Thread(r);
        thread.setName("kafka-sse-heartbeat");
        thread.setDaemon(true);
        return thread;
    });

    public SseEmitter register(String key) {
        LOG.debug("Registering sse client for {}", key);
        Optional.ofNullable(removeEmitterContext(key)).ifPresent(context -> context.emitter.complete());

        SseEmitter emitter = new SseEmitter(SSE_TIMEOUT);
        emitter.onCompletion(() -> removeEmitterContext(key, emitter));
        emitter.onTimeout(() -> {
            LOG.debug("sse emitter for {} timed out", key);
            removeEmitterContext(key, emitter);
        });
        emitter.onError(error -> {
            LOG.debug("sse emitter for {} failed", key, error);
            removeEmitterContext(key, emitter);
        });

        ScheduledFuture<?> heartbeatTask = heartbeatExecutor.scheduleAtFixedRate(
            () -> sendHeartbeat(key, emitter),
            HEARTBEAT_INTERVAL.toMillis(),
            HEARTBEAT_INTERVAL.toMillis(),
            TimeUnit.MILLISECONDS
        );

        emitters.put(key, new EmitterContext(emitter, heartbeatTask));
        sendHeartbeat(key, emitter);
        return emitter;
    }

    public void unregister(String key) {
        LOG.debug("Unregistering sse emitter for: {}", key);
        Optional.ofNullable(removeEmitterContext(key)).ifPresent(context -> context.emitter.complete());
    }

    @Override
    public void accept(String input) {
        LOG.info("Received message from kafka stream: {}", input);
        emitters
            .entrySet()
            .forEach(entry -> {
                String key = entry.getKey();
                SseEmitter emitter = entry.getValue().emitter;
                try {
                    emitter.send(event().data(input, MediaType.TEXT_PLAIN));
                } catch (Exception e) {
                    LOG.debug("error sending sse message, {}", input, e);
                    Optional.ofNullable(removeEmitterContext(key, emitter)).ifPresent(context -> context.emitter.completeWithError(e));
                }
            });
    }

    @PreDestroy
    public void shutdown() {
        emitters.values().forEach(EmitterContext::cancel);
        emitters.clear();
        heartbeatExecutor.shutdownNow();
    }

    private void sendHeartbeat(String key, SseEmitter emitter) {
        if (!isCurrentEmitter(key, emitter)) {
            return;
        }
        try {
            emitter.send(event().comment("heartbeat").reconnectTime(HEARTBEAT_INTERVAL.toMillis()));
        } catch (Exception e) {
            LOG.debug("error sending heartbeat to {}", key, e);
            Optional.ofNullable(removeEmitterContext(key, emitter)).ifPresent(context -> context.emitter.completeWithError(e));
        }
    }

    private boolean isCurrentEmitter(String key, SseEmitter emitter) {
        EmitterContext context = emitters.get(key);
        return context != null && context.emitter == emitter;
    }

    private EmitterContext removeEmitterContext(String key) {
        return removeEmitterContext(key, null);
    }

    private EmitterContext removeEmitterContext(String key, SseEmitter expectedEmitter) {
        EmitterContext context = emitters.get(key);
        if (context == null) {
            return null;
        }
        if (expectedEmitter != null && context.emitter != expectedEmitter) {
            return null;
        }
        boolean removed = emitters.remove(key, context);
        if (removed) {
            context.cancel();
            return context;
        }
        return null;
    }

    private static final class EmitterContext {

        private final SseEmitter emitter;
        private final ScheduledFuture<?> heartbeatTask;

        private EmitterContext(SseEmitter emitter, ScheduledFuture<?> heartbeatTask) {
            this.emitter = emitter;
            this.heartbeatTask = heartbeatTask;
        }

        private void cancel() {
            if (heartbeatTask != null) {
                heartbeatTask.cancel(true);
            }
        }
    }
}
