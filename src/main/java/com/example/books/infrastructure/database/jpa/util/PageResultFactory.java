package com.example.books.infrastructure.database.jpa.util;

import com.example.books.shared.pagination.PageResult;
import java.util.List;
import org.springframework.data.domain.Page;

/**
 * Converts Spring {@link Page} into {@link PageResult}.
 */
public final class PageResultFactory {

    private PageResultFactory() {}

    public static <T> PageResult<T> from(Page<T> page) {
        List<T> content = page.getContent();
        return new PageResult<>(content, page.getTotalElements(), page.getNumber(), page.getSize());
    }
}
