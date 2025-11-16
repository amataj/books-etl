package com.example.books.etl.shared.pagination;

/**
 * Simple pagination criteria used in the domain and use-case layers.
 */
public record PageCriteria(int page, int size) {
    public PageCriteria {
        if (page < 0) {
            throw new IllegalArgumentException("page must be >= 0");
        }
        if (size <= 0) {
            throw new IllegalArgumentException("size must be > 0");
        }
    }
}
