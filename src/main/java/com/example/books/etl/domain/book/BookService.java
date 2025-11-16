package com.example.books.etl.domain.book;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Service Implementation for managing {@link Book}.
 */
public class BookService {

    private static final Logger LOG = LoggerFactory.getLogger(BookService.class);

    private final BookCommandRepository bookCommandRepository;
    private final BookQueryRepository bookQueryRepository;

    public BookService(BookCommandRepository bookCommandRepository, BookQueryRepository bookQueryRepository) {
        this.bookCommandRepository = bookCommandRepository;
        this.bookQueryRepository = bookQueryRepository;
    }

    /**
     * Save a book.
     *
     * @param book the entity to save.
     * @return the persisted entity.
     */
    public Book save(Book book) {
        LOG.debug("Request to save Book : {}", book);
        Optional<Book> existingBook = bookQueryRepository.findByTitle(book.getTitle());
        if (existingBook.isPresent()) {
            LOG.info("Book with title {} already exists", book.getTitle());
            throw new IllegalArgumentException("Book with title " + book.getTitle() + " already exists");
        }
        return bookCommandRepository.save(book);
    }

    /**
     * Update a book.
     *
     * @param book the entity to save.
     * @return the persisted entity.
     */
    public Book update(Book book) {
        LOG.debug("Request to update Book : {}", book);
        if (bookQueryRepository.findById(book.getId()).isEmpty()) {
            LOG.info("Book with id {} does not exist", book.getId());
            throw new IllegalArgumentException("Book with id " + book.getId() + " does not exist");
        }
        return bookCommandRepository.save(book);
    }

    /**
     * Partially update a book.
     *
     * @param book the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Book> partialUpdate(Book book) {
        LOG.debug("Request to partially update Book : {}", book);

        Optional<Book> optionalBook = bookQueryRepository.findById(book.getId());
        if (optionalBook.isEmpty()) {
            LOG.info("Book with id {} does not exist", book.getId());
            return Optional.empty();
        }
        Book existingBook = optionalBook.orElseThrow();
        partialUpdate(existingBook, book);

        return Optional.ofNullable(bookCommandRepository.save(existingBook));
    }

    /**
     * Delete the book by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Book : {}", id);
        if (bookQueryRepository.findById(id).isEmpty()) {
            LOG.info("Book with id {} does not exist", id);
            throw new IllegalArgumentException("Book with id " + id + " does not exist");
        }
        bookCommandRepository.deleteById(id);
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
