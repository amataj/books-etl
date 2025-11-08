package com.example.books.adapter.web.rest;

import com.example.books.adapter.web.rest.dto.IngestRunDTO;
import com.example.books.adapter.web.rest.errors.BadRequestAlertException;
import com.example.books.adapter.web.rest.mapper.IngestRunRestMapper;
import com.example.books.adapter.web.rest.util.PageCriteriaFactory;
import com.example.books.adapter.web.rest.util.PageResponseFactory;
import com.example.books.usecase.ingestrun.IngestRunUseCase;
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
@RequestMapping("/api/ingest-runs")
public class IngestRunResource {

    private static final Logger LOG = LoggerFactory.getLogger(IngestRunResource.class);
    private static final String ENTITY_NAME = "ingestRun";
    private final IngestRunUseCase ingestRunUseCase;
    private final IngestRunRestMapper mapper;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public IngestRunResource(IngestRunUseCase ingestRunUseCase, IngestRunRestMapper mapper) {
        this.ingestRunUseCase = ingestRunUseCase;
        this.mapper = mapper;
    }

    @PostMapping("")
    public ResponseEntity<IngestRunDTO> createIngestRun(@Valid @RequestBody IngestRunDTO ingestRunDTO) throws URISyntaxException {
        LOG.debug("REST request to save IngestRun : {}", ingestRunDTO);
        if (ingestRunDTO.getId() != null) {
            throw new BadRequestAlertException("A new ingestRun cannot already have an ID", ENTITY_NAME, "idexists");
        }
        var saved = ingestRunUseCase.save(mapper.toDomain(ingestRunDTO));
        IngestRunDTO response = mapper.toDto(saved);
        return ResponseEntity.created(new URI("/api/ingest-runs/" + response.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, response.getId().toString()))
            .body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<IngestRunDTO> updateIngestRun(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody IngestRunDTO ingestRunDTO
    ) {
        LOG.debug("REST request to update IngestRun : {}, {}", id, ingestRunDTO);
        if (ingestRunDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ingestRunDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ingestRunUseCase.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        IngestRunDTO response = mapper.toDto(ingestRunUseCase.update(mapper.toDomain(ingestRunDTO)));
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, response.getId().toString()))
            .body(response);
    }

    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<IngestRunDTO> partialUpdateIngestRun(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody IngestRunDTO ingestRunDTO
    ) {
        LOG.debug("REST request to partial update IngestRun : {}, {}", id, ingestRunDTO);
        if (ingestRunDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ingestRunDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ingestRunUseCase.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<IngestRunDTO> result = ingestRunUseCase.partialUpdate(mapper.toDomain(ingestRunDTO)).map(mapper::toDto);
        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ingestRunDTO.getId().toString())
        );
    }

    @GetMapping("")
    public ResponseEntity<List<IngestRunDTO>> getAllIngestRuns(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of IngestRuns");
        Page<IngestRunDTO> page = PageResponseFactory.toPage(
            ingestRunUseCase.findAll(PageCriteriaFactory.from(pageable)),
            pageable,
            mapper::toDto
        );
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/{id}")
    public ResponseEntity<IngestRunDTO> getIngestRun(@PathVariable("id") Long id) {
        LOG.debug("REST request to get IngestRun : {}", id);
        Optional<IngestRunDTO> ingestRunDTO = ingestRunUseCase.findOne(id).map(mapper::toDto);
        return ResponseUtil.wrapOrNotFound(ingestRunDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIngestRun(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete IngestRun : {}", id);
        ingestRunUseCase.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
