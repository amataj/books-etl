package com.example.books.usecase.bookpagetext;

import com.example.books.domain.bookpage.BookPageText;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Use case boundary for book page text operations.
 */
public interface BookPageTextUseCase {
    BookPageText create(BookPageText bookPageText);

    BookPageText update(BookPageText bookPageText);

    Optional<BookPageText> partialUpdate(BookPageText bookPageText);

    Page<BookPageText> findAll(Pageable pageable);

    Page<BookPageText> findAllWithEagerRelationships(Pageable pageable);

    Optional<BookPageText> findOne(Long id);

    void delete(Long id);

    boolean exists(Long id);
}
