package com.example.books.domain.service.bookfile.impl;

import com.example.books.domain.core.book.Book;
import com.example.books.domain.core.bookfile.BookFile;
import com.example.books.domain.repository.bookfile.BookFileDataAccessRepository;
import com.example.books.domain.repository.bookfile.BookFileRepository;
import com.example.books.domain.service.bookfile.BookFileService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BookFileServiceImpl implements BookFileService {

    private static final Logger LOG = LoggerFactory.getLogger(BookFileServiceImpl.class);

    private final BookFileRepository bookFileRepository;
    private final BookFileDataAccessRepository bookFileDataAccessRepository;

    public BookFileServiceImpl(BookFileRepository bookFileRepository, BookFileDataAccessRepository bookFileDataAccessRepository) {
        this.bookFileRepository = bookFileRepository;
        this.bookFileDataAccessRepository = bookFileDataAccessRepository;
    }

    private static BookFile merge(BookFile existing, BookFile incoming) {
        String pathNorm = incoming.pathNorm() != null ? incoming.pathNorm() : existing.pathNorm();
        String sha256 = incoming.sha256() != null ? incoming.sha256() : existing.sha256();
        Long sizeBytes = incoming.sizeBytes() != null ? incoming.sizeBytes() : existing.sizeBytes();
        var mtime = incoming.mtime() != null ? incoming.mtime() : existing.mtime();
        String storageUri = incoming.storageUri() != null ? incoming.storageUri() : existing.storageUri();
        var firstSeenAt = incoming.firstSeenAt() != null ? incoming.firstSeenAt() : existing.firstSeenAt();
        var lastSeenAt = incoming.lastSeenAt() != null ? incoming.lastSeenAt() : existing.lastSeenAt();
        Book book = incoming.book() != null ? incoming.book() : existing.book();
        return new BookFile(existing.id(), pathNorm, sha256, sizeBytes, mtime, storageUri, firstSeenAt, lastSeenAt, book);
    }

    @Override
    public BookFile save(BookFile bookFile) {
        LOG.debug("Domain service: save BookFile {}", bookFile);
        return bookFileRepository.save(bookFile);
    }

    @Override
    public BookFile update(BookFile bookFile) {
        LOG.debug("Domain service: update BookFile {}", bookFile);
        return bookFileRepository.save(bookFile);
    }

    @Override
    public Optional<BookFile> partialUpdate(BookFile bookFile) {
        LOG.debug("Domain service: partial update BookFile {}", bookFile);
        return bookFileDataAccessRepository
            .findById(bookFile.id(), true)
            .map(existing -> merge(existing, bookFile))
            .map(bookFileRepository::save);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Domain service: delete BookFile {}", id);
        bookFileRepository.deleteById(id);
    }
}
