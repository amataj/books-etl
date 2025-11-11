package com.example.books.infrastructure.database.jpa.adapter;

import com.example.books.domain.bookpage.BookPageText;
import com.example.books.domain.bookpage.BookPageTextDataAccessRepository;
import com.example.books.infrastructure.database.jpa.mapper.BookPageTextMapper;
import com.example.books.infrastructure.database.jpa.repository.BookPageTextJpaRepository;
import com.example.books.shared.pagination.PageCriteria;
import com.example.books.shared.pagination.PageResult;
import java.util.Optional;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

/**
 * JPA adapter that fulfills {@link BookPageTextDataAccessRepository}.
 */
@Repository
public class BookPageTextJpaAdapter implements BookPageTextDataAccessRepository {

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
    public PageResult<BookPageText> findAll(PageCriteria criteria, boolean eagerRelationships) {
        var pageable = PageRequest.of(criteria.page(), criteria.size());
        var page = eagerRelationships
            ? bookPageTextJpaRepository.findAllWithEagerRelationships(pageable)
            : bookPageTextJpaRepository.findAll(pageable);
        var content = page.stream().map(bookPageTextMapper::toDto).toList();
        return new PageResult<>(content, page.getTotalElements(), page.getNumber(), page.getSize());
    }
}
