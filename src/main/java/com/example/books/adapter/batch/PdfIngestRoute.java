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
    public void configure() {}
}
