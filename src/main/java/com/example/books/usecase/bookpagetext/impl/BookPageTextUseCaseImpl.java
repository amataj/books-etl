package com.example.books.usecase.bookpagetext.impl;

import com.example.books.domain.core.BookPageText;
import com.example.books.domain.service.BookPageTextService;
import com.example.books.usecase.bookpagetext.BookPageTextUseCase;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BookPageTextUseCaseImpl implements BookPageTextUseCase {

    private final BookPageTextService bookPageTextService;

    public BookPageTextUseCaseImpl(BookPageTextService bookPageTextService) {
        this.bookPageTextService = bookPageTextService;
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
        return bookPageTextService.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BookPageText> findAllWithEagerRelationships(Pageable pageable) {
        return bookPageTextService.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BookPageText> findOne(Long id) {
        return bookPageTextService.findOne(id);
    }

    @Override
    public void delete(Long id) {
        bookPageTextService.delete(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean exists(Long id) {
        return bookPageTextService.exists(id);
    }
}
