package com.example.books.repository;

import com.example.books.domain.IngestRun;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the IngestRun entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IngestRunRepository extends JpaRepository<IngestRun, Long> {}
