package com.example.books.adapter.web.rest;

import com.example.books.adapter.web.rest.errors.BadRequestAlertException;
import com.example.books.infrastructure.infrastructure.database.jpa.entity.BookFileEntity;
import com.example.books.infrastructure.infrastructure.database.jpa.repository.BookFileRepository;
import com.example.books.service.BookFileService;
import com.example.books.service.dto.BookFileDTO;
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
 * REST controller for managing {@link BookFileEntity}.
 */
@RestController
@RequestMapping("/api/book-files")
public class BookFileResource {

    private static final Logger LOG = LoggerFactory.getLogger(BookFileResource.class);

    private static final String ENTITY_NAME = "bookFile";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BookFileService bookFileService;

    private final BookFileRepository bookFileRepository;

    public BookFileResource(BookFileService bookFileService, BookFileRepository bookFileRepository) {
        this.bookFileService = bookFileService;
        this.bookFileRepository = bookFileRepository;
    }

    /**
     * {@code POST  /book-files} : Create a new bookFile.
     *
     * @param bookFileDTO the bookFileDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bookFileDTO, or with status {@code 400 (Bad Request)} if the bookFile has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<BookFileDTO> createBookFile(@Valid @RequestBody BookFileDTO bookFileDTO) throws URISyntaxException {
        LOG.debug("REST request to save BookFile : {}", bookFileDTO);
        if (bookFileDTO.getId() != null) {
            throw new BadRequestAlertException("A new bookFile cannot already have an ID", ENTITY_NAME, "idexists");
        }
        bookFileDTO = bookFileService.save(bookFileDTO);
        return ResponseEntity.created(new URI("/api/book-files/" + bookFileDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, bookFileDTO.getId().toString()))
            .body(bookFileDTO);
    }

    /**
     * {@code PUT  /book-files/:id} : Updates an existing bookFile.
     *
     * @param id the id of the bookFileDTO to save.
     * @param bookFileDTO the bookFileDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bookFileDTO,
     * or with status {@code 400 (Bad Request)} if the bookFileDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bookFileDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<BookFileDTO> updateBookFile(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody BookFileDTO bookFileDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update BookFile : {}, {}", id, bookFileDTO);
        if (bookFileDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bookFileDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bookFileRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        bookFileDTO = bookFileService.update(bookFileDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, bookFileDTO.getId().toString()))
            .body(bookFileDTO);
    }

    /**
     * {@code PATCH  /book-files/:id} : Partial updates given fields of an existing bookFile, field will ignore if it is null
     *
     * @param id the id of the bookFileDTO to save.
     * @param bookFileDTO the bookFileDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bookFileDTO,
     * or with status {@code 400 (Bad Request)} if the bookFileDTO is not valid,
     * or with status {@code 404 (Not Found)} if the bookFileDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the bookFileDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BookFileDTO> partialUpdateBookFile(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody BookFileDTO bookFileDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update BookFile partially : {}, {}", id, bookFileDTO);
        if (bookFileDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bookFileDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bookFileRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BookFileDTO> result = bookFileService.partialUpdate(bookFileDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, bookFileDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /book-files} : get all the bookFiles.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bookFiles in body.
     */
    @GetMapping("")
    public ResponseEntity<List<BookFileDTO>> getAllBookFiles(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of BookFiles");
        Page<BookFileDTO> page;
        if (eagerload) {
            page = bookFileService.findAllWithEagerRelationships(pageable);
        } else {
            page = bookFileService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /book-files/:id} : get the "id" bookFile.
     *
     * @param id the id of the bookFileDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bookFileDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<BookFileDTO> getBookFile(@PathVariable("id") Long id) {
        LOG.debug("REST request to get BookFile : {}", id);
        Optional<BookFileDTO> bookFileDTO = bookFileService.findOne(id);
        return ResponseUtil.wrapOrNotFound(bookFileDTO);
    }

    /**
     * {@code DELETE  /book-files/:id} : delete the "id" bookFile.
     *
     * @param id the id of the bookFileDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBookFile(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete BookFile : {}", id);
        bookFileService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
