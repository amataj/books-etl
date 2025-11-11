package com.example.books.config;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties describing how the ETL pipeline interacts with the local book repository.
 */
@ConfigurationProperties(prefix = "books", ignoreUnknownFields = false)
public class BooksProperties {

    private String root = Paths.get(System.getProperty("user.home"), "books").toString();

    private List<String> exclude = new ArrayList<>();

    public Path getRoot() {
        return Paths.get(root).toAbsolutePath().normalize();
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
