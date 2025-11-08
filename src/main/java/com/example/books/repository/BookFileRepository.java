package com.example.books.repository;

import com.example.books.infrastructure.infrastructure.database.jpa.entity.BookFile;
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
public interface BookFileRepository extends JpaRepository<BookFile, Long> {
    default Optional<BookFile> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<BookFile> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<BookFile> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select bookFile from BookFile bookFile left join fetch bookFile.book",
        countQuery = "select count(bookFile) from BookFile bookFile"
    )
    Page<BookFile> findAllWithToOneRelationships(Pageable pageable);

    @Query("select bookFile from BookFile bookFile left join fetch bookFile.book")
    List<BookFile> findAllWithToOneRelationships();

    @Query("select bookFile from BookFile bookFile left join fetch bookFile.book where bookFile.id =:id")
    Optional<BookFile> findOneWithToOneRelationships(@Param("id") Long id);
}
