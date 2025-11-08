package com.example.books.infrastructure.database.jpa.repository;

import com.example.books.infrastructure.database.jpa.entity.IngestEventEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the IngestEvent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IngestEventJpaRepository extends JpaRepository<IngestEventEntity, Long> {}
