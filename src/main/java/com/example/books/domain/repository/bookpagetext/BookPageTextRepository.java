package com.example.books.domain.repository.bookpagetext;

import com.example.books.domain.core.bookpagetext.BookPageText;

/**
 * Port for mutating BookPageText aggregates.
 */
public interface BookPageTextRepository {
    BookPageText save(BookPageText bookPageText);

    void deleteById(Long id);
}
