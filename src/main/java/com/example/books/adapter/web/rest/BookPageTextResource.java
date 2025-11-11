package com.example.books.adapter.web.rest;

import com.example.books.adapter.web.rest.errors.BadRequestAlertException;
import com.example.books.domain.bookpage.BookPageText;
import com.example.books.domain.bookpage.BookPageTextService;
import com.example.books.infrastructure.database.jpa.entity.BookPageTextEntity;
import com.example.books.infrastructure.database.jpa.repository.BookPageTextJpaRepository;
import com.example.books.usecase.bookpagetext.BookPageTextUseCase;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link BookPageTextEntity}.
 */
@RestController
@RequestMapping("/api/book-page-texts")
public class BookPageTextResource {

    private static final Logger LOG = LoggerFactory.getLogger(BookPageTextResource.class);

    private static final String ENTITY_NAME = "bookPageText";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BookPageTextUseCase bookPageTextService;

    private final BookPageTextJpaRepository bookPageTextRepository;

    public BookPageTextResource(BookPageTextUseCase bookPageTextService, BookPageTextJpaRepository bookPageTextRepository) {
        this.bookPageTextService = bookPageTextService;
        this.bookPageTextRepository = bookPageTextRepository;
    }

    /**
     * {@code POST  /book-page-texts} : Create a new bookPageText.
     *
     * @param bookPageTextDTO the bookPageTextDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bookPageTextDTO, or with status {@code 400 (Bad Request)} if the bookPageText has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<BookPageText> createBookPageText(@Valid @RequestBody BookPageText bookPageTextDTO) throws URISyntaxException {
        LOG.debug("REST request to save BookPageText : {}", bookPageTextDTO);
        if (bookPageTextDTO.getId() != null) {
            throw new BadRequestAlertException("A new bookPageText cannot already have an ID", ENTITY_NAME, "idexists");
        }
        bookPageTextDTO = bookPageTextService.save(bookPageTextDTO);
        return ResponseEntity.created(new URI("/api/book-page-texts/" + bookPageTextDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, bookPageTextDTO.getId().toString()))
            .body(bookPageTextDTO);
    }

    /**
     * {@code PUT  /book-page-texts/:id} : Updates an existing bookPageText.
     *
     * @param id the id of the bookPageTextDTO to save.
     * @param bookPageTextDTO the bookPageTextDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bookPageTextDTO,
     * or with status {@code 400 (Bad Request)} if the bookPageTextDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bookPageTextDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<BookPageText> updateBookPageText(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody BookPageText bookPageTextDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update BookPageText : {}, {}", id, bookPageTextDTO);
        if (bookPageTextDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bookPageTextDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bookPageTextRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        bookPageTextDTO = bookPageTextService.update(bookPageTextDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, bookPageTextDTO.getId().toString()))
            .body(bookPageTextDTO);
    }

    /**
     * {@code PATCH  /book-page-texts/:id} : Partial updates given fields of an existing bookPageText, field will ignore if it is null
     *
     * @param id the id of the bookPageTextDTO to save.
     * @param bookPageTextDTO the bookPageTextDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bookPageTextDTO,
     * or with status {@code 400 (Bad Request)} if the bookPageTextDTO is not valid,
     * or with status {@code 404 (Not Found)} if the bookPageTextDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the bookPageTextDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BookPageText> partialUpdateBookPageText(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody BookPageText bookPageTextDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update BookPageText partially : {}, {}", id, bookPageTextDTO);
        if (bookPageTextDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bookPageTextDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bookPageTextRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BookPageText> result = bookPageTextService.partialUpdate(bookPageTextDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, bookPageTextDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /book-page-texts} : get all the bookPageTexts.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bookPageTexts in body.
     */
    @GetMapping("")
    public ResponseEntity<List<BookPageText>> getAllBookPageTexts(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of BookPageTexts");
        Page<BookPageText> page;
        if (eagerload) {
            page = bookPageTextService.findAllWithEagerRelationships(pageable);
        } else {
            page = bookPageTextService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /book-page-texts/:id} : get the "id" bookPageText.
     *
     * @param id the id of the bookPageTextDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bookPageTextDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<BookPageText> getBookPageText(@PathVariable("id") Long id) {
        LOG.debug("REST request to get BookPageText : {}", id);
        Optional<BookPageText> bookPageTextDTO = bookPageTextService.findOne(id);
        return ResponseUtil.wrapOrNotFound(bookPageTextDTO);
    }

    /**
     * {@code DELETE  /book-page-texts/:id} : delete the "id" bookPageText.
     *
     * @param id the id of the bookPageTextDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBookPageText(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete BookPageText : {}", id);
        bookPageTextService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
