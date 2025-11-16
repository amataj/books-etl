package com.example.books.infrastructure.broker;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "application.kafka.topics", ignoreUnknownFields = false)
public class KafkaTopicProperties {

    private String raw;

    private String parsed;

    private String indexed;

    private String dlq;

    private String sse;

    public String getRaw() {
        return raw;
    }

    public void setRaw(String raw) {
        this.raw = raw;
    }

    public String getParsed() {
        return parsed;
    }

    public void setParsed(String parsed) {
        this.parsed = parsed;
    }

    public String getIndexed() {
        return indexed;
    }

    public void setIndexed(String indexed) {
        this.indexed = indexed;
    }

    public String getDlq() {
        return dlq;
    }

    public void setDlq(String dlq) {
        this.dlq = dlq;
    }

    public String getSse() {
        return sse;
    }

    public void setSse(String sse) {
        this.sse = sse;
    }
}
