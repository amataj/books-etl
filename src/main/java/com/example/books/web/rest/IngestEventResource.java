package com.example.books.web.rest;

import com.example.books.repository.IngestEventRepository;
import com.example.books.service.IngestEventService;
import com.example.books.service.dto.IngestEventDTO;
import com.example.books.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.example.books.domain.IngestEvent}.
 */
@RestController
@RequestMapping("/api/ingest-events")
public class IngestEventResource {

    private static final Logger LOG = LoggerFactory.getLogger(IngestEventResource.class);

    private static final String ENTITY_NAME = "ingestEvent";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IngestEventService ingestEventService;

    private final IngestEventRepository ingestEventRepository;

    public IngestEventResource(IngestEventService ingestEventService, IngestEventRepository ingestEventRepository) {
        this.ingestEventService = ingestEventService;
        this.ingestEventRepository = ingestEventRepository;
    }

    /**
     * {@code POST  /ingest-events} : Create a new ingestEvent.
     *
     * @param ingestEventDTO the ingestEventDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ingestEventDTO, or with status {@code 400 (Bad Request)} if the ingestEvent has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<IngestEventDTO> createIngestEvent(@Valid @RequestBody IngestEventDTO ingestEventDTO) throws URISyntaxException {
        LOG.debug("REST request to save IngestEvent : {}", ingestEventDTO);
        if (ingestEventDTO.getId() != null) {
            throw new BadRequestAlertException("A new ingestEvent cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ingestEventDTO = ingestEventService.save(ingestEventDTO);
        return ResponseEntity.created(new URI("/api/ingest-events/" + ingestEventDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, ingestEventDTO.getId().toString()))
            .body(ingestEventDTO);
    }

    /**
     * {@code PUT  /ingest-events/:id} : Updates an existing ingestEvent.
     *
     * @param id the id of the ingestEventDTO to save.
     * @param ingestEventDTO the ingestEventDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ingestEventDTO,
     * or with status {@code 400 (Bad Request)} if the ingestEventDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ingestEventDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<IngestEventDTO> updateIngestEvent(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody IngestEventDTO ingestEventDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update IngestEvent : {}, {}", id, ingestEventDTO);
        if (ingestEventDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ingestEventDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ingestEventRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ingestEventDTO = ingestEventService.update(ingestEventDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ingestEventDTO.getId().toString()))
            .body(ingestEventDTO);
    }

    /**
     * {@code PATCH  /ingest-events/:id} : Partial updates given fields of an existing ingestEvent, field will ignore if it is null
     *
     * @param id the id of the ingestEventDTO to save.
     * @param ingestEventDTO the ingestEventDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ingestEventDTO,
     * or with status {@code 400 (Bad Request)} if the ingestEventDTO is not valid,
     * or with status {@code 404 (Not Found)} if the ingestEventDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the ingestEventDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<IngestEventDTO> partialUpdateIngestEvent(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody IngestEventDTO ingestEventDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update IngestEvent partially : {}, {}", id, ingestEventDTO);
        if (ingestEventDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ingestEventDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ingestEventRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<IngestEventDTO> result = ingestEventService.partialUpdate(ingestEventDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ingestEventDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /ingest-events} : get all the ingestEvents.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ingestEvents in body.
     */
    @GetMapping("")
    public List<IngestEventDTO> getAllIngestEvents() {
        LOG.debug("REST request to get all IngestEvents");
        return ingestEventService.findAll();
    }

    /**
     * {@code GET  /ingest-events/:id} : get the "id" ingestEvent.
     *
     * @param id the id of the ingestEventDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ingestEventDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<IngestEventDTO> getIngestEvent(@PathVariable("id") Long id) {
        LOG.debug("REST request to get IngestEvent : {}", id);
        Optional<IngestEventDTO> ingestEventDTO = ingestEventService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ingestEventDTO);
    }

    /**
     * {@code DELETE  /ingest-events/:id} : delete the "id" ingestEvent.
     *
     * @param id the id of the ingestEventDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIngestEvent(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete IngestEvent : {}", id);
        ingestEventService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
