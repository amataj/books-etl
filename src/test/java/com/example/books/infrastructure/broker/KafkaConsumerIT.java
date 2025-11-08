package com.example.books.infrastructure.broker;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@ImportAutoConfiguration(TestChannelBinderConfiguration.class)
class KafkaConsumerIT {

    private static final String BINDING = "binding-out-0";

    @Autowired
    private StreamBridge streamBridge;

    @SpyBean
    private KafkaConsumer kafkaConsumer;

    @Test
    void shouldConsumeMessagesSentViaStreamBridge() {
        boolean sent = streamBridge.send(BINDING, "test-message");

        assertThat(sent).isTrue();
        verify(kafkaConsumer, timeout(1000)).accept("test-message");
    }
}
