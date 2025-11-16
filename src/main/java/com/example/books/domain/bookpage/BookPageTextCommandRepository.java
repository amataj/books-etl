package com.example.books.domain.bookpage;

/**
 * Command-side port for mutating BookPageText aggregates.
 */
public interface BookPageTextCommandRepository {
    BookPageText save(BookPageText bookPageText);

    void deleteById(Long id);
}
