package com.example.books.etl.infrastructure.database.jpa.adapter;

import com.example.books.etl.domain.bookfile.BookFile;
import com.example.books.etl.domain.bookfile.BookFileCommandRepository;
import com.example.books.etl.domain.bookfile.BookFileQueryRepository;
import com.example.books.etl.infrastructure.database.jpa.mapper.BookFileMapper;
import com.example.books.etl.infrastructure.database.jpa.repository.BookFileJpaRepository;
import com.example.books.etl.shared.pagination.PageCriteria;
import com.example.books.etl.shared.pagination.PageResult;
import java.util.Optional;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

/**
 * JPA-backed implementation of {@link BookFileQueryRepository}.
 */
@Repository
public class BookFileJpaAdapter implements BookFileQueryRepository, BookFileCommandRepository {

    private final BookFileJpaRepository bookFileJpaRepository;
    private final BookFileMapper bookFileMapper;

    public BookFileJpaAdapter(BookFileJpaRepository bookFileJpaRepository, BookFileMapper bookFileMapper) {
        this.bookFileJpaRepository = bookFileJpaRepository;
        this.bookFileMapper = bookFileMapper;
    }

    @Override
    public Optional<BookFile> findById(Long id) {
        return findById(id, false);
    }

    @Override
    public Optional<BookFile> findById(Long id, boolean eagerRelationships) {
        var entity = eagerRelationships ? bookFileJpaRepository.findOneWithEagerRelationships(id) : bookFileJpaRepository.findById(id);
        return entity.map(bookFileMapper::toDto);
    }

    @Override
    public BookFile save(BookFile file) {
        var entity = bookFileMapper.toEntity(file);
        var persisted = bookFileJpaRepository.save(entity);
        return bookFileMapper.toDto(persisted);
    }

    @Override
    public void deleteById(Long id) {
        bookFileJpaRepository.deleteById(id);
    }

    @Override
    public Optional<BookFile> findBySha256(String sha256) {
        return bookFileJpaRepository.findOneBySha256(sha256).map(bookFileMapper::toDto);
    }

    @Override
    public PageResult<BookFile> findAll(PageCriteria criteria, boolean eagerRelationships) {
        var pageable = PageRequest.of(criteria.page(), criteria.size());
        var page = eagerRelationships
            ? bookFileJpaRepository.findAllWithEagerRelationships(pageable)
            : bookFileJpaRepository.findAll(pageable);
        var content = page.stream().map(bookFileMapper::toDto).toList();
        return new PageResult<>(content, page.getTotalElements(), page.getNumber(), page.getSize());
    }
}
