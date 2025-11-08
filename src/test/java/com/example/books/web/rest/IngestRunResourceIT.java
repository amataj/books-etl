package com.example.books.web.rest;

import static com.example.books.domain.IngestRunAsserts.*;
import static com.example.books.web.rest.TestUtil.createUpdateProxyForBean;
import static com.example.books.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.books.IntegrationTest;
import com.example.books.adapter.web.rest.dto.IngestRunDTO;
import com.example.books.adapter.web.rest.mapper.IngestRunRestMapper;
import com.example.books.domain.core.ingestrun.IngestRun;
import com.example.books.infrastructure.database.jpa.entity.IngestRunEntity;
import com.example.books.infrastructure.database.jpa.mapper.IngestRunEntityMapper;
import com.example.books.infrastructure.database.jpa.repository.IngestRunJpaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link IngestRunResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class IngestRunResourceIT {

    private static final ZonedDateTime DEFAULT_STARTED_AT = ZonedDateTime.now(ZoneOffset.UTC).withNano(0);
    private static final ZonedDateTime UPDATED_STARTED_AT = DEFAULT_STARTED_AT.plusHours(1);

    private static final ZonedDateTime DEFAULT_FINISHED_AT = DEFAULT_STARTED_AT.plusHours(2);
    private static final ZonedDateTime UPDATED_FINISHED_AT = DEFAULT_FINISHED_AT.plusHours(2);

    private static final String DEFAULT_STATUS = "RUNNING";
    private static final String UPDATED_STATUS = "COMPLETED";

    private static final Integer DEFAULT_FILES_SEEN = 10;
    private static final Integer UPDATED_FILES_SEEN = 20;

    private static final Integer DEFAULT_FILES_PARSED = 5;
    private static final Integer UPDATED_FILES_PARSED = 15;

    private static final Integer DEFAULT_FILES_FAILED = 1;
    private static final Integer UPDATED_FILES_FAILED = 3;

    private static final String ENTITY_API_URL = "/api/ingest-runs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private IngestRunJpaRepository ingestRunJpaRepository;

    @Autowired
    private IngestRunEntityMapper ingestRunEntityMapper;

    @Autowired
    private IngestRunRestMapper ingestRunRestMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restIngestRunMockMvc;

    private IngestRunEntity ingestRun;

    private IngestRunEntity insertedIngestRun;

    public static IngestRunEntity createEntity(EntityManager em) {
        return new IngestRunEntity()
            .startedAt(DEFAULT_STARTED_AT)
            .finishedAt(DEFAULT_FINISHED_AT)
            .status(DEFAULT_STATUS)
            .filesSeen(DEFAULT_FILES_SEEN)
            .filesParsed(DEFAULT_FILES_PARSED)
            .filesFailed(DEFAULT_FILES_FAILED);
    }

    public static IngestRunEntity createUpdatedEntity(EntityManager em) {
        return new IngestRunEntity()
            .startedAt(UPDATED_STARTED_AT)
            .finishedAt(UPDATED_FINISHED_AT)
            .status(UPDATED_STATUS)
            .filesSeen(UPDATED_FILES_SEEN)
            .filesParsed(UPDATED_FILES_PARSED)
            .filesFailed(UPDATED_FILES_FAILED);
    }

    @BeforeEach
    void initTest() {
        ingestRun = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedIngestRun != null) {
            ingestRunJpaRepository.delete(insertedIngestRun);
            insertedIngestRun = null;
        }
    }

    @Test
    @Transactional
    void createIngestRun() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        IngestRunDTO ingestRunDTO = toDto(ingestRun);
        IngestRunDTO returnedIngestRunDTO = om.readValue(
            restIngestRunMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ingestRunDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            IngestRunDTO.class
        );

        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        IngestRun returnedIngestRun = ingestRunRestMapper.toDomain(returnedIngestRunDTO);
        assertIngestRunUpdatableFieldsEquals(returnedIngestRun, getPersistedIngestRun(returnedIngestRun.id()));
        insertedIngestRun = ingestRunJpaRepository.findById(returnedIngestRun.id()).orElseThrow();
    }

    @Test
    @Transactional
    void createIngestRunWithExistingId() throws Exception {
        ingestRun.setId(1L);
        IngestRunDTO ingestRunDTO = toDto(ingestRun);

        long databaseSizeBeforeCreate = getRepositoryCount();

        restIngestRunMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ingestRunDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        ingestRun.setStatus(null);
        IngestRunDTO ingestRunDTO = toDto(ingestRun);

        restIngestRunMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ingestRunDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllIngestRuns() throws Exception {
        insertedIngestRun = ingestRunJpaRepository.saveAndFlush(ingestRun);

        restIngestRunMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ingestRun.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].filesSeen").value(hasItem(DEFAULT_FILES_SEEN)))
            .andExpect(jsonPath("$.[*].filesParsed").value(hasItem(DEFAULT_FILES_PARSED)))
            .andExpect(jsonPath("$.[*].filesFailed").value(hasItem(DEFAULT_FILES_FAILED)));
    }

    @Test
    @Transactional
    void getIngestRun() throws Exception {
        insertedIngestRun = ingestRunJpaRepository.saveAndFlush(ingestRun);

        restIngestRunMockMvc
            .perform(get(ENTITY_API_URL_ID, ingestRun.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ingestRun.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.filesSeen").value(DEFAULT_FILES_SEEN))
            .andExpect(jsonPath("$.filesParsed").value(DEFAULT_FILES_PARSED))
            .andExpect(jsonPath("$.filesFailed").value(DEFAULT_FILES_FAILED));
    }

    @Test
    @Transactional
    void getNonExistingIngestRun() throws Exception {
        restIngestRunMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingIngestRun() throws Exception {
        insertedIngestRun = ingestRunJpaRepository.saveAndFlush(ingestRun);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        IngestRunEntity updatedIngestRun = ingestRunJpaRepository.findById(ingestRun.getId()).orElseThrow();
        em.detach(updatedIngestRun);
        updatedIngestRun
            .startedAt(UPDATED_STARTED_AT)
            .finishedAt(UPDATED_FINISHED_AT)
            .status(UPDATED_STATUS)
            .filesSeen(UPDATED_FILES_SEEN)
            .filesParsed(UPDATED_FILES_PARSED)
            .filesFailed(UPDATED_FILES_FAILED);
        IngestRunDTO ingestRunDTO = toDto(updatedIngestRun);

        restIngestRunMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ingestRunDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(ingestRunDTO))
            )
            .andExpect(status().isOk());

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedIngestRunToMatchAllProperties(updatedIngestRun);
    }

    @Test
    @Transactional
    void putNonExistingIngestRun() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ingestRun.setId(longCount.incrementAndGet());
        IngestRunDTO ingestRunDTO = toDto(ingestRun);

        restIngestRunMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ingestRunDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(ingestRunDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchIngestRun() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ingestRun.setId(longCount.incrementAndGet());
        IngestRunDTO ingestRunDTO = toDto(ingestRun);

        restIngestRunMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(ingestRunDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamIngestRun() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ingestRun.setId(longCount.incrementAndGet());
        IngestRunDTO ingestRunDTO = toDto(ingestRun);

        restIngestRunMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ingestRunDTO)))
            .andExpect(status().isMethodNotAllowed());

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateIngestRunWithPatch() throws Exception {
        insertedIngestRun = ingestRunJpaRepository.saveAndFlush(ingestRun);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        IngestRunEntity partialUpdatedIngestRun = new IngestRunEntity();
        partialUpdatedIngestRun.setId(ingestRun.getId());
        partialUpdatedIngestRun.filesFailed(UPDATED_FILES_FAILED);

        restIngestRunMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIngestRun.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedIngestRun))
            )
            .andExpect(status().isOk());

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertIngestRunUpdatableFieldsEquals(
            toDomain(createUpdateProxyForBean(partialUpdatedIngestRun, ingestRun)),
            getPersistedIngestRun(ingestRun.getId())
        );
    }

    @Test
    @Transactional
    void fullUpdateIngestRunWithPatch() throws Exception {
        insertedIngestRun = ingestRunJpaRepository.saveAndFlush(ingestRun);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        IngestRunEntity partialUpdatedIngestRun = new IngestRunEntity();
        partialUpdatedIngestRun.setId(ingestRun.getId());
        partialUpdatedIngestRun
            .startedAt(UPDATED_STARTED_AT)
            .finishedAt(UPDATED_FINISHED_AT)
            .status(UPDATED_STATUS)
            .filesSeen(UPDATED_FILES_SEEN)
            .filesParsed(UPDATED_FILES_PARSED)
            .filesFailed(UPDATED_FILES_FAILED);

        restIngestRunMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIngestRun.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedIngestRun))
            )
            .andExpect(status().isOk());

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertIngestRunUpdatableFieldsEquals(toDomain(partialUpdatedIngestRun), getPersistedIngestRun(partialUpdatedIngestRun.getId()));
    }

    @Test
    @Transactional
    void patchNonExistingIngestRun() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ingestRun.setId(longCount.incrementAndGet());
        IngestRunDTO ingestRunDTO = toDto(ingestRun);

        restIngestRunMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ingestRunDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(ingestRunDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchIngestRun() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ingestRun.setId(longCount.incrementAndGet());
        IngestRunDTO ingestRunDTO = toDto(ingestRun);

        restIngestRunMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(ingestRunDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamIngestRun() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ingestRun.setId(longCount.incrementAndGet());
        IngestRunDTO ingestRunDTO = toDto(ingestRun);

        restIngestRunMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(ingestRunDTO)))
            .andExpect(status().isMethodNotAllowed());

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteIngestRun() throws Exception {
        insertedIngestRun = ingestRunJpaRepository.saveAndFlush(ingestRun);

        long databaseSizeBeforeDelete = getRepositoryCount();

        restIngestRunMockMvc
            .perform(delete(ENTITY_API_URL_ID, ingestRun.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return ingestRunJpaRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(getRepositoryCount()).isEqualTo(countBefore + 1);
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(getRepositoryCount()).isEqualTo(countBefore - 1);
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(getRepositoryCount()).isEqualTo(countBefore);
    }

    protected IngestRun getPersistedIngestRun(Long id) {
        return ingestRunEntityMapper.toDomain(ingestRunJpaRepository.findById(id).orElseThrow());
    }

    protected void assertPersistedIngestRunToMatchAllProperties(IngestRunEntity expectedIngestRun) {
        assertIngestRunAllPropertiesEquals(toDomain(expectedIngestRun), getPersistedIngestRun(expectedIngestRun.getId()));
    }

    protected void assertPersistedIngestRunToMatchUpdatableProperties(IngestRunEntity expectedIngestRun) {
        assertIngestRunAllUpdatablePropertiesEquals(toDomain(expectedIngestRun), getPersistedIngestRun(expectedIngestRun.getId()));
    }

    private IngestRunDTO toDto(IngestRunEntity entity) {
        return ingestRunRestMapper.toDto(ingestRunEntityMapper.toDomain(entity));
    }

    private IngestRun toDomain(IngestRunEntity entity) {
        return ingestRunEntityMapper.toDomain(entity);
    }
}
