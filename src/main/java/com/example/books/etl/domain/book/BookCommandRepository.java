package com.example.books.etl.domain.book;

/**
 * Port for mutating Book aggregates.
 */
public interface BookCommandRepository {
    Book save(Book book);

    void deleteById(Long id);
}
