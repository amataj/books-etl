package com.example.books.usecase.bookpagetext;

import com.example.books.domain.core.bookpagetext.BookPageText;
import com.example.books.shared.pagination.PageCriteria;
import com.example.books.shared.pagination.PageResult;
import java.util.Optional;

public interface BookPageTextUseCase {
    BookPageText save(BookPageText bookPageText);

    BookPageText update(BookPageText bookPageText);

    Optional<BookPageText> partialUpdate(BookPageText bookPageText);

    PageResult<BookPageText> findAll(PageCriteria criteria, boolean eagerRelationships);

    Optional<BookPageText> findOne(Long id, boolean eagerRelationships);

    void delete(Long id);

    boolean existsById(Long id);
}
