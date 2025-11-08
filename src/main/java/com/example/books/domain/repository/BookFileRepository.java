package com.example.books.domain.repository;

import com.example.books.domain.core.BookFile;
import java.util.Optional;

/**
 * Port for persisting book files.
 */
public interface BookFileRepository {
    BookFile save(BookFile file);

    void deleteById(Long id);

    Optional<BookFile> findBySha256(String sha256);
}
