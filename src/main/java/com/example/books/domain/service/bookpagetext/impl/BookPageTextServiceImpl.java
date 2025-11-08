package com.example.books.domain.service.bookpagetext.impl;

import com.example.books.domain.core.book.Book;
import com.example.books.domain.core.bookpagetext.BookPageText;
import com.example.books.domain.repository.bookpagetext.BookPageTextDataAccessRepository;
import com.example.books.domain.repository.bookpagetext.BookPageTextRepository;
import com.example.books.domain.service.bookpagetext.BookPageTextService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BookPageTextServiceImpl implements BookPageTextService {

    private static final Logger LOG = LoggerFactory.getLogger(BookPageTextServiceImpl.class);

    private final BookPageTextRepository bookPageTextRepository;
    private final BookPageTextDataAccessRepository bookPageTextDataAccessRepository;

    public BookPageTextServiceImpl(
        BookPageTextRepository bookPageTextRepository,
        BookPageTextDataAccessRepository bookPageTextDataAccessRepository
    ) {
        this.bookPageTextRepository = bookPageTextRepository;
        this.bookPageTextDataAccessRepository = bookPageTextDataAccessRepository;
    }

    private static BookPageText merge(BookPageText existing, BookPageText incoming) {
        String documentId = incoming.documentId() != null ? incoming.documentId() : existing.documentId();
        Integer pageNo = incoming.pageNo() != null ? incoming.pageNo() : existing.pageNo();
        String text = incoming.text() != null ? incoming.text() : existing.text();
        Book book = incoming.book() != null ? incoming.book() : existing.book();
        return new BookPageText(existing.id(), documentId, pageNo, text, book);
    }

    @Override
    public BookPageText save(BookPageText bookPageText) {
        LOG.debug("Domain service: save BookPageText {}", bookPageText);
        return bookPageTextRepository.save(bookPageText);
    }

    @Override
    public BookPageText update(BookPageText bookPageText) {
        LOG.debug("Domain service: update BookPageText {}", bookPageText);
        return bookPageTextRepository.save(bookPageText);
    }

    @Override
    public Optional<BookPageText> partialUpdate(BookPageText bookPageText) {
        LOG.debug("Domain service: partial update BookPageText {}", bookPageText);
        return bookPageTextDataAccessRepository
            .findById(bookPageText.id(), true)
            .map(existing -> merge(existing, bookPageText))
            .map(bookPageTextRepository::save);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Domain service: delete BookPageText {}", id);
        bookPageTextRepository.deleteById(id);
    }
}
