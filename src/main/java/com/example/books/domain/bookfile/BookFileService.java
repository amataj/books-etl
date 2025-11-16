package com.example.books.domain.bookfile;

import com.example.books.shared.pagination.PageCriteria;
import com.example.books.shared.pagination.PageResult;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

/**
 * Domain service for managing {@link BookFile} aggregates.
 */
public class BookFileService {

    private static final Logger LOG = LoggerFactory.getLogger(BookFileService.class);

    private final BookFileCommandRepository bookFileCommandRepository;
    private final BookFileQueryRepository bookFileQueryRepository;

    public BookFileService(BookFileCommandRepository bookFileCommandRepository, BookFileQueryRepository bookFileQueryRepository) {
        this.bookFileCommandRepository = bookFileCommandRepository;
        this.bookFileQueryRepository = bookFileQueryRepository;
    }

    /**
     * Save a bookFile.
     *
     * @param bookFile the aggregate to save.
     * @return the persisted aggregate.
     */
    public BookFile save(BookFile bookFile) {
        LOG.debug("Request to save BookFile : {}", bookFile);
        return bookFileCommandRepository.save(bookFile);
    }

    /**
     * Update a bookFile.
     *
     * @param bookFile the aggregate to update.
     * @return the persisted aggregate.
     */
    public BookFile update(BookFile bookFile) {
        LOG.debug("Request to update BookFile : {}", bookFile);
        return bookFileCommandRepository.save(bookFile);
    }

    /**
     * Partially update a bookFile.
     *
     * @param bookFile the aggregate to update partially.
     * @return the persisted aggregate.
     */
    public Optional<BookFile> partialUpdate(BookFile bookFile) {
        LOG.debug("Request to partially update BookFile : {}", bookFile);
        if (bookFile.getId() == null) {
            return Optional.empty();
        }

        return bookFileQueryRepository
            .findById(bookFile.getId(), true)
            .map(existingBookFile -> {
                partialUpdate(existingBookFile, bookFile);
                return bookFileCommandRepository.save(existingBookFile);
            });
    }

    /**
     * Get all the bookFiles.
     *
     * @param pageable the pagination information.
     * @return the list of aggregates.
     */
    public Page<BookFile> findAll(Pageable pageable) {
        LOG.debug("Request to get all BookFiles");
        PageResult<BookFile> result = bookFileQueryRepository.findAll(
            new PageCriteria(pageable.getPageNumber(), pageable.getPageSize()),
            false
        );
        return new PageImpl<>(result.content(), pageable, result.totalElements());
    }

    /**
     * Get all the bookFiles with eager load of relationships.
     *
     * @param pageable the pagination information.
     * @return the list of aggregates.
     */
    public Page<BookFile> findAllWithEagerRelationships(Pageable pageable) {
        LOG.debug("Request to get all BookFiles with eager relationships");
        PageResult<BookFile> result = bookFileQueryRepository.findAll(
            new PageCriteria(pageable.getPageNumber(), pageable.getPageSize()),
            true
        );
        return new PageImpl<>(result.content(), pageable, result.totalElements());
    }

    /**
     * Get one bookFile by id.
     *
     * @param id the id of the aggregate.
     * @return the aggregate.
     */
    public Optional<BookFile> findOne(Long id) {
        LOG.debug("Request to get BookFile : {}", id);
        return bookFileQueryRepository.findById(id, true);
    }

    /**
     * Delete the bookFile by id.
     *
     * @param id the id of the aggregate.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete BookFile : {}", id);
        bookFileCommandRepository.deleteById(id);
    }

    private void partialUpdate(BookFile existing, BookFile incoming) {
        if (incoming == null) {
            return;
        }

        if (incoming.getId() != null) {
            existing.setId(incoming.getId());
        }
        if (incoming.getPathNorm() != null) {
            existing.setPathNorm(incoming.getPathNorm());
        }
        if (incoming.getSha256() != null) {
            existing.setSha256(incoming.getSha256());
        }
        if (incoming.getSizeBytes() != null) {
            existing.setSizeBytes(incoming.getSizeBytes());
        }
        if (incoming.getMtime() != null) {
            existing.setMtime(incoming.getMtime());
        }
        if (incoming.getStorageUri() != null) {
            existing.setStorageUri(incoming.getStorageUri());
        }
        if (incoming.getFirstSeenAt() != null) {
            existing.setFirstSeenAt(incoming.getFirstSeenAt());
        }
        if (incoming.getLastSeenAt() != null) {
            existing.setLastSeenAt(incoming.getLastSeenAt());
        }
        if (incoming.getBook() != null) {
            existing.setBook(incoming.getBook());
        }
    }
}
