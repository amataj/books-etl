package com.example.books.service;

import com.example.books.infrastructure.infrastructure.database.jpa.entity.IngestRun;
import com.example.books.repository.IngestRunRepository;
import com.example.books.service.dto.IngestRunDTO;
import com.example.books.service.mapper.IngestRunMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link IngestRun}.
 */
@Service
@Transactional
public class IngestRunService {

    private static final Logger LOG = LoggerFactory.getLogger(IngestRunService.class);

    private final IngestRunRepository ingestRunRepository;

    private final IngestRunMapper ingestRunMapper;

    public IngestRunService(IngestRunRepository ingestRunRepository, IngestRunMapper ingestRunMapper) {
        this.ingestRunRepository = ingestRunRepository;
        this.ingestRunMapper = ingestRunMapper;
    }

    /**
     * Save a ingestRun.
     *
     * @param ingestRunDTO the entity to save.
     * @return the persisted entity.
     */
    public IngestRunDTO save(IngestRunDTO ingestRunDTO) {
        LOG.debug("Request to save IngestRun : {}", ingestRunDTO);
        IngestRun ingestRun = ingestRunMapper.toEntity(ingestRunDTO);
        ingestRun = ingestRunRepository.save(ingestRun);
        return ingestRunMapper.toDto(ingestRun);
    }

    /**
     * Update a ingestRun.
     *
     * @param ingestRunDTO the entity to save.
     * @return the persisted entity.
     */
    public IngestRunDTO update(IngestRunDTO ingestRunDTO) {
        LOG.debug("Request to update IngestRun : {}", ingestRunDTO);
        IngestRun ingestRun = ingestRunMapper.toEntity(ingestRunDTO);
        ingestRun = ingestRunRepository.save(ingestRun);
        return ingestRunMapper.toDto(ingestRun);
    }

    /**
     * Partially update a ingestRun.
     *
     * @param ingestRunDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<IngestRunDTO> partialUpdate(IngestRunDTO ingestRunDTO) {
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
    public List<IngestRunDTO> findAll() {
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
    public Optional<IngestRunDTO> findOne(Long id) {
        LOG.debug("Request to get IngestRun : {}", id);
        return ingestRunRepository.findById(id).map(ingestRunMapper::toDto);
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
