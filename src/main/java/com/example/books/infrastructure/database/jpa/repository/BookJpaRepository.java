package com.example.books.infrastructure.database.jpa.repository;

import com.example.books.infrastructure.database.jpa.entity.BookEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Book entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BookJpaRepository extends JpaRepository<BookEntity, Long> {}
