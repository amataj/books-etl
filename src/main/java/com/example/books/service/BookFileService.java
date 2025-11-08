package com.example.books.service;

import com.example.books.infrastructure.infrastructure.database.jpa.entity.BookFileEntity;
import com.example.books.infrastructure.infrastructure.database.jpa.repository.BookFileRepository;
import com.example.books.service.dto.BookFileDTO;
import com.example.books.service.mapper.BookFileMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link BookFileEntity}.
 */
@Service
@Transactional
public class BookFileService {

    private static final Logger LOG = LoggerFactory.getLogger(BookFileService.class);

    private final BookFileRepository bookFileRepository;

    private final BookFileMapper bookFileMapper;

    public BookFileService(BookFileRepository bookFileRepository, BookFileMapper bookFileMapper) {
        this.bookFileRepository = bookFileRepository;
        this.bookFileMapper = bookFileMapper;
    }

    /**
     * Save a bookFile.
     *
     * @param bookFileDTO the entity to save.
     * @return the persisted entity.
     */
    public BookFileDTO save(BookFileDTO bookFileDTO) {
        LOG.debug("Request to save BookFile : {}", bookFileDTO);
        BookFileEntity bookFile = bookFileMapper.toEntity(bookFileDTO);
        bookFile = bookFileRepository.save(bookFile);
        return bookFileMapper.toDto(bookFile);
    }

    /**
     * Update a bookFile.
     *
     * @param bookFileDTO the entity to save.
     * @return the persisted entity.
     */
    public BookFileDTO update(BookFileDTO bookFileDTO) {
        LOG.debug("Request to update BookFile : {}", bookFileDTO);
        BookFileEntity bookFile = bookFileMapper.toEntity(bookFileDTO);
        bookFile = bookFileRepository.save(bookFile);
        return bookFileMapper.toDto(bookFile);
    }

    /**
     * Partially update a bookFile.
     *
     * @param bookFileDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<BookFileDTO> partialUpdate(BookFileDTO bookFileDTO) {
        LOG.debug("Request to partially update BookFile : {}", bookFileDTO);

        return bookFileRepository
            .findById(bookFileDTO.getId())
            .map(existingBookFile -> {
                bookFileMapper.partialUpdate(existingBookFile, bookFileDTO);

                return existingBookFile;
            })
            .map(bookFileRepository::save)
            .map(bookFileMapper::toDto);
    }

    /**
     * Get all the bookFiles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<BookFileDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all BookFiles");
        return bookFileRepository.findAll(pageable).map(bookFileMapper::toDto);
    }

    /**
     * Get all the bookFiles with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<BookFileDTO> findAllWithEagerRelationships(Pageable pageable) {
        return bookFileRepository.findAllWithEagerRelationships(pageable).map(bookFileMapper::toDto);
    }

    /**
     * Get one bookFile by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<BookFileDTO> findOne(Long id) {
        LOG.debug("Request to get BookFile : {}", id);
        return bookFileRepository.findOneWithEagerRelationships(id).map(bookFileMapper::toDto);
    }

    /**
     * Delete the bookFile by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete BookFile : {}", id);
        bookFileRepository.deleteById(id);
    }
}
