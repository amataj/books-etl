package com.example.books.usecase.book.impl;

import com.example.books.domain.core.Book;
import com.example.books.domain.service.BookService;
import com.example.books.usecase.book.BookUseCase;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BookUseCaseImpl implements BookUseCase {

    private final BookService bookService;

    public BookUseCaseImpl(BookService bookService) {
        this.bookService = bookService;
    }

    @Override
    public Book create(Book book) {
        return bookService.save(book);
    }

    @Override
    public Book update(Book book) {
        return bookService.update(book);
    }

    @Override
    public Optional<Book> partialUpdate(Book book) {
        return bookService.partialUpdate(book);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Book> findAll(Pageable pageable) {
        return bookService.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Book> findOne(Long id) {
        return bookService.findOne(id);
    }

    @Override
    public void delete(Long id) {
        bookService.delete(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean exists(Long id) {
        return bookService.exists(id);
    }
}
