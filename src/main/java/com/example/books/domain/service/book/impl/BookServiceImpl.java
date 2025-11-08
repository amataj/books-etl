package com.example.books.domain.service.book.impl;

import com.example.books.domain.core.book.Book;
import com.example.books.domain.repository.book.BookDataAccessRepository;
import com.example.books.domain.repository.book.BookRepository;
import com.example.books.domain.service.book.BookService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Framework-free implementation of {@link BookService}.
 */
public class BookServiceImpl implements BookService {

    private static final Logger LOG = LoggerFactory.getLogger(BookServiceImpl.class);

    private final BookRepository bookRepository;
    private final BookDataAccessRepository bookDataAccessRepository;

    public BookServiceImpl(BookRepository bookRepository, BookDataAccessRepository bookDataAccessRepository) {
        this.bookRepository = bookRepository;
        this.bookDataAccessRepository = bookDataAccessRepository;
    }

    @Override
    public Book save(Book book) {
        LOG.debug("Domain service: save Book {}", book);
        return bookRepository.save(book);
    }

    @Override
    public Book update(Book book) {
        LOG.debug("Domain service: update Book {}", book);
        return bookRepository.save(book);
    }

    @Override
    public Optional<Book> partialUpdate(Book book) {
        LOG.debug("Domain service: partial update Book {}", book);
        return bookDataAccessRepository
            .findById(book.id())
            .map(existing ->
                new Book(
                    existing.id(),
                    existing.documentId(),
                    book.title() != null ? book.title() : existing.title(),
                    book.author() != null ? book.author() : existing.author(),
                    book.lang() != null ? book.lang() : existing.lang(),
                    book.pages() != null ? book.pages() : existing.pages()
                )
            )
            .map(bookRepository::save);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Domain service: delete Book {}", id);
        bookRepository.deleteById(id);
    }
}
