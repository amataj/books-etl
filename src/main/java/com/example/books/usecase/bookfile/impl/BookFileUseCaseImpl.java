package com.example.books.usecase.bookfile.impl;

import com.example.books.domain.core.bookfile.BookFile;
import com.example.books.domain.repository.bookfile.BookFileDataAccessRepository;
import com.example.books.domain.service.bookfile.BookFileService;
import com.example.books.shared.pagination.PageCriteria;
import com.example.books.shared.pagination.PageResult;
import com.example.books.usecase.bookfile.BookFileUseCase;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BookFileUseCaseImpl implements BookFileUseCase {

    private static final Logger LOG = LoggerFactory.getLogger(BookFileUseCaseImpl.class);

    private final BookFileService bookFileService;
    private final BookFileDataAccessRepository bookFileDataAccessRepository;

    public BookFileUseCaseImpl(BookFileService bookFileService, BookFileDataAccessRepository bookFileDataAccessRepository) {
        this.bookFileService = bookFileService;
        this.bookFileDataAccessRepository = bookFileDataAccessRepository;
    }

    @Override
    public BookFile save(BookFile bookFile) {
        LOG.debug("Use case: save BookFile {}", bookFile);
        return bookFileService.save(bookFile);
    }

    @Override
    public BookFile update(BookFile bookFile) {
        LOG.debug("Use case: update BookFile {}", bookFile);
        return bookFileService.update(bookFile);
    }

    @Override
    public Optional<BookFile> partialUpdate(BookFile bookFile) {
        LOG.debug("Use case: partial update BookFile {}", bookFile);
        return bookFileService.partialUpdate(bookFile);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResult<BookFile> findAll(PageCriteria criteria, boolean eagerRelationships) {
        return bookFileDataAccessRepository.findAll(criteria, eagerRelationships);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BookFile> findOne(Long id, boolean eagerRelationships) {
        return bookFileDataAccessRepository.findById(id, eagerRelationships);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Use case: delete BookFile {}", id);
        bookFileService.delete(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return bookFileDataAccessRepository.findById(id, false).isPresent();
    }
}
