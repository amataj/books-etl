package com.example.books.usecase.book.impl;

import com.example.books.domain.book.Book;
import com.example.books.domain.book.BookDataAccessRepository;
import com.example.books.domain.book.BookService;
import com.example.books.shared.pagination.PageCriteria;
import com.example.books.shared.pagination.PageResult;
import com.example.books.usecase.book.BookUseCase;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BookUseCaseImpl implements BookUseCase {

    private final BookService bookService;
    private final BookDataAccessRepository bookDataAccessRepository;

    public BookUseCaseImpl(BookService bookService, BookDataAccessRepository bookDataAccessRepository) {
        this.bookService = bookService;
        this.bookDataAccessRepository = bookDataAccessRepository;
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
        PageResult<Book> all = bookDataAccessRepository.findAll(new PageCriteria(pageable.getPageNumber(), pageable.getPageSize()));
        return new PageImpl<>(all.content(), pageable, all.totalElements());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Book> findOne(Long id) {
        return bookDataAccessRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        bookService.delete(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean exists(Long id) {
        return bookDataAccessRepository.findById(id).isPresent();
    }
}
