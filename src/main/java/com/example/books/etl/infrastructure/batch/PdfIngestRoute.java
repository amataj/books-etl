package com.example.books.etl.infrastructure.batch;

import com.example.books.etl.domain.book.PdfParserPort;
import com.example.books.etl.infrastructure.broker.KafkaTopicProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private final PdfParserPort pdfParserPort;
    private final String rawTopic;
    private final String parsedTopic;
    private final String dlqTopic;

    public PdfIngestRoute(ObjectMapper objectMapper, PdfParserPort pdfParserPort, KafkaTopicProperties topics) {
        this.objectMapper = objectMapper;
        this.pdfParserPort = pdfParserPort;
        this.rawTopic = topics.getRaw();
        this.parsedTopic = topics.getParsed();
        this.dlqTopic = topics.getDlq();
    }

    @Override
    public void configure() {}
}
