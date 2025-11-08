package com.example.books.adapter.web.rest.util;

import com.example.books.shared.pagination.PageCriteria;
import org.springframework.data.domain.Pageable;

public final class PageCriteriaFactory {

    private PageCriteriaFactory() {}

    public static PageCriteria from(Pageable pageable) {
        return new PageCriteria(pageable.getPageNumber(), pageable.getPageSize());
    }
}
