package com.example.books.adapter.web.rest;

import com.example.books.adapter.web.rest.dto.BookPageTextDTO;
import com.example.books.adapter.web.rest.errors.BadRequestAlertException;
import com.example.books.adapter.web.rest.mapper.BookPageTextRestMapper;
import com.example.books.adapter.web.rest.util.PageCriteriaFactory;
import com.example.books.adapter.web.rest.util.PageResponseFactory;
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

@RestController
@RequestMapping("/api/book-page-texts")
public class BookPageTextResource {

    private static final Logger LOG = LoggerFactory.getLogger(BookPageTextResource.class);
    private static final String ENTITY_NAME = "bookPageText";
    private final BookPageTextUseCase bookPageTextUseCase;
    private final BookPageTextRestMapper mapper;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public BookPageTextResource(BookPageTextUseCase bookPageTextUseCase, BookPageTextRestMapper mapper) {
        this.bookPageTextUseCase = bookPageTextUseCase;
        this.mapper = mapper;
    }

    @PostMapping("")
    public ResponseEntity<BookPageTextDTO> createBookPageText(@Valid @RequestBody BookPageTextDTO bookPageTextDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save BookPageText : {}", bookPageTextDTO);
        if (bookPageTextDTO.getId() != null) {
            throw new BadRequestAlertException("A new bookPageText cannot already have an ID", ENTITY_NAME, "idexists");
        }
        var saved = bookPageTextUseCase.save(mapper.toDomain(bookPageTextDTO));
        BookPageTextDTO response = mapper.toDto(saved);
        return ResponseEntity.created(new URI("/api/book-page-texts/" + response.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, response.getId().toString()))
            .body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookPageTextDTO> updateBookPageText(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody BookPageTextDTO bookPageTextDTO
    ) {
        LOG.debug("REST request to update BookPageText : {}, {}", id, bookPageTextDTO);
        if (bookPageTextDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bookPageTextDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bookPageTextUseCase.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BookPageTextDTO response = mapper.toDto(bookPageTextUseCase.update(mapper.toDomain(bookPageTextDTO)));
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, response.getId().toString()))
            .body(response);
    }

    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BookPageTextDTO> partialUpdateBookPageText(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody BookPageTextDTO bookPageTextDTO
    ) {
        LOG.debug("REST request to partial update BookPageText : {}, {}", id, bookPageTextDTO);
        if (bookPageTextDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bookPageTextDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bookPageTextUseCase.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BookPageTextDTO> result = bookPageTextUseCase.partialUpdate(mapper.toDomain(bookPageTextDTO)).map(mapper::toDto);
        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, bookPageTextDTO.getId().toString())
        );
    }

    @GetMapping("")
    public ResponseEntity<List<BookPageTextDTO>> getAllBookPageTexts(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of BookPageTexts");
        Page<BookPageTextDTO> page = PageResponseFactory.toPage(
            bookPageTextUseCase.findAll(PageCriteriaFactory.from(pageable), eagerload),
            pageable,
            mapper::toDto
        );
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookPageTextDTO> getBookPageText(@PathVariable("id") Long id) {
        LOG.debug("REST request to get BookPageText : {}", id);
        Optional<BookPageTextDTO> dto = bookPageTextUseCase.findOne(id, true).map(mapper::toDto);
        return ResponseUtil.wrapOrNotFound(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBookPageText(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete BookPageText : {}", id);
        bookPageTextUseCase.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
