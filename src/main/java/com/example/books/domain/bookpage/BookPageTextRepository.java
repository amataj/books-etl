package com.example.books.domain.bookpage;

/**
 * Port for mutating BookPageText aggregates.
 */
public interface BookPageTextRepository {
    BookPageText save(BookPageText bookPageText);

    void deleteById(Long id);
}
