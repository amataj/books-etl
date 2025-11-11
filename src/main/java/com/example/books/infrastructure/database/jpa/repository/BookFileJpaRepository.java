package com.example.books.infrastructure.database.jpa.repository;

import com.example.books.infrastructure.database.jpa.entity.BookFileEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the BookFile entity.
 */
@Repository
public interface BookFileJpaRepository extends JpaRepository<BookFileEntity, Long> {
    Optional<BookFileEntity> findOneBySha256(String sha256);

    default Optional<BookFileEntity> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<BookFileEntity> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<BookFileEntity> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select bookFile from BookFileEntity bookFile left join fetch bookFile.book",
        countQuery = "select count(bookFile) from BookFileEntity bookFile"
    )
    Page<BookFileEntity> findAllWithToOneRelationships(Pageable pageable);

    @Query("select bookFile from BookFileEntity bookFile left join fetch bookFile.book")
    List<BookFileEntity> findAllWithToOneRelationships();

    @Query("select bookFile from BookFileEntity bookFile left join fetch bookFile.book where bookFile.id =:id")
    Optional<BookFileEntity> findOneWithToOneRelationships(@Param("id") Long id);
}
