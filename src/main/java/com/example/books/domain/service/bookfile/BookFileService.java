package com.example.books.domain.service.bookfile;

import com.example.books.domain.core.bookfile.BookFile;
import java.util.Optional;

public interface BookFileService {
    BookFile save(BookFile bookFile);

    BookFile update(BookFile bookFile);

    Optional<BookFile> partialUpdate(BookFile bookFile);

    void delete(Long id);
}
