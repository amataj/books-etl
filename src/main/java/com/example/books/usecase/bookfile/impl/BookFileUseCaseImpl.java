package com.example.books.usecase.bookfile.impl;

import com.example.books.domain.bookfile.BookFile;
import com.example.books.domain.bookfile.BookFileQueryRepository;
import com.example.books.domain.bookfile.BookFileService;
import com.example.books.usecase.bookfile.BookFileUseCase;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BookFileUseCaseImpl implements BookFileUseCase {

    private final BookFileService bookFileService;
    private final BookFileQueryRepository bookFileQueryRepository;

    public BookFileUseCaseImpl(BookFileService bookFileService, BookFileQueryRepository bookFileQueryRepository) {
        this.bookFileService = bookFileService;
        this.bookFileQueryRepository = bookFileQueryRepository;
    }

    @Override
    public BookFile create(BookFile bookFile) {
        return bookFileService.save(bookFile);
    }

    @Override
    public BookFile update(BookFile bookFile) {
        return bookFileService.update(bookFile);
    }

    @Override
    public Optional<BookFile> partialUpdate(BookFile bookFile) {
        return bookFileService.partialUpdate(bookFile);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BookFile> findAll(Pageable pageable) {
        return bookFileService.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BookFile> findAllWithEagerRelationships(Pageable pageable) {
        return bookFileService.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BookFile> findOne(Long id) {
        return bookFileQueryRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        bookFileService.delete(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean exists(Long id) {
        return bookFileQueryRepository.findById(id).isPresent();
    }
}
