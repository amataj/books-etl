package com.example.books.service;

import com.example.books.infrastructure.infrastructure.database.jpa.entity.BookPageText;
import com.example.books.infrastructure.infrastructure.database.jpa.repository.BookPageTextRepository;
import com.example.books.service.dto.BookPageTextDTO;
import com.example.books.service.mapper.BookPageTextMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link BookPageText}.
 */
@Service
@Transactional
public class BookPageTextService {

    private static final Logger LOG = LoggerFactory.getLogger(BookPageTextService.class);

    private final BookPageTextRepository bookPageTextRepository;

    private final BookPageTextMapper bookPageTextMapper;

    public BookPageTextService(BookPageTextRepository bookPageTextRepository, BookPageTextMapper bookPageTextMapper) {
        this.bookPageTextRepository = bookPageTextRepository;
        this.bookPageTextMapper = bookPageTextMapper;
    }

    /**
     * Save a bookPageText.
     *
     * @param bookPageTextDTO the entity to save.
     * @return the persisted entity.
     */
    public BookPageTextDTO save(BookPageTextDTO bookPageTextDTO) {
        LOG.debug("Request to save BookPageText : {}", bookPageTextDTO);
        BookPageText bookPageText = bookPageTextMapper.toEntity(bookPageTextDTO);
        bookPageText = bookPageTextRepository.save(bookPageText);
        return bookPageTextMapper.toDto(bookPageText);
    }

    /**
     * Update a bookPageText.
     *
     * @param bookPageTextDTO the entity to save.
     * @return the persisted entity.
     */
    public BookPageTextDTO update(BookPageTextDTO bookPageTextDTO) {
        LOG.debug("Request to update BookPageText : {}", bookPageTextDTO);
        BookPageText bookPageText = bookPageTextMapper.toEntity(bookPageTextDTO);
        bookPageText = bookPageTextRepository.save(bookPageText);
        return bookPageTextMapper.toDto(bookPageText);
    }

    /**
     * Partially update a bookPageText.
     *
     * @param bookPageTextDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<BookPageTextDTO> partialUpdate(BookPageTextDTO bookPageTextDTO) {
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
    public Page<BookPageTextDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all BookPageTexts");
        return bookPageTextRepository.findAll(pageable).map(bookPageTextMapper::toDto);
    }

    /**
     * Get all the bookPageTexts with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<BookPageTextDTO> findAllWithEagerRelationships(Pageable pageable) {
        return bookPageTextRepository.findAllWithEagerRelationships(pageable).map(bookPageTextMapper::toDto);
    }

    /**
     * Get one bookPageText by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<BookPageTextDTO> findOne(Long id) {
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
