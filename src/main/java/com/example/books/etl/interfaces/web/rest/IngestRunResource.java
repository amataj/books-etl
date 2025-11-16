package com.example.books.etl.interfaces.web.rest;

import com.example.books.etl.application.ingestrun.IngestRunUseCase;
import com.example.books.etl.domain.ingestrun.IngestRun;
import com.example.books.etl.infrastructure.database.jpa.entity.IngestRunEntity;
import com.example.books.etl.infrastructure.database.jpa.repository.IngestRunJpaRepository;
import com.example.books.etl.interfaces.web.rest.errors.BadRequestAlertException;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link IngestRunEntity}.
 */
@RestController
@RequestMapping("/api/ingest-runs")
public class IngestRunResource {

    private static final Logger LOG = LoggerFactory.getLogger(IngestRunResource.class);

    private static final String ENTITY_NAME = "ingestRun";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IngestRunUseCase ingestRunService;

    private final IngestRunJpaRepository ingestRunRepository;

    public IngestRunResource(IngestRunUseCase ingestRunService, IngestRunJpaRepository ingestRunRepository) {
        this.ingestRunService = ingestRunService;
        this.ingestRunRepository = ingestRunRepository;
    }

    /**
     * {@code POST  /ingest-runs} : Create a new ingestRun.
     *
     * @param ingestRunDTO the ingestRunDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ingestRunDTO, or with status {@code 400 (Bad Request)} if the ingestRun has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<IngestRun> createIngestRun(@Valid @RequestBody IngestRun ingestRunDTO) throws URISyntaxException {
        LOG.debug("REST request to save IngestRun : {}", ingestRunDTO);
        if (ingestRunDTO.getId() != null) {
            throw new BadRequestAlertException("A new ingestRun cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ingestRunDTO = ingestRunService.create(ingestRunDTO);
        return ResponseEntity.created(new URI("/api/ingest-runs/" + ingestRunDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, ingestRunDTO.getId().toString()))
            .body(ingestRunDTO);
    }

    /**
     * {@code PUT  /ingest-runs/:id} : Updates an existing ingestRun.
     *
     * @param id the id of the ingestRunDTO to save.
     * @param ingestRunDTO the ingestRunDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ingestRunDTO,
     * or with status {@code 400 (Bad Request)} if the ingestRunDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ingestRunDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<IngestRun> updateIngestRun(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody IngestRun ingestRunDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update IngestRun : {}, {}", id, ingestRunDTO);
        if (ingestRunDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ingestRunDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ingestRunRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ingestRunDTO = ingestRunService.update(ingestRunDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ingestRunDTO.getId().toString()))
            .body(ingestRunDTO);
    }

    /**
     * {@code PATCH  /ingest-runs/:id} : Partial updates given fields of an existing ingestRun, field will ignore if it is null
     *
     * @param id the id of the ingestRunDTO to save.
     * @param ingestRunDTO the ingestRunDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ingestRunDTO,
     * or with status {@code 400 (Bad Request)} if the ingestRunDTO is not valid,
     * or with status {@code 404 (Not Found)} if the ingestRunDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the ingestRunDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<IngestRun> partialUpdateIngestRun(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody IngestRun ingestRunDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update IngestRun partially : {}, {}", id, ingestRunDTO);
        if (ingestRunDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ingestRunDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ingestRunRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<IngestRun> result = ingestRunService.partialUpdate(ingestRunDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ingestRunDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /ingest-runs} : get all the ingestRuns.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ingestRuns in body.
     */
    @GetMapping("")
    public List<IngestRun> getAllIngestRuns() {
        LOG.debug("REST request to get all IngestRuns");
        return ingestRunService.findAll();
    }

    /**
     * {@code GET  /ingest-runs/:id} : get the "id" ingestRun.
     *
     * @param id the id of the ingestRunDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ingestRunDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<IngestRun> getIngestRun(@PathVariable("id") Long id) {
        LOG.debug("REST request to get IngestRun : {}", id);
        Optional<IngestRun> ingestRunDTO = ingestRunService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ingestRunDTO);
    }

    /**
     * {@code DELETE  /ingest-runs/:id} : delete the "id" ingestRun.
     *
     * @param id the id of the ingestRunDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIngestRun(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete IngestRun : {}", id);
        ingestRunService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
