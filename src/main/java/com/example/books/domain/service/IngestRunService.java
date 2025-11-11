package com.example.books.domain.service;

import com.example.books.domain.core.IngestRun;
import com.example.books.infrastructure.database.jpa.entity.IngestRunEntity;
import com.example.books.infrastructure.database.jpa.mapper.IngestRunMapper;
import com.example.books.infrastructure.database.jpa.repository.IngestRunJpaRepository;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link IngestRunEntity}.
 */
@Service
@Transactional
public class IngestRunService {

    private static final Logger LOG = LoggerFactory.getLogger(IngestRunService.class);

    private final IngestRunJpaRepository ingestRunRepository;

    private final IngestRunMapper ingestRunMapper;

    public IngestRunService(IngestRunJpaRepository ingestRunRepository, IngestRunMapper ingestRunMapper) {
        this.ingestRunRepository = ingestRunRepository;
        this.ingestRunMapper = ingestRunMapper;
    }

    /**
     * Save a ingestRun.
     *
     * @param ingestRunDTO the entity to save.
     * @return the persisted entity.
     */
    public IngestRun save(IngestRun ingestRunDTO) {
        LOG.debug("Request to save IngestRun : {}", ingestRunDTO);
        IngestRunEntity ingestRun = ingestRunMapper.toEntity(ingestRunDTO);
        ingestRun = ingestRunRepository.save(ingestRun);
        return ingestRunMapper.toDto(ingestRun);
    }

    /**
     * Update a ingestRun.
     *
     * @param ingestRunDTO the entity to save.
     * @return the persisted entity.
     */
    public IngestRun update(IngestRun ingestRunDTO) {
        LOG.debug("Request to update IngestRun : {}", ingestRunDTO);
        IngestRunEntity ingestRun = ingestRunMapper.toEntity(ingestRunDTO);
        ingestRun = ingestRunRepository.save(ingestRun);
        return ingestRunMapper.toDto(ingestRun);
    }

    /**
     * Partially update a ingestRun.
     *
     * @param ingestRunDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<IngestRun> partialUpdate(IngestRun ingestRunDTO) {
        LOG.debug("Request to partially update IngestRun : {}", ingestRunDTO);

        return ingestRunRepository
            .findById(ingestRunDTO.getId())
            .map(existingIngestRun -> {
                ingestRunMapper.partialUpdate(existingIngestRun, ingestRunDTO);

                return existingIngestRun;
            })
            .map(ingestRunRepository::save)
            .map(ingestRunMapper::toDto);
    }

    /**
     * Get all the ingestRuns.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<IngestRun> findAll() {
        LOG.debug("Request to get all IngestRuns");
        return ingestRunRepository.findAll().stream().map(ingestRunMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one ingestRun by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<IngestRun> findOne(Long id) {
        LOG.debug("Request to get IngestRun : {}", id);
        return ingestRunRepository.findById(id).map(ingestRunMapper::toDto);
    }

    @Transactional(readOnly = true)
    public boolean exists(Long id) {
        return ingestRunRepository.existsById(id);
    }

    /**
     * Delete the ingestRun by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete IngestRun : {}", id);
        ingestRunRepository.deleteById(id);
    }
}
