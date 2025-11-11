package com.example.books.config;

import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to Books Etl.
 * <p>
 * Properties are configured in the {@code application.yml} file.
 * See {@link tech.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    private final Liquibase liquibase = new Liquibase();
    private final Books books = new Books();
    private final Kafka kafka = new Kafka();

    // jhipster-needle-application-properties-property

    public Liquibase getLiquibase() {
        return liquibase;
    }

    public Books getBooks() {
        return books;
    }

    public Kafka getKafka() {
        return kafka;
    }

    // jhipster-needle-application-properties-property-getter

    public static class Liquibase {

        private Boolean asyncStart = true;

        public Boolean getAsyncStart() {
            return asyncStart;
        }

        public void setAsyncStart(Boolean asyncStart) {
            this.asyncStart = asyncStart;
        }
    }

    public static class Books {

        private String root;
        private List<String> exclude = new ArrayList<>();

        public String getRoot() {
            return root;
        }

        public void setRoot(String root) {
            this.root = root;
        }

        public List<String> getExclude() {
            return exclude;
        }

        public void setExclude(List<String> exclude) {
            this.exclude = exclude;
        }
    }

    public static class Kafka {

        private final Topics topics = new Topics();

        public Topics getTopics() {
            return topics;
        }

        public static class Topics {

            private String raw;
            private String parsed;
            private String indexed;
            private String dlq;

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
        }
    }
    // jhipster-needle-application-properties-property-class
}
