package com.example.books;

import com.example.books.config.AsyncSyncConfiguration;
import com.example.books.config.JacksonConfiguration;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Variant of {@link IntegrationTest} that avoids spinning up the Embedded Kafka
 * container so tests can rely on the Spring Cloud Stream test binder.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(classes = { BooksEtlApp.class, JacksonConfiguration.class, AsyncSyncConfiguration.class })
public @interface MockKafkaIntegrationTest {
}
