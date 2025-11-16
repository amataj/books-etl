package com.example.books.domain.book;

import com.example.books.infrastructure.database.jpa.entity.BookEntity;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Book}.
 */
public class BookService {

    private static final Logger LOG = LoggerFactory.getLogger(BookService.class);

    private final BookRepository bookRepository;
    private final BookDataAccessRepository bookDataAccessRepository;

    public BookService(BookRepository bookRepository, BookDataAccessRepository bookDataAccessRepository) {
        this.bookRepository = bookRepository;
        this.bookDataAccessRepository = bookDataAccessRepository;
    }

    /**
     * Save a book.
     *
     * @param book the entity to save.
     * @return the persisted entity.
     */
    public Book save(Book book) {
        LOG.debug("Request to save Book : {}", book);
        Optional<Book> existingBook = bookDataAccessRepository.findByTitle(book.getTitle());
        if (existingBook.isPresent()) {
            LOG.info("Book with title {} already exists", book.getTitle());
            throw new IllegalArgumentException("Book with title " + book.getTitle() + " already exists");
        }
        return bookRepository.save(book);
    }

    /**
     * Update a book.
     *
     * @param book the entity to save.
     * @return the persisted entity.
     */
    public Book update(Book book) {
        LOG.debug("Request to update Book : {}", book);
        if (bookDataAccessRepository.findById(book.getId()).isEmpty()) {
            LOG.info("Book with id {} does not exist", book.getId());
            throw new IllegalArgumentException("Book with id " + book.getId() + " does not exist");
        }
        return bookRepository.save(book);
    }

    /**
     * Partially update a book.
     *
     * @param book the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Book> partialUpdate(Book book) {
        LOG.debug("Request to partially update Book : {}", book);

        Optional<Book> optionalBook = bookDataAccessRepository.findById(book.getId());
        if (optionalBook.isEmpty()) {
            LOG.info("Book with id {} does not exist", book.getId());
            return Optional.empty();
        }
        Book existingBook = optionalBook.get();
        partialUpdate(existingBook, book);

        return Optional.ofNullable(bookRepository.save(existingBook));
    }

    /**
     * Delete the book by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Book : {}", id);
        if (bookDataAccessRepository.findById(id).isEmpty()) {
            LOG.info("Book with id {} does not exist", id);
            throw new IllegalArgumentException("Book with id " + id + " does not exist");
        }
        bookRepository.deleteById(id);
    }

    private void partialUpdate(Book existingBook, Book book) {
        if (book == null) {
            return;
        }

        if (book.getId() != null) {
            existingBook.setId(book.getId());
        }
        if (book.getDocumentId() != null) {
            existingBook.setDocumentId(book.getDocumentId());
        }
        if (book.getTitle() != null) {
            existingBook.setTitle(book.getTitle());
        }
        if (book.getAuthor() != null) {
            existingBook.setAuthor(book.getAuthor());
        }
        if (book.getLang() != null) {
            existingBook.setLang(book.getLang());
        }
        if (book.getPages() != null) {
            existingBook.setPages(book.getPages());
        }
    }
}
