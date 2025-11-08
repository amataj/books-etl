package com.example.books.infrastructure.database.jpa.adapter;

import com.example.books.domain.core.bookfile.BookFile;
import com.example.books.domain.repository.bookfile.BookFileDataAccessRepository;
import com.example.books.domain.repository.bookfile.BookFileRepository;
import com.example.books.infrastructure.database.jpa.entity.BookEntity;
import com.example.books.infrastructure.database.jpa.entity.BookFileEntity;
import com.example.books.infrastructure.database.jpa.mapper.BookFileEntityMapper;
import com.example.books.infrastructure.database.jpa.repository.BookFileJpaRepository;
import com.example.books.infrastructure.database.jpa.repository.BookJpaRepository;
import com.example.books.infrastructure.database.jpa.util.PageResultFactory;
import com.example.books.infrastructure.database.jpa.util.PageableFactory;
import com.example.books.shared.pagination.PageCriteria;
import com.example.books.shared.pagination.PageResult;
import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import org.springframework.stereotype.Component;

/**
 * Adapter for {@link BookFile} persistence.
 */
@Component
public class BookFileJpaRepositoryAdapter implements BookFileRepository, BookFileDataAccessRepository {

    private final BookFileJpaRepository bookFileJpaRepository;
    private final BookJpaRepository bookJpaRepository;
    private final BookFileEntityMapper mapper;

    public BookFileJpaRepositoryAdapter(
        BookFileJpaRepository bookFileJpaRepository,
        BookJpaRepository bookJpaRepository,
        BookFileEntityMapper mapper
    ) {
        this.bookFileJpaRepository = bookFileJpaRepository;
        this.bookJpaRepository = bookJpaRepository;
        this.mapper = mapper;
    }

    @Override
    public BookFile save(BookFile bookFile) {
        BookFileEntity entity = mapper.toEntity(bookFile);
        entity.setBook(resolveBook(bookFile.book().documentId()));
        BookFileEntity persisted = bookFileJpaRepository.save(entity);
        return mapper.toDomain(persisted);
    }

    @Override
    public void deleteById(Long id) {
        bookFileJpaRepository.deleteById(id);
    }

    @Override
    public Optional<BookFile> findBySha256(String sha256) {
        return bookFileJpaRepository.findOneBySha256(sha256).map(mapper::toDomain);
    }

    @Override
    public Optional<BookFile> findById(Long id, boolean eagerRelationships) {
        return (eagerRelationships ? bookFileJpaRepository.findOneWithToOneRelationships(id) : bookFileJpaRepository.findById(id)).map(
                mapper::toDomain
            );
    }

    @Override
    public PageResult<BookFile> findAll(PageCriteria criteria, boolean eagerRelationships) {
        return PageResultFactory.from(
            (eagerRelationships
                    ? bookFileJpaRepository.findAllWithEagerRelationships(PageableFactory.from(criteria))
                    : bookFileJpaRepository.findAll(PageableFactory.from(criteria))).map(mapper::toDomain)
        );
    }

    private BookEntity resolveBook(String documentId) {
        return bookJpaRepository
            .findOneByDocumentId(documentId)
            .orElseThrow(() -> new EntityNotFoundException("Book not found for documentId " + documentId));
    }
}
