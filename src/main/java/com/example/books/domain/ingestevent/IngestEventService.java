package com.example.books.domain.ingestevent;

import com.example.books.domain.ingestrun.IngestEvent;
import com.example.books.infrastructure.database.jpa.entity.IngestEventEntity;
import com.example.books.infrastructure.database.jpa.mapper.IngestEventMapper;
import com.example.books.infrastructure.database.jpa.repository.IngestEventJpaRepository;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link IngestEventEntity}.
 */
@Service
@Transactional
public class IngestEventService {

    private static final Logger LOG = LoggerFactory.getLogger(IngestEventService.class);

    private final IngestEventJpaRepository ingestEventRepository;

    private final IngestEventMapper ingestEventMapper;

    public IngestEventService(IngestEventJpaRepository ingestEventRepository, IngestEventMapper ingestEventMapper) {
        this.ingestEventRepository = ingestEventRepository;
        this.ingestEventMapper = ingestEventMapper;
    }

    /**
     * Save a ingestEvent.
     *
     * @param ingestEventDTO the entity to save.
     * @return the persisted entity.
     */
    public IngestEvent save(IngestEvent ingestEventDTO) {
        LOG.debug("Request to save IngestEvent : {}", ingestEventDTO);
        IngestEventEntity ingestEvent = ingestEventMapper.toEntity(ingestEventDTO);
        ingestEvent = ingestEventRepository.save(ingestEvent);
        return ingestEventMapper.toDto(ingestEvent);
    }

    /**
     * Update a ingestEvent.
     *
     * @param ingestEventDTO the entity to save.
     * @return the persisted entity.
     */
    public IngestEvent update(IngestEvent ingestEventDTO) {
        LOG.debug("Request to update IngestEvent : {}", ingestEventDTO);
        IngestEventEntity ingestEvent = ingestEventMapper.toEntity(ingestEventDTO);
        ingestEvent = ingestEventRepository.save(ingestEvent);
        return ingestEventMapper.toDto(ingestEvent);
    }

    /**
     * Partially update a ingestEvent.
     *
     * @param ingestEventDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<IngestEvent> partialUpdate(IngestEvent ingestEventDTO) {
        LOG.debug("Request to partially update IngestEvent : {}", ingestEventDTO);

        return ingestEventRepository
            .findById(ingestEventDTO.getId())
            .map(existingIngestEvent -> {
                ingestEventMapper.partialUpdate(existingIngestEvent, ingestEventDTO);

                return existingIngestEvent;
            })
            .map(ingestEventRepository::save)
            .map(ingestEventMapper::toDto);
    }

    /**
     * Get all the ingestEvents.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<IngestEvent> findAll() {
        LOG.debug("Request to get all IngestEvents");
        return ingestEventRepository.findAll().stream().map(ingestEventMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one ingestEvent by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<IngestEvent> findOne(Long id) {
        LOG.debug("Request to get IngestEvent : {}", id);
        return ingestEventRepository.findById(id).map(ingestEventMapper::toDto);
    }

    /**
     * Delete the ingestEvent by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete IngestEvent : {}", id);
        ingestEventRepository.deleteById(id);
    }
}
