package com.example.books.etl.infrastructure.database.jpa.adapter;

import com.example.books.etl.domain.bookpage.BookPageText;
import com.example.books.etl.domain.bookpage.BookPageTextCommandRepository;
import com.example.books.etl.domain.bookpage.BookPageTextQueryRepository;
import com.example.books.etl.infrastructure.database.jpa.mapper.BookPageTextMapper;
import com.example.books.etl.infrastructure.database.jpa.repository.BookPageTextJpaRepository;
import com.example.books.etl.shared.pagination.PageCriteria;
import com.example.books.etl.shared.pagination.PageResult;
import java.util.Optional;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

/**
 * JPA adapter that fulfills {@link BookPageTextQueryRepository}.
 */
@Repository
public class BookPageTextJpaAdapter implements BookPageTextQueryRepository, BookPageTextCommandRepository {

    private final BookPageTextJpaRepository bookPageTextJpaRepository;
    private final BookPageTextMapper bookPageTextMapper;

    public BookPageTextJpaAdapter(BookPageTextJpaRepository bookPageTextJpaRepository, BookPageTextMapper bookPageTextMapper) {
        this.bookPageTextJpaRepository = bookPageTextJpaRepository;
        this.bookPageTextMapper = bookPageTextMapper;
    }

    @Override
    public Optional<BookPageText> findById(Long id) {
        return findById(id, false);
    }

    @Override
    public Optional<BookPageText> findById(Long id, boolean eagerRelationships) {
        var entity = eagerRelationships
            ? bookPageTextJpaRepository.findOneWithEagerRelationships(id)
            : bookPageTextJpaRepository.findById(id);
        return entity.map(bookPageTextMapper::toDto);
    }

    @Override
    public BookPageText save(BookPageText bookPageText) {
        var entity = bookPageTextMapper.toEntity(bookPageText);
        var persisted = bookPageTextJpaRepository.save(entity);
        return bookPageTextMapper.toDto(persisted);
    }

    @Override
    public void deleteById(Long id) {
        bookPageTextJpaRepository.deleteById(id);
    }

    @Override
    public PageResult<BookPageText> findAll(PageCriteria criteria, boolean eagerRelationships) {
        var pageable = PageRequest.of(criteria.page(), criteria.size());
        var page = eagerRelationships
            ? bookPageTextJpaRepository.findAllWithEagerRelationships(pageable)
            : bookPageTextJpaRepository.findAll(pageable);
        var content = page.stream().map(bookPageTextMapper::toDto).toList();
        return new PageResult<>(content, page.getTotalElements(), page.getNumber(), page.getSize());
    }
}
