package com.example.books.infrastructure.broker;

import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

import com.example.books.IntegrationTest;
import com.example.books.config.EmbeddedKafka;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.util.MimeTypeUtils;

@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
@EmbeddedKafka
@ImportAutoConfiguration(TestChannelBinderConfiguration.class)
class KafkaConsumerIT {

    private static final String BINDING = "kafkaConsumer-in-0";

    @Autowired
    private InputDestination inputDestination;

    @SpyBean
    private KafkaConsumer kafkaConsumer;

    @Test
    void shouldConsumeMessagesSentViaInputDestination() {
        MessageHeaders headers = new MessageHeaders(Map.of(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.TEXT_PLAIN_VALUE));
        Message<String> message = new GenericMessage<>("test-message", headers);

        inputDestination.send(message, BINDING);

        verify(kafkaConsumer, timeout(1000)).accept("test-message");
    }
}
