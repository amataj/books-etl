package com.example.books.infrastructure.broker;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfiguration {

    public static final String PDF_INGEST_RAW = "pdf.ingest.raw";
    public static final String PDF_INGEST_PARSED = "pdf.ingest.parsed";
    public static final String PDF_INGEST_INDEXED = "pdf.ingest.indexed";
    public static final String PDF_INGEST_DLQ = "pdf.ingest.dlq";

    @Bean
    public NewTopic pdfIngestRawTopic() {
        return buildSinglePartitionTopic(PDF_INGEST_RAW);
    }

    @Bean
    public NewTopic pdfIngestParsedTopic() {
        return buildSinglePartitionTopic(PDF_INGEST_PARSED);
    }

    @Bean
    public NewTopic pdfIngestIndexedTopic() {
        return buildSinglePartitionTopic(PDF_INGEST_INDEXED);
    }

    @Bean
    public NewTopic pdfIngestDlqTopic() {
        return buildSinglePartitionTopic(PDF_INGEST_DLQ);
    }

    private NewTopic buildSinglePartitionTopic(String topicName) {
        return TopicBuilder.name(topicName).partitions(1).replicas(1).build();
    }
}
