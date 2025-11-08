package com.example.books.repository;

import com.example.books.infrastructure.infrastructure.database.jpa.entity.IngestEvent;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the IngestEvent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IngestEventRepository extends JpaRepository<IngestEvent, Long> {}
