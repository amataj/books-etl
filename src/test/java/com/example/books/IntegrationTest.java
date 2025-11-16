package com.example.books;

import com.example.books.etl.config.AsyncSyncConfiguration;
import com.example.books.etl.config.EmbeddedKafka;
import com.example.books.etl.config.EmbeddedSQL;
import com.example.books.etl.infrastructure.config.JacksonConfiguration;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(classes = { BooksEtlApp.class, JacksonConfiguration.class, AsyncSyncConfiguration.class })
@EmbeddedSQL
@EmbeddedKafka
public @interface IntegrationTest {
}
