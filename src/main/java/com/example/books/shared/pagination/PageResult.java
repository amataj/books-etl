package com.example.books.shared.pagination;

import java.util.List;

/**
 * Simple page container used to share paged data without Spring dependencies.
 */
public record PageResult<T>(List<T> content, long totalElements, int pageNumber, int pageSize) {
    public int totalPages() {
        if (pageSize == 0) {
            return 0;
        }
        return (int) Math.ceil((double) totalElements / (double) pageSize);
    }
}
