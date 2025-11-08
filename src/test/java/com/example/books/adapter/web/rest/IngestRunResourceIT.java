package com.example.books.adapter.web.rest;

import static com.example.books.adapter.web.rest.TestUtil.createUpdateProxyForBean;
import static com.example.books.adapter.web.rest.TestUtil.sameInstant;
import static com.example.books.infrastructure.infrastructure.database.jpa.entity.IngestRunAsserts.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.books.IntegrationTest;
import com.example.books.infrastructure.infrastructure.database.jpa.entity.IngestRun;
import com.example.books.infrastructure.infrastructure.database.jpa.repository.IngestRunRepository;
import com.example.books.service.dto.IngestRunDTO;
import com.example.books.service.mapper.IngestRunMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.ZoneId;
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

    private static final ZonedDateTime DEFAULT_STARTED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_STARTED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_FINISHED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_FINISHED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final Integer DEFAULT_FILES_SEEN = 0;
    private static final Integer UPDATED_FILES_SEEN = 1;

    private static final Integer DEFAULT_FILES_PARSED = 0;
    private static final Integer UPDATED_FILES_PARSED = 1;

    private static final Integer DEFAULT_FILES_FAILED = 0;
    private static final Integer UPDATED_FILES_FAILED = 1;

    private static final String ENTITY_API_URL = "/api/ingest-runs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private IngestRunRepository ingestRunRepository;

    @Autowired
    private IngestRunMapper ingestRunMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restIngestRunMockMvc;

    private IngestRun ingestRun;

    private IngestRun insertedIngestRun;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IngestRun createEntity() {
        return new IngestRun()
            .startedAt(DEFAULT_STARTED_AT)
            .finishedAt(DEFAULT_FINISHED_AT)
            .status(DEFAULT_STATUS)
            .filesSeen(DEFAULT_FILES_SEEN)
            .filesParsed(DEFAULT_FILES_PARSED)
            .filesFailed(DEFAULT_FILES_FAILED);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IngestRun createUpdatedEntity() {
        return new IngestRun()
            .startedAt(UPDATED_STARTED_AT)
            .finishedAt(UPDATED_FINISHED_AT)
            .status(UPDATED_STATUS)
            .filesSeen(UPDATED_FILES_SEEN)
            .filesParsed(UPDATED_FILES_PARSED)
            .filesFailed(UPDATED_FILES_FAILED);
    }

    @BeforeEach
    void initTest() {
        ingestRun = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedIngestRun != null) {
            ingestRunRepository.delete(insertedIngestRun);
            insertedIngestRun = null;
        }
    }

    @Test
    @Transactional
    void createIngestRun() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the IngestRun
        IngestRunDTO ingestRunDTO = ingestRunMapper.toDto(ingestRun);
        var returnedIngestRunDTO = om.readValue(
            restIngestRunMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ingestRunDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            IngestRunDTO.class
        );

        // Validate the IngestRun in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedIngestRun = ingestRunMapper.toEntity(returnedIngestRunDTO);
        assertIngestRunUpdatableFieldsEquals(returnedIngestRun, getPersistedIngestRun(returnedIngestRun));

        insertedIngestRun = returnedIngestRun;
    }

    @Test
    @Transactional
    void createIngestRunWithExistingId() throws Exception {
        // Create the IngestRun with an existing ID
        ingestRun.setId(1L);
        IngestRunDTO ingestRunDTO = ingestRunMapper.toDto(ingestRun);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restIngestRunMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ingestRunDTO)))
            .andExpect(status().isBadRequest());

        // Validate the IngestRun in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        ingestRun.setStatus(null);

        // Create the IngestRun, which fails.
        IngestRunDTO ingestRunDTO = ingestRunMapper.toDto(ingestRun);

        restIngestRunMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ingestRunDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllIngestRuns() throws Exception {
        // Initialize the database
        insertedIngestRun = ingestRunRepository.saveAndFlush(ingestRun);

        // Get all the ingestRunList
        restIngestRunMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ingestRun.getId().intValue())))
            .andExpect(jsonPath("$.[*].startedAt").value(hasItem(sameInstant(DEFAULT_STARTED_AT))))
            .andExpect(jsonPath("$.[*].finishedAt").value(hasItem(sameInstant(DEFAULT_FINISHED_AT))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].filesSeen").value(hasItem(DEFAULT_FILES_SEEN)))
            .andExpect(jsonPath("$.[*].filesParsed").value(hasItem(DEFAULT_FILES_PARSED)))
            .andExpect(jsonPath("$.[*].filesFailed").value(hasItem(DEFAULT_FILES_FAILED)));
    }

    @Test
    @Transactional
    void getIngestRun() throws Exception {
        // Initialize the database
        insertedIngestRun = ingestRunRepository.saveAndFlush(ingestRun);

        // Get the ingestRun
        restIngestRunMockMvc
            .perform(get(ENTITY_API_URL_ID, ingestRun.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ingestRun.getId().intValue()))
            .andExpect(jsonPath("$.startedAt").value(sameInstant(DEFAULT_STARTED_AT)))
            .andExpect(jsonPath("$.finishedAt").value(sameInstant(DEFAULT_FINISHED_AT)))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.filesSeen").value(DEFAULT_FILES_SEEN))
            .andExpect(jsonPath("$.filesParsed").value(DEFAULT_FILES_PARSED))
            .andExpect(jsonPath("$.filesFailed").value(DEFAULT_FILES_FAILED));
    }

    @Test
    @Transactional
    void getNonExistingIngestRun() throws Exception {
        // Get the ingestRun
        restIngestRunMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingIngestRun() throws Exception {
        // Initialize the database
        insertedIngestRun = ingestRunRepository.saveAndFlush(ingestRun);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the ingestRun
        IngestRun updatedIngestRun = ingestRunRepository.findById(ingestRun.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedIngestRun are not directly saved in db
        em.detach(updatedIngestRun);
        updatedIngestRun
            .startedAt(UPDATED_STARTED_AT)
            .finishedAt(UPDATED_FINISHED_AT)
            .status(UPDATED_STATUS)
            .filesSeen(UPDATED_FILES_SEEN)
            .filesParsed(UPDATED_FILES_PARSED)
            .filesFailed(UPDATED_FILES_FAILED);
        IngestRunDTO ingestRunDTO = ingestRunMapper.toDto(updatedIngestRun);

        restIngestRunMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ingestRunDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(ingestRunDTO))
            )
            .andExpect(status().isOk());

        // Validate the IngestRun in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedIngestRunToMatchAllProperties(updatedIngestRun);
    }

    @Test
    @Transactional
    void putNonExistingIngestRun() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ingestRun.setId(longCount.incrementAndGet());

        // Create the IngestRun
        IngestRunDTO ingestRunDTO = ingestRunMapper.toDto(ingestRun);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIngestRunMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ingestRunDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(ingestRunDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IngestRun in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchIngestRun() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ingestRun.setId(longCount.incrementAndGet());

        // Create the IngestRun
        IngestRunDTO ingestRunDTO = ingestRunMapper.toDto(ingestRun);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIngestRunMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(ingestRunDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IngestRun in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamIngestRun() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ingestRun.setId(longCount.incrementAndGet());

        // Create the IngestRun
        IngestRunDTO ingestRunDTO = ingestRunMapper.toDto(ingestRun);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIngestRunMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ingestRunDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the IngestRun in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateIngestRunWithPatch() throws Exception {
        // Initialize the database
        insertedIngestRun = ingestRunRepository.saveAndFlush(ingestRun);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the ingestRun using partial update
        IngestRun partialUpdatedIngestRun = new IngestRun();
        partialUpdatedIngestRun.setId(ingestRun.getId());

        partialUpdatedIngestRun.finishedAt(UPDATED_FINISHED_AT).filesSeen(UPDATED_FILES_SEEN).filesFailed(UPDATED_FILES_FAILED);

        restIngestRunMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIngestRun.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedIngestRun))
            )
            .andExpect(status().isOk());

        // Validate the IngestRun in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertIngestRunUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedIngestRun, ingestRun),
            getPersistedIngestRun(ingestRun)
        );
    }

    @Test
    @Transactional
    void fullUpdateIngestRunWithPatch() throws Exception {
        // Initialize the database
        insertedIngestRun = ingestRunRepository.saveAndFlush(ingestRun);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the ingestRun using partial update
        IngestRun partialUpdatedIngestRun = new IngestRun();
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

        // Validate the IngestRun in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertIngestRunUpdatableFieldsEquals(partialUpdatedIngestRun, getPersistedIngestRun(partialUpdatedIngestRun));
    }

    @Test
    @Transactional
    void patchNonExistingIngestRun() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ingestRun.setId(longCount.incrementAndGet());

        // Create the IngestRun
        IngestRunDTO ingestRunDTO = ingestRunMapper.toDto(ingestRun);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIngestRunMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ingestRunDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(ingestRunDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IngestRun in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchIngestRun() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ingestRun.setId(longCount.incrementAndGet());

        // Create the IngestRun
        IngestRunDTO ingestRunDTO = ingestRunMapper.toDto(ingestRun);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIngestRunMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(ingestRunDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IngestRun in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamIngestRun() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ingestRun.setId(longCount.incrementAndGet());

        // Create the IngestRun
        IngestRunDTO ingestRunDTO = ingestRunMapper.toDto(ingestRun);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIngestRunMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(ingestRunDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the IngestRun in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteIngestRun() throws Exception {
        // Initialize the database
        insertedIngestRun = ingestRunRepository.saveAndFlush(ingestRun);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the ingestRun
        restIngestRunMockMvc
            .perform(delete(ENTITY_API_URL_ID, ingestRun.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return ingestRunRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected IngestRun getPersistedIngestRun(IngestRun ingestRun) {
        return ingestRunRepository.findById(ingestRun.getId()).orElseThrow();
    }

    protected void assertPersistedIngestRunToMatchAllProperties(IngestRun expectedIngestRun) {
        assertIngestRunAllPropertiesEquals(expectedIngestRun, getPersistedIngestRun(expectedIngestRun));
    }

    protected void assertPersistedIngestRunToMatchUpdatableProperties(IngestRun expectedIngestRun) {
        assertIngestRunAllUpdatablePropertiesEquals(expectedIngestRun, getPersistedIngestRun(expectedIngestRun));
    }
}
