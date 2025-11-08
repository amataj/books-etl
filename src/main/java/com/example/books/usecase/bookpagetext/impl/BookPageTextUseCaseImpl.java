package com.example.books.usecase.bookpagetext.impl;

import com.example.books.domain.core.bookpagetext.BookPageText;
import com.example.books.domain.repository.bookpagetext.BookPageTextDataAccessRepository;
import com.example.books.domain.service.bookpagetext.BookPageTextService;
import com.example.books.shared.pagination.PageCriteria;
import com.example.books.shared.pagination.PageResult;
import com.example.books.usecase.bookpagetext.BookPageTextUseCase;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BookPageTextUseCaseImpl implements BookPageTextUseCase {

    private static final Logger LOG = LoggerFactory.getLogger(BookPageTextUseCaseImpl.class);

    private final BookPageTextService bookPageTextService;
    private final BookPageTextDataAccessRepository bookPageTextDataAccessRepository;

    public BookPageTextUseCaseImpl(
        BookPageTextService bookPageTextService,
        BookPageTextDataAccessRepository bookPageTextDataAccessRepository
    ) {
        this.bookPageTextService = bookPageTextService;
        this.bookPageTextDataAccessRepository = bookPageTextDataAccessRepository;
    }

    @Override
    public BookPageText save(BookPageText bookPageText) {
        LOG.debug("Use case: save BookPageText {}", bookPageText);
        return bookPageTextService.save(bookPageText);
    }

    @Override
    public BookPageText update(BookPageText bookPageText) {
        LOG.debug("Use case: update BookPageText {}", bookPageText);
        return bookPageTextService.update(bookPageText);
    }

    @Override
    public Optional<BookPageText> partialUpdate(BookPageText bookPageText) {
        LOG.debug("Use case: partial update BookPageText {}", bookPageText);
        return bookPageTextService.partialUpdate(bookPageText);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResult<BookPageText> findAll(PageCriteria criteria, boolean eagerRelationships) {
        return bookPageTextDataAccessRepository.findAll(criteria, eagerRelationships);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BookPageText> findOne(Long id, boolean eagerRelationships) {
        return bookPageTextDataAccessRepository.findById(id, eagerRelationships);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Use case: delete BookPageText {}", id);
        bookPageTextService.delete(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return bookPageTextDataAccessRepository.findById(id, false).isPresent();
    }
}
