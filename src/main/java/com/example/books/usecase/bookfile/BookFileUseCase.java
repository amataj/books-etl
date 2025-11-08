package com.example.books.usecase.bookfile;

import com.example.books.domain.core.bookfile.BookFile;
import com.example.books.shared.pagination.PageCriteria;
import com.example.books.shared.pagination.PageResult;
import java.util.Optional;

public interface BookFileUseCase {
    BookFile save(BookFile bookFile);

    BookFile update(BookFile bookFile);

    Optional<BookFile> partialUpdate(BookFile bookFile);

    PageResult<BookFile> findAll(PageCriteria criteria, boolean eagerRelationships);

    Optional<BookFile> findOne(Long id, boolean eagerRelationships);

    void delete(Long id);

    boolean existsById(Long id);
}
