package com.example.books.adapter.batch;

import com.example.books.adapter.fs.FileChecksumCalculator;
import com.example.books.adapter.parser.PdfParser;
import com.example.books.config.KafkaTopicProperties;
import com.example.books.shared.ingest.DlqMessage;
import com.example.books.shared.ingest.FileChangeNotification;
import com.example.books.shared.ingest.ParsedPdfDocument;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(prefix = "application.camel.routes.pdf-ingest", name = "enabled", havingValue = "true")
public class PdfIngestRoute extends RouteBuilder {

    private static final Logger LOG = LoggerFactory.getLogger(PdfIngestRoute.class);

    private final ObjectMapper objectMapper;
    private final PdfParser pdfParser;
    private final String rawTopic;
    private final String parsedTopic;
    private final String dlqTopic;

    public PdfIngestRoute(ObjectMapper objectMapper, PdfParser pdfParser, KafkaTopicProperties topics) {
        this.objectMapper = objectMapper;
        this.pdfParser = pdfParser;
        this.rawTopic = topics.getRaw();
        this.parsedTopic = topics.getParsed();
        this.dlqTopic = topics.getDlq();
    }

    @Override
    public void configure() {
        onException(Exception.class)
            .handled(true)
            .process(exchange -> {
                String original = exchange.getIn().getBody(String.class);
                Throwable cause = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Throwable.class);
                String error = cause != null ? cause.getMessage() : "Unknown error";
                LOG.error("Error processing message: '{}'", original, cause);
                DlqMessage dlqMessage = new DlqMessage(original, error, Instant.now());
                try {
                    exchange.getMessage().setBody(objectMapper.writeValueAsString(dlqMessage));
                } catch (JsonProcessingException jsonProcessingException) {
                    LOG.error("Failed to serialize DLQ message", jsonProcessingException);
                    exchange.getMessage().setBody(dlqMessage.toString());
                }
            })
            .to("kafka:" + dlqTopic);

        from("kafka:" + rawTopic + "?groupId=books-etl-camel&autoOffsetReset=latest")
            .routeId("pdf-ingest-batch-route")
            .process(exchange -> {
                String payload = exchange.getIn().getBody(String.class);
                FileChangeNotification event = objectMapper.readValue(payload, FileChangeNotification.class);
                LOG.info("Received file change event: '{}' - payload: {}", event, payload);
                exchange.setProperty("fileChange", event);
            })
            .choice()
            .when(exchange -> {
                FileChangeNotification change = exchange.getProperty("fileChange", FileChangeNotification.class);
                return change == null || !change.changeType().requiresContent();
            })
            .log("Skipping change event for ${exchangeProperty.fileChange.path}")
            .otherwise()
            .process(exchange -> {
                FileChangeNotification change = exchange.getProperty("fileChange", FileChangeNotification.class);
                Path file = Path.of(change.path());
                if (!Files.exists(file)) {
                    LOG.warn("File no longer exists: '{}'", file);
                    throw new IllegalStateException("File no longer exists: " + file);
                }
                ParsedPdfDocument parsedPdfDocument = pdfParser.parse(file, change);
                exchange.getMessage().setBody(objectMapper.writeValueAsString(parsedPdfDocument));
            })
            .to("kafka:" + parsedTopic)
            .end();
    }
}
