package com.example.books.domain.bookpage;

import com.example.books.infrastructure.database.jpa.entity.BookPageTextEntity;
import com.example.books.infrastructure.database.jpa.mapper.BookPageTextMapper;
import com.example.books.infrastructure.database.jpa.repository.BookPageTextJpaRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link BookPageTextEntity}.
 */
public class BookPageTextService {

    private static final Logger LOG = LoggerFactory.getLogger(BookPageTextService.class);

    private final BookPageTextJpaRepository bookPageTextRepository;

    private final BookPageTextMapper bookPageTextMapper;

    public BookPageTextService(BookPageTextJpaRepository bookPageTextRepository, BookPageTextMapper bookPageTextMapper) {
        this.bookPageTextRepository = bookPageTextRepository;
        this.bookPageTextMapper = bookPageTextMapper;
    }

    /**
     * Save a bookPageText.
     *
     * @param bookPageTextDTO the entity to save.
     * @return the persisted entity.
     */
    public BookPageText save(BookPageText bookPageTextDTO) {
        LOG.debug("Request to save BookPageText : {}", bookPageTextDTO);
        BookPageTextEntity bookPageText = bookPageTextMapper.toEntity(bookPageTextDTO);
        bookPageText = bookPageTextRepository.save(bookPageText);
        return bookPageTextMapper.toDto(bookPageText);
    }

    /**
     * Update a bookPageText.
     *
     * @param bookPageTextDTO the entity to save.
     * @return the persisted entity.
     */
    public BookPageText update(BookPageText bookPageTextDTO) {
        LOG.debug("Request to update BookPageText : {}", bookPageTextDTO);
        BookPageTextEntity bookPageText = bookPageTextMapper.toEntity(bookPageTextDTO);
        bookPageText = bookPageTextRepository.save(bookPageText);
        return bookPageTextMapper.toDto(bookPageText);
    }

    /**
     * Partially update a bookPageText.
     *
     * @param bookPageTextDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<BookPageText> partialUpdate(BookPageText bookPageTextDTO) {
        LOG.debug("Request to partially update BookPageText : {}", bookPageTextDTO);

        return bookPageTextRepository
            .findById(bookPageTextDTO.getId())
            .map(existingBookPageText -> {
                bookPageTextMapper.partialUpdate(existingBookPageText, bookPageTextDTO);

                return existingBookPageText;
            })
            .map(bookPageTextRepository::save)
            .map(bookPageTextMapper::toDto);
    }

    /**
     * Get all the bookPageTexts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<BookPageText> findAll(Pageable pageable) {
        LOG.debug("Request to get all BookPageTexts");
        return bookPageTextRepository.findAll(pageable).map(bookPageTextMapper::toDto);
    }

    /**
     * Get all the bookPageTexts with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<BookPageText> findAllWithEagerRelationships(Pageable pageable) {
        return bookPageTextRepository.findAllWithEagerRelationships(pageable).map(bookPageTextMapper::toDto);
    }

    /**
     * Get one bookPageText by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<BookPageText> findOne(Long id) {
        LOG.debug("Request to get BookPageText : {}", id);
        return bookPageTextRepository.findOneWithEagerRelationships(id).map(bookPageTextMapper::toDto);
    }

    /**
     * Delete the bookPageText by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete BookPageText : {}", id);
        bookPageTextRepository.deleteById(id);
    }
}
