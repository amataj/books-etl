package com.example.books.adapter.web.rest;

import com.example.books.adapter.web.rest.dto.BookFileDTO;
import com.example.books.adapter.web.rest.errors.BadRequestAlertException;
import com.example.books.adapter.web.rest.mapper.BookFileRestMapper;
import com.example.books.adapter.web.rest.util.PageCriteriaFactory;
import com.example.books.adapter.web.rest.util.PageResponseFactory;
import com.example.books.usecase.bookfile.BookFileUseCase;
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
@RequestMapping("/api/book-files")
public class BookFileResource {

    private static final Logger LOG = LoggerFactory.getLogger(BookFileResource.class);
    private static final String ENTITY_NAME = "bookFile";
    private final BookFileUseCase bookFileUseCase;
    private final BookFileRestMapper mapper;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public BookFileResource(BookFileUseCase bookFileUseCase, BookFileRestMapper mapper) {
        this.bookFileUseCase = bookFileUseCase;
        this.mapper = mapper;
    }

    @PostMapping("")
    public ResponseEntity<BookFileDTO> createBookFile(@Valid @RequestBody BookFileDTO bookFileDTO) throws URISyntaxException {
        LOG.debug("REST request to save BookFile : {}", bookFileDTO);
        if (bookFileDTO.getId() != null) {
            throw new BadRequestAlertException("A new bookFile cannot already have an ID", ENTITY_NAME, "idexists");
        }
        var saved = bookFileUseCase.save(mapper.toDomain(bookFileDTO));
        BookFileDTO response = mapper.toDto(saved);
        return ResponseEntity.created(new URI("/api/book-files/" + response.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, response.getId().toString()))
            .body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookFileDTO> updateBookFile(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody BookFileDTO bookFileDTO
    ) {
        LOG.debug("REST request to update BookFile : {}, {}", id, bookFileDTO);
        if (bookFileDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bookFileDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bookFileUseCase.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BookFileDTO response = mapper.toDto(bookFileUseCase.update(mapper.toDomain(bookFileDTO)));
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, response.getId().toString()))
            .body(response);
    }

    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BookFileDTO> partialUpdateBookFile(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody BookFileDTO bookFileDTO
    ) {
        LOG.debug("REST request to partial update BookFile : {}, {}", id, bookFileDTO);
        if (bookFileDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bookFileDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bookFileUseCase.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BookFileDTO> result = bookFileUseCase.partialUpdate(mapper.toDomain(bookFileDTO)).map(mapper::toDto);
        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, bookFileDTO.getId().toString())
        );
    }

    @GetMapping("")
    public ResponseEntity<List<BookFileDTO>> getAllBookFiles(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of BookFiles");
        Page<BookFileDTO> page = PageResponseFactory.toPage(
            bookFileUseCase.findAll(PageCriteriaFactory.from(pageable), eagerload),
            pageable,
            mapper::toDto
        );
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookFileDTO> getBookFile(@PathVariable("id") Long id) {
        LOG.debug("REST request to get BookFile : {}", id);
        Optional<BookFileDTO> bookFileDTO = bookFileUseCase.findOne(id, true).map(mapper::toDto);
        return ResponseUtil.wrapOrNotFound(bookFileDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBookFile(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete BookFile : {}", id);
        bookFileUseCase.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
