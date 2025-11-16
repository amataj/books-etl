package com.example.books.infrastructure.database.jpa.adapter;

import com.example.books.domain.book.Book;
import com.example.books.domain.book.BookCommandRepository;
import com.example.books.domain.book.BookQueryRepository;
import com.example.books.infrastructure.database.jpa.mapper.BookMapper;
import com.example.books.infrastructure.database.jpa.repository.BookJpaRepository;
import com.example.books.shared.pagination.PageCriteria;
import com.example.books.shared.pagination.PageResult;
import java.util.Optional;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

/**
 * JPA-backed implementation of the {@link BookQueryRepository} read port.
 */
@Repository
public class BookJpaAdapter implements BookQueryRepository, BookCommandRepository {

    private final BookJpaRepository bookJpaRepository;
    private final BookMapper bookMapper;

    public BookJpaAdapter(BookJpaRepository bookJpaRepository, BookMapper bookMapper) {
        this.bookJpaRepository = bookJpaRepository;
        this.bookMapper = bookMapper;
    }

    @Override
    public Optional<Book> findById(Long id) {
        return bookJpaRepository.findById(id).map(bookMapper::toDto);
    }

    @Override
    public Optional<Book> findByDocumentId(String documentId) {
        return bookJpaRepository.findByDocumentId(documentId).map(bookMapper::toDto);
    }

    @Override
    public Book save(Book book) {
        var entity = bookMapper.toEntity(book);
        var persisted = bookJpaRepository.save(entity);
        return bookMapper.toDto(persisted);
    }

    @Override
    public void deleteById(Long id) {
        bookJpaRepository.deleteById(id);
    }

    @Override
    public PageResult<Book> findAll(PageCriteria criteria) {
        var pageable = PageRequest.of(criteria.page(), criteria.size());
        var page = bookJpaRepository.findAll(pageable);
        var content = page.stream().map(bookMapper::toDto).toList();
        return new PageResult<>(content, page.getTotalElements(), page.getNumber(), page.getSize());
    }

    @Override
    public Optional<Book> findByTitle(String title) {
        return bookJpaRepository.findByTitle(title).map(bookMapper::toDto);
    }
}
