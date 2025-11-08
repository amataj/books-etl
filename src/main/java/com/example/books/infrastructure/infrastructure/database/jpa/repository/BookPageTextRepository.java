package com.example.books.infrastructure.infrastructure.database.jpa.repository;

import com.example.books.infrastructure.infrastructure.database.jpa.entity.BookPageText;
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
public interface BookPageTextRepository extends JpaRepository<BookPageText, Long> {
    default Optional<BookPageText> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<BookPageText> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<BookPageText> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select bookPageText from BookPageText bookPageText left join fetch bookPageText.book",
        countQuery = "select count(bookPageText) from BookPageText bookPageText"
    )
    Page<BookPageText> findAllWithToOneRelationships(Pageable pageable);

    @Query("select bookPageText from BookPageText bookPageText left join fetch bookPageText.book")
    List<BookPageText> findAllWithToOneRelationships();

    @Query("select bookPageText from BookPageText bookPageText left join fetch bookPageText.book where bookPageText.id =:id")
    Optional<BookPageText> findOneWithToOneRelationships(@Param("id") Long id);
}
