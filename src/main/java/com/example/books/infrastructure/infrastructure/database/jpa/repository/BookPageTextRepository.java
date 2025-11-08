package com.example.books.infrastructure.infrastructure.database.jpa.repository;

import com.example.books.infrastructure.infrastructure.database.jpa.entity.BookPageTextEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the BookPageText entity.
 */
@Repository
public interface BookPageTextRepository extends JpaRepository<BookPageTextEntity, Long> {
    default Optional<BookPageTextEntity> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<BookPageTextEntity> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<BookPageTextEntity> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select bookPageText from BookPageTextEntity bookPageText left join fetch bookPageText.book",
        countQuery = "select count(bookPageText) from BookPageTextEntity bookPageText"
    )
    Page<BookPageTextEntity> findAllWithToOneRelationships(Pageable pageable);

    @Query("select bookPageText from BookPageTextEntity bookPageText left join fetch bookPageText.book")
    List<BookPageTextEntity> findAllWithToOneRelationships();

    @Query("select bookPageText from BookPageTextEntity bookPageText left join fetch bookPageText.book where bookPageText.id =:id")
    Optional<BookPageTextEntity> findOneWithToOneRelationships(@Param("id") Long id);
}
