package com.example.books.etl.infrastructure.broker;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfiguration {

    private final KafkaTopicProperties topics;

    public KafkaTopicConfiguration(KafkaTopicProperties topics) {
        this.topics = topics;
    }

    @Bean
    public NewTopic pdfIngestRawTopic() {
        return buildSinglePartitionTopic(topics.getRaw());
    }

    @Bean
    public NewTopic pdfIngestParsedTopic() {
        return buildSinglePartitionTopic(topics.getParsed());
    }

    @Bean
    public NewTopic pdfIngestIndexedTopic() {
        return buildSinglePartitionTopic(topics.getIndexed());
    }

    @Bean
    public NewTopic pdfIngestDlqTopic() {
        return buildSinglePartitionTopic(topics.getDlq());
    }

    private NewTopic buildSinglePartitionTopic(String topicName) {
        return TopicBuilder.name(topicName).partitions(1).replicas(1).build();
    }
}
