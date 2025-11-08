package com.example.books.adapter.web.rest.util;

import com.example.books.shared.pagination.PageResult;
import java.util.List;
import java.util.function.Function;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public final class PageResponseFactory {

    private PageResponseFactory() {}

    public static <T, R> Page<R> toPage(PageResult<T> result, Pageable pageable, Function<T, R> mapper) {
        List<R> content = result.content().stream().map(mapper).toList();
        return new PageImpl<>(content, pageable, result.totalElements());
    }
}
