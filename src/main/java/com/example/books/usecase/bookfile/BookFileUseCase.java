package com.example.books.usecase.bookfile;

import com.example.books.domain.core.BookFile;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Use case boundary for book file orchestration.
 */
public interface BookFileUseCase {
    BookFile create(BookFile bookFile);

    BookFile update(BookFile bookFile);

    Optional<BookFile> partialUpdate(BookFile bookFile);

    Page<BookFile> findAll(Pageable pageable);

    Page<BookFile> findAllWithEagerRelationships(Pageable pageable);

    Optional<BookFile> findOne(Long id);

    void delete(Long id);

    boolean exists(Long id);
}
