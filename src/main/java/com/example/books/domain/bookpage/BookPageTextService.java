package com.example.books.domain.bookpage;

import com.example.books.shared.pagination.PageCriteria;
import com.example.books.shared.pagination.PageResult;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

/**
 * Domain service for managing {@link BookPageText} aggregates.
 */
public class BookPageTextService {

    private static final Logger LOG = LoggerFactory.getLogger(BookPageTextService.class);

    private final BookPageTextCommandRepository bookPageTextCommandRepository;
    private final BookPageTextQueryRepository bookPageTextQueryRepository;

    public BookPageTextService(
        BookPageTextCommandRepository bookPageTextCommandRepository,
        BookPageTextQueryRepository bookPageTextQueryRepository
    ) {
        this.bookPageTextCommandRepository = bookPageTextCommandRepository;
        this.bookPageTextQueryRepository = bookPageTextQueryRepository;
    }

    /**
     * Save a bookPageText.
     *
     * @param bookPageText the aggregate to save.
     * @return the persisted aggregate.
     */
    public BookPageText save(BookPageText bookPageText) {
        LOG.debug("Request to save BookPageText : {}", bookPageText);
        return bookPageTextCommandRepository.save(bookPageText);
    }

    /**
     * Update a bookPageText.
     *
     * @param bookPageText the aggregate to update.
     * @return the persisted aggregate.
     */
    public BookPageText update(BookPageText bookPageText) {
        LOG.debug("Request to update BookPageText : {}", bookPageText);
        return bookPageTextCommandRepository.save(bookPageText);
    }

    /**
     * Partially update a bookPageText.
     *
     * @param bookPageText the aggregate to update partially.
     * @return the persisted aggregate.
     */
    public Optional<BookPageText> partialUpdate(BookPageText bookPageText) {
        LOG.debug("Request to partially update BookPageText : {}", bookPageText);
        if (bookPageText.getId() == null) {
            return Optional.empty();
        }

        return bookPageTextQueryRepository
            .findById(bookPageText.getId(), true)
            .map(existing -> {
                partialUpdate(existing, bookPageText);
                return bookPageTextCommandRepository.save(existing);
            });
    }

    /**
     * Delete the bookPageText by id.
     *
     * @param id the id of the aggregate.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete BookPageText : {}", id);
        bookPageTextCommandRepository.deleteById(id);
    }

    private void partialUpdate(BookPageText existing, BookPageText incoming) {
        if (incoming == null) {
            return;
        }

        if (incoming.getId() != null) {
            existing.setId(incoming.getId());
        }
        if (incoming.getDocumentId() != null) {
            existing.setDocumentId(incoming.getDocumentId());
        }
        if (incoming.getPageNo() != null) {
            existing.setPageNo(incoming.getPageNo());
        }
        if (incoming.getText() != null) {
            existing.setText(incoming.getText());
        }
        if (incoming.getBook() != null) {
            existing.setBook(incoming.getBook());
        }
    }
}
