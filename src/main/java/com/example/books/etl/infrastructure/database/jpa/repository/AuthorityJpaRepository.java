package com.example.books.etl.infrastructure.database.jpa.repository;

import com.example.books.etl.infrastructure.database.jpa.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Authority entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AuthorityJpaRepository extends JpaRepository<Authority, String> {}
