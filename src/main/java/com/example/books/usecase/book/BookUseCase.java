package com.example.books.usecase.book;

import com.example.books.domain.core.book.Book;
import com.example.books.shared.pagination.PageCriteria;
import com.example.books.shared.pagination.PageResult;
import java.util.Optional;

public interface BookUseCase {
    Book save(Book book);

    Book update(Book book);

    Optional<Book> partialUpdate(Book book);

    PageResult<Book> findAll(PageCriteria criteria);

    Optional<Book> findOne(Long id);

    void delete(Long id);

    boolean existsById(Long id);
}
