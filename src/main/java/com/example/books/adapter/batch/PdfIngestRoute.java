package com.example.books.adapter.batch;

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
import org.springframework.stereotype.Component;

@Component
public class PdfIngestRoute extends RouteBuilder {

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
                DlqMessage dlqMessage = new DlqMessage(original, error, Instant.now());
                try {
                    exchange.getMessage().setBody(objectMapper.writeValueAsString(dlqMessage));
                } catch (JsonProcessingException jsonProcessingException) {
                    exchange.getMessage().setBody(dlqMessage.toString());
                }
            })
            .to("kafka:" + dlqTopic);

        from("kafka:" + rawTopic + "?groupId=books-etl-camel&autoOffsetReset=latest")
            .routeId("pdf-ingest-batch-route")
            .process(exchange -> {
                String payload = exchange.getIn().getBody(String.class);
                FileChangeNotification event = objectMapper.readValue(payload, FileChangeNotification.class);
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
                    throw new IllegalStateException("File no longer exists: " + file);
                }
                ParsedPdfDocument parsedPdfDocument = pdfParser.parse(file, change);
                exchange.getMessage().setBody(objectMapper.writeValueAsString(parsedPdfDocument));
            })
            .to("kafka:" + parsedTopic)
            .end();
    }
}
