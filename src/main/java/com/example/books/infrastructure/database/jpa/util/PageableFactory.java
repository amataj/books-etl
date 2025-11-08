package com.example.books.infrastructure.database.jpa.util;

import com.example.books.shared.pagination.PageCriteria;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

/**
 * Converts Clean Architecture pagination objects into Spring Data {@link Pageable}.
 */
public final class PageableFactory {

    private PageableFactory() {}

    public static Pageable from(PageCriteria criteria) {
        return PageRequest.of(criteria.page(), criteria.size());
    }
}
