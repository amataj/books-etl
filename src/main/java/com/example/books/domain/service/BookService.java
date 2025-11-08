package com.example.books.domain.service;

import com.example.books.domain.core.Book;
import com.example.books.infrastructure.database.jpa.entity.BookEntity;
import com.example.books.infrastructure.database.jpa.mapper.BookMapper;
import com.example.books.infrastructure.database.jpa.repository.BookJpaRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link BookEntity}.
 */
@Service
@Transactional
public class BookService {

    private static final Logger LOG = LoggerFactory.getLogger(BookService.class);

    private final BookJpaRepository bookRepository;

    private final BookMapper bookMapper;

    public BookService(BookJpaRepository bookRepository, BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
    }

    /**
     * Save a book.
     *
     * @param bookDTO the entity to save.
     * @return the persisted entity.
     */
    public Book save(Book bookDTO) {
        LOG.debug("Request to save Book : {}", bookDTO);
        BookEntity book = bookMapper.toEntity(bookDTO);
        book = bookRepository.save(book);
        return bookMapper.toDto(book);
    }

    /**
     * Update a book.
     *
     * @param bookDTO the entity to save.
     * @return the persisted entity.
     */
    public Book update(Book bookDTO) {
        LOG.debug("Request to update Book : {}", bookDTO);
        BookEntity book = bookMapper.toEntity(bookDTO);
        book = bookRepository.save(book);
        return bookMapper.toDto(book);
    }

    /**
     * Partially update a book.
     *
     * @param bookDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Book> partialUpdate(Book bookDTO) {
        LOG.debug("Request to partially update Book : {}", bookDTO);

        return bookRepository
            .findById(bookDTO.getId())
            .map(existingBook -> {
                bookMapper.partialUpdate(existingBook, bookDTO);

                return existingBook;
            })
            .map(bookRepository::save)
            .map(bookMapper::toDto);
    }

    /**
     * Get all the books.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Book> findAll(Pageable pageable) {
        LOG.debug("Request to get all Books");
        return bookRepository.findAll(pageable).map(bookMapper::toDto);
    }

    /**
     * Get one book by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Book> findOne(Long id) {
        LOG.debug("Request to get Book : {}", id);
        return bookRepository.findById(id).map(bookMapper::toDto);
    }

    /**
     * Delete the book by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Book : {}", id);
        bookRepository.deleteById(id);
    }
}
