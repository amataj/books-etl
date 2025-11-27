package com.example.books.etl.infrastructure.config;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
    private final Camel camel = new Camel();
    private final Ocr ocr = new Ocr();
    private final Temporal temporal = new Temporal();

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

    public Camel getCamel() {
        return camel;
    }

    public Ocr getOcr() {
        return ocr;
    }

    public Temporal getTemporal() {
        return temporal;
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

        private String inbox;
        private String completed;
        private String error;
        private List<String> exclude = new ArrayList<>();

        public String getInbox() {
            return inbox;
        }

        public void setInbox(String inbox) {
            this.inbox = inbox;
        }

        public String getCompleted() {
            return completed;
        }

        public void setCompleted(String completed) {
            this.completed = completed;
        }

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
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

    public static class Camel {

        private final Routes routes = new Routes();

        public Routes getRoutes() {
            return routes;
        }

        public static class Routes {

            private final PdfIngest pdfIngest = new PdfIngest();

            public PdfIngest getPdfIngest() {
                return pdfIngest;
            }

            public static class PdfIngest {

                private boolean enabled;

                public boolean isEnabled() {
                    return enabled;
                }

                public void setEnabled(boolean enabled) {
                    this.enabled = enabled;
                }
            }
        }
    }

    public static class Ocr {

        private final Map<String, String> tessVariables = new LinkedHashMap<>();
        private boolean enabled;
        private String dataPath;
        private String language = "eng";
        private Integer pageSegMode;
        private Integer ocrEngineMode;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public String getDataPath() {
            return dataPath;
        }

        public void setDataPath(String dataPath) {
            this.dataPath = dataPath;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public Integer getPageSegMode() {
            return pageSegMode;
        }

        public void setPageSegMode(Integer pageSegMode) {
            this.pageSegMode = pageSegMode;
        }

        public Integer getOcrEngineMode() {
            return ocrEngineMode;
        }

        public void setOcrEngineMode(Integer ocrEngineMode) {
            this.ocrEngineMode = ocrEngineMode;
        }

        public Map<String, String> getTessVariables() {
            return tessVariables;
        }
    }

    public static class Temporal {

        private String namespace;
        private String serverAddress;

        public String getNamespace() {
            return namespace;
        }

        public void setNamespace(String namespace) {
            this.namespace = namespace;
        }

        public String getServerAddress() {
            return serverAddress;
        }

        public void setServerAddress(String serverAddress) {
            this.serverAddress = serverAddress;
        }
    }
    // jhipster-needle-application-properties-property-class
}
