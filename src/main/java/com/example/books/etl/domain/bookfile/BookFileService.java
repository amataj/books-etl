package com.example.books.etl.domain.bookfile;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
