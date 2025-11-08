package com.example.books.adapter.web.rest;

import com.example.books.adapter.web.rest.dto.IngestEventDTO;
import com.example.books.adapter.web.rest.errors.BadRequestAlertException;
import com.example.books.adapter.web.rest.mapper.IngestEventRestMapper;
import com.example.books.adapter.web.rest.util.PageCriteriaFactory;
import com.example.books.adapter.web.rest.util.PageResponseFactory;
import com.example.books.usecase.ingestevent.IngestEventUseCase;
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
@RequestMapping("/api/ingest-events")
public class IngestEventResource {

    private static final Logger LOG = LoggerFactory.getLogger(IngestEventResource.class);
    private static final String ENTITY_NAME = "ingestEvent";
    private final IngestEventUseCase ingestEventUseCase;
    private final IngestEventRestMapper mapper;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public IngestEventResource(IngestEventUseCase ingestEventUseCase, IngestEventRestMapper mapper) {
        this.ingestEventUseCase = ingestEventUseCase;
        this.mapper = mapper;
    }

    @PostMapping("")
    public ResponseEntity<IngestEventDTO> createIngestEvent(@Valid @RequestBody IngestEventDTO ingestEventDTO) throws URISyntaxException {
        LOG.debug("REST request to save IngestEvent : {}", ingestEventDTO);
        if (ingestEventDTO.getId() != null) {
            throw new BadRequestAlertException("A new ingestEvent cannot already have an ID", ENTITY_NAME, "idexists");
        }
        var saved = ingestEventUseCase.save(mapper.toDomain(ingestEventDTO));
        IngestEventDTO response = mapper.toDto(saved);
        return ResponseEntity.created(new URI("/api/ingest-events/" + response.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, response.getId().toString()))
            .body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<IngestEventDTO> updateIngestEvent(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody IngestEventDTO ingestEventDTO
    ) {
        LOG.debug("REST request to update IngestEvent : {}, {}", id, ingestEventDTO);
        if (ingestEventDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ingestEventDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ingestEventUseCase.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        IngestEventDTO response = mapper.toDto(ingestEventUseCase.update(mapper.toDomain(ingestEventDTO)));
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, response.getId().toString()))
            .body(response);
    }

    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<IngestEventDTO> partialUpdateIngestEvent(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody IngestEventDTO ingestEventDTO
    ) {
        LOG.debug("REST request to partial update IngestEvent : {}, {}", id, ingestEventDTO);
        if (ingestEventDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ingestEventDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ingestEventUseCase.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<IngestEventDTO> result = ingestEventUseCase.partialUpdate(mapper.toDomain(ingestEventDTO)).map(mapper::toDto);
        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ingestEventDTO.getId().toString())
        );
    }

    @GetMapping("")
    public ResponseEntity<List<IngestEventDTO>> getAllIngestEvents(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of IngestEvents");
        Page<IngestEventDTO> page = PageResponseFactory.toPage(
            ingestEventUseCase.findAll(PageCriteriaFactory.from(pageable)),
            pageable,
            mapper::toDto
        );
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/{id}")
    public ResponseEntity<IngestEventDTO> getIngestEvent(@PathVariable("id") Long id) {
        LOG.debug("REST request to get IngestEvent : {}", id);
        Optional<IngestEventDTO> ingestEventDTO = ingestEventUseCase.findOne(id).map(mapper::toDto);
        return ResponseUtil.wrapOrNotFound(ingestEventDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIngestEvent(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete IngestEvent : {}", id);
        ingestEventUseCase.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
