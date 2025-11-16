package com.example.books.etl.infrastructure.database.jpa.repository;

import com.example.books.etl.infrastructure.database.jpa.entity.BookEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
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
