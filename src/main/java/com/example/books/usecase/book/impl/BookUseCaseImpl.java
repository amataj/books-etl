package com.example.books.usecase.book.impl;

import com.example.books.domain.core.book.Book;
import com.example.books.domain.repository.book.BookDataAccessRepository;
import com.example.books.domain.service.book.BookService;
import com.example.books.shared.pagination.PageCriteria;
import com.example.books.shared.pagination.PageResult;
import com.example.books.usecase.book.BookUseCase;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BookUseCaseImpl implements BookUseCase {

    private static final Logger LOG = LoggerFactory.getLogger(BookUseCaseImpl.class);

    private final BookService bookService;
    private final BookDataAccessRepository bookDataAccessRepository;

    public BookUseCaseImpl(BookService bookService, BookDataAccessRepository bookDataAccessRepository) {
        this.bookService = bookService;
        this.bookDataAccessRepository = bookDataAccessRepository;
    }

    @Override
    public Book save(Book book) {
        LOG.debug("Use case: save Book {}", book);
        return bookService.save(book);
    }

    @Override
    public Book update(Book book) {
        LOG.debug("Use case: update Book {}", book);
        return bookService.update(book);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Book> findOne(Long id) {
        return bookDataAccessRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResult<Book> findAll(PageCriteria criteria) {
        return bookDataAccessRepository.findAll(criteria);
    }

    @Override
    public Optional<Book> partialUpdate(Book book) {
        LOG.debug("Use case: partial update Book {}", book);
        return bookService.partialUpdate(book);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Use case: delete Book {}", id);
        bookService.delete(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return bookDataAccessRepository.findById(id).isPresent();
    }
}
