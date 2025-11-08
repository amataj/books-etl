package com.example.books.domain.repository;

import com.example.books.domain.core.BookPageText;

/**
 * Port for mutating BookPageText aggregates.
 */
public interface BookPageTextRepository {
    BookPageText save(BookPageText bookPageText);

    void deleteById(Long id);
}
