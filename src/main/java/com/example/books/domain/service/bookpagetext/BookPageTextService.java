package com.example.books.domain.service.bookpagetext;

import com.example.books.domain.core.bookpagetext.BookPageText;
import java.util.Optional;

public interface BookPageTextService {
    BookPageText save(BookPageText bookPageText);

    BookPageText update(BookPageText bookPageText);

    Optional<BookPageText> partialUpdate(BookPageText bookPageText);

    void delete(Long id);
}
