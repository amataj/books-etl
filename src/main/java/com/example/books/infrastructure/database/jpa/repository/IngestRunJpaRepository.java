package com.example.books.infrastructure.database.jpa.repository;

import com.example.books.infrastructure.database.jpa.entity.IngestRunEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the IngestRun entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IngestRunJpaRepository extends JpaRepository<IngestRunEntity, Long> {}
