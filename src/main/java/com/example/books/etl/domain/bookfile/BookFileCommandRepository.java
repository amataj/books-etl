package com.example.books.etl.domain.bookfile;

import java.util.Optional;

/**
 * Command-side port for persisting book files.
 */
public interface BookFileCommandRepository {
    BookFile save(BookFile file);

    void deleteById(Long id);

    Optional<BookFile> findBySha256(String sha256);
}
