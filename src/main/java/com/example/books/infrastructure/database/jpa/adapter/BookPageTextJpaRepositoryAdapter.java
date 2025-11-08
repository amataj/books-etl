package com.example.books.infrastructure.database.jpa.adapter;

import com.example.books.domain.core.bookpagetext.BookPageText;
import com.example.books.domain.repository.bookpagetext.BookPageTextDataAccessRepository;
import com.example.books.domain.repository.bookpagetext.BookPageTextRepository;
import com.example.books.infrastructure.database.jpa.entity.BookEntity;
import com.example.books.infrastructure.database.jpa.entity.BookPageTextEntity;
import com.example.books.infrastructure.database.jpa.mapper.BookPageTextEntityMapper;
import com.example.books.infrastructure.database.jpa.repository.BookJpaRepository;
import com.example.books.infrastructure.database.jpa.repository.BookPageTextJpaRepository;
import com.example.books.infrastructure.database.jpa.util.PageResultFactory;
import com.example.books.infrastructure.database.jpa.util.PageableFactory;
import com.example.books.shared.pagination.PageCriteria;
import com.example.books.shared.pagination.PageResult;
import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class BookPageTextJpaRepositoryAdapter implements BookPageTextRepository, BookPageTextDataAccessRepository {

    private final BookPageTextJpaRepository bookPageTextJpaRepository;
    private final BookJpaRepository bookJpaRepository;
    private final BookPageTextEntityMapper mapper;

    public BookPageTextJpaRepositoryAdapter(
        BookPageTextJpaRepository bookPageTextJpaRepository,
        BookJpaRepository bookJpaRepository,
        BookPageTextEntityMapper mapper
    ) {
        this.bookPageTextJpaRepository = bookPageTextJpaRepository;
        this.bookJpaRepository = bookJpaRepository;
        this.mapper = mapper;
    }

    @Override
    public BookPageText save(BookPageText bookPageText) {
        BookPageTextEntity entity = mapper.toEntity(bookPageText);
        entity.setBook(resolveBook(bookPageText.book().documentId()));
        return mapper.toDomain(bookPageTextJpaRepository.save(entity));
    }

    @Override
    public void deleteById(Long id) {
        bookPageTextJpaRepository.deleteById(id);
    }

    @Override
    public Optional<BookPageText> findById(Long id, boolean eagerRelationships) {
        return (
            eagerRelationships ? bookPageTextJpaRepository.findOneWithToOneRelationships(id) : bookPageTextJpaRepository.findById(id)
        ).map(mapper::toDomain);
    }

    @Override
    public PageResult<BookPageText> findAll(PageCriteria criteria, boolean eagerRelationships) {
        return PageResultFactory.from(
            (eagerRelationships
                    ? bookPageTextJpaRepository.findAllWithEagerRelationships(PageableFactory.from(criteria))
                    : bookPageTextJpaRepository.findAll(PageableFactory.from(criteria))).map(mapper::toDomain)
        );
    }

    private BookEntity resolveBook(String documentId) {
        return bookJpaRepository
            .findOneByDocumentId(documentId)
            .orElseThrow(() -> new EntityNotFoundException("Book not found for documentId " + documentId));
    }
}
