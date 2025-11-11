package com.example.books.usecase.bookpagetext.impl;

import com.example.books.domain.bookpage.BookPageText;
import com.example.books.domain.bookpage.BookPageTextDataAccessRepository;
import com.example.books.domain.bookpage.BookPageTextService;
import com.example.books.shared.pagination.PageCriteria;
import com.example.books.shared.pagination.PageResult;
import com.example.books.usecase.bookpagetext.BookPageTextUseCase;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BookPageTextUseCaseImpl implements BookPageTextUseCase {

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
    public BookPageText create(BookPageText bookPageText) {
        return bookPageTextService.save(bookPageText);
    }

    @Override
    public BookPageText update(BookPageText bookPageText) {
        return bookPageTextService.update(bookPageText);
    }

    @Override
    public Optional<BookPageText> partialUpdate(BookPageText bookPageText) {
        return bookPageTextService.partialUpdate(bookPageText);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BookPageText> findAll(Pageable pageable) {
        PageResult<BookPageText> all = bookPageTextDataAccessRepository.findAll(
            new PageCriteria(pageable.getPageNumber(), pageable.getPageSize()),
            false
        );
        return new PageImpl<>(all.content(), pageable, all.totalElements());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BookPageText> findAllWithEagerRelationships(Pageable pageable) {
        PageResult<BookPageText> all = bookPageTextDataAccessRepository.findAll(
            new PageCriteria(pageable.getPageNumber(), pageable.getPageSize()),
            true
        );
        return new PageImpl<>(all.content(), pageable, all.totalElements());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BookPageText> findOne(Long id) {
        return bookPageTextDataAccessRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        bookPageTextService.delete(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean exists(Long id) {
        return bookPageTextDataAccessRepository.findById(id).isPresent();
    }
}
