package com.example.books.infrastructure.database.jpa.adapter;

import com.example.books.domain.core.book.Book;
import com.example.books.domain.repository.book.BookDataAccessRepository;
import com.example.books.domain.repository.book.BookRepository;
import com.example.books.infrastructure.database.jpa.entity.BookEntity;
import com.example.books.infrastructure.database.jpa.mapper.BookEntityMapper;
import com.example.books.infrastructure.database.jpa.repository.BookJpaRepository;
import com.example.books.infrastructure.database.jpa.util.PageResultFactory;
import com.example.books.infrastructure.database.jpa.util.PageableFactory;
import com.example.books.shared.pagination.PageCriteria;
import com.example.books.shared.pagination.PageResult;
import java.util.Optional;
import org.springframework.stereotype.Component;

/**
 * JPA adapter implementing the Book repository ports.
 */
@Component
public class BookJpaRepositoryAdapter implements BookRepository, BookDataAccessRepository {

    private final BookJpaRepository bookJpaRepository;
    private final BookEntityMapper mapper;

    public BookJpaRepositoryAdapter(BookJpaRepository bookJpaRepository, BookEntityMapper mapper) {
        this.bookJpaRepository = bookJpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Book save(Book book) {
        BookEntity entity = mapper.toEntity(book);
        BookEntity persisted = bookJpaRepository.save(entity);
        return mapper.toDomain(persisted);
    }

    @Override
    public void deleteById(Long id) {
        bookJpaRepository.deleteById(id);
    }

    @Override
    public Optional<Book> findByDocumentId(String documentId) {
        return bookJpaRepository.findOneByDocumentId(documentId).map(mapper::toDomain);
    }

    @Override
    public Optional<Book> findById(Long id) {
        return bookJpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public PageResult<Book> findAll(PageCriteria criteria) {
        return PageResultFactory.from(bookJpaRepository.findAll(PageableFactory.from(criteria)).map(mapper::toDomain));
    }
}
