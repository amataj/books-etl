package com.example.books.infrastructure.database.jpa.repository;

import com.example.books.infrastructure.database.jpa.entity.BookEntity;
import java.nio.channels.FileChannel;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Book entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BookJpaRepository extends JpaRepository<BookEntity, Long> {
    Optional<BookEntity> findByDocumentId(String documentId);

    Optional<BookEntity> findByTitle(String title);
}
