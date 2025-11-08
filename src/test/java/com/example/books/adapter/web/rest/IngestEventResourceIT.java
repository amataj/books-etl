package com.example.books.adapter.web.rest;

import static com.example.books.adapter.web.rest.TestUtil.createUpdateProxyForBean;
import static com.example.books.adapter.web.rest.TestUtil.sameInstant;
import static com.example.books.infrastructure.database.jpa.entity.IngestEventAsserts.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.books.IntegrationTest;
import com.example.books.domain.core.IngestEvent;
import com.example.books.infrastructure.database.jpa.entity.IngestEventEntity;
import com.example.books.infrastructure.database.jpa.mapper.IngestEventMapper;
import com.example.books.infrastructure.database.jpa.repository.IngestEventJpaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Random;
import java.util.UUID;
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
 * Integration tests for the {@link IngestEventResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class IngestEventResourceIT {

    private static final UUID DEFAULT_RUN_ID = UUID.randomUUID();
    private static final UUID UPDATED_RUN_ID = UUID.randomUUID();

    private static final String DEFAULT_DOCUMENT_ID = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENT_ID = "BBBBBBBBBB";

    private static final String DEFAULT_TOPIC = "AAAAAAAAAA";
    private static final String UPDATED_TOPIC = "BBBBBBBBBB";

    private static final String DEFAULT_PAYLOAD = "AAAAAAAAAA";
    private static final String UPDATED_PAYLOAD = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/ingest-events";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private IngestEventJpaRepository ingestEventRepository;

    @Autowired
    private IngestEventMapper ingestEventMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restIngestEventMockMvc;

    private IngestEventEntity ingestEvent;

    private IngestEventEntity insertedIngestEvent;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IngestEventEntity createEntity() {
        return new IngestEventEntity()
            .runId(DEFAULT_RUN_ID)
            .documentId(DEFAULT_DOCUMENT_ID)
            .topic(DEFAULT_TOPIC)
            .payload(DEFAULT_PAYLOAD)
            .createdAt(DEFAULT_CREATED_AT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IngestEventEntity createUpdatedEntity() {
        return new IngestEventEntity()
            .runId(UPDATED_RUN_ID)
            .documentId(UPDATED_DOCUMENT_ID)
            .topic(UPDATED_TOPIC)
            .payload(UPDATED_PAYLOAD)
            .createdAt(UPDATED_CREATED_AT);
    }

    @BeforeEach
    void initTest() {
        ingestEvent = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedIngestEvent != null) {
            ingestEventRepository.delete(insertedIngestEvent);
            insertedIngestEvent = null;
        }
    }

    @Test
    @Transactional
    void createIngestEvent() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the IngestEvent
        IngestEvent ingestEventDTO = ingestEventMapper.toDto(ingestEvent);
        var returnedIngestEventDTO = om.readValue(
            restIngestEventMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ingestEventDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            IngestEvent.class
        );

        // Validate the IngestEvent in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedIngestEvent = ingestEventMapper.toEntity(returnedIngestEventDTO);
        assertIngestEventUpdatableFieldsEquals(returnedIngestEvent, getPersistedIngestEvent(returnedIngestEvent));

        insertedIngestEvent = returnedIngestEvent;
    }

    @Test
    @Transactional
    void createIngestEventWithExistingId() throws Exception {
        // Create the IngestEvent with an existing ID
        ingestEvent.setId(1L);
        IngestEvent ingestEventDTO = ingestEventMapper.toDto(ingestEvent);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restIngestEventMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ingestEventDTO)))
            .andExpect(status().isBadRequest());

        // Validate the IngestEvent in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTopicIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        ingestEvent.setTopic(null);

        // Create the IngestEvent, which fails.
        IngestEvent ingestEventDTO = ingestEventMapper.toDto(ingestEvent);

        restIngestEventMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ingestEventDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllIngestEvents() throws Exception {
        // Initialize the database
        insertedIngestEvent = ingestEventRepository.saveAndFlush(ingestEvent);

        // Get all the ingestEventList
        restIngestEventMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ingestEvent.getId().intValue())))
            .andExpect(jsonPath("$.[*].runId").value(hasItem(DEFAULT_RUN_ID.toString())))
            .andExpect(jsonPath("$.[*].documentId").value(hasItem(DEFAULT_DOCUMENT_ID)))
            .andExpect(jsonPath("$.[*].topic").value(hasItem(DEFAULT_TOPIC)))
            .andExpect(jsonPath("$.[*].payload").value(hasItem(DEFAULT_PAYLOAD)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))));
    }

    @Test
    @Transactional
    void getIngestEvent() throws Exception {
        // Initialize the database
        insertedIngestEvent = ingestEventRepository.saveAndFlush(ingestEvent);

        // Get the ingestEvent
        restIngestEventMockMvc
            .perform(get(ENTITY_API_URL_ID, ingestEvent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ingestEvent.getId().intValue()))
            .andExpect(jsonPath("$.runId").value(DEFAULT_RUN_ID.toString()))
            .andExpect(jsonPath("$.documentId").value(DEFAULT_DOCUMENT_ID))
            .andExpect(jsonPath("$.topic").value(DEFAULT_TOPIC))
            .andExpect(jsonPath("$.payload").value(DEFAULT_PAYLOAD))
            .andExpect(jsonPath("$.createdAt").value(sameInstant(DEFAULT_CREATED_AT)));
    }

    @Test
    @Transactional
    void getNonExistingIngestEvent() throws Exception {
        // Get the ingestEvent
        restIngestEventMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingIngestEvent() throws Exception {
        // Initialize the database
        insertedIngestEvent = ingestEventRepository.saveAndFlush(ingestEvent);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the ingestEvent
        IngestEventEntity updatedIngestEvent = ingestEventRepository.findById(ingestEvent.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedIngestEvent are not directly saved in db
        em.detach(updatedIngestEvent);
        updatedIngestEvent
            .runId(UPDATED_RUN_ID)
            .documentId(UPDATED_DOCUMENT_ID)
            .topic(UPDATED_TOPIC)
            .payload(UPDATED_PAYLOAD)
            .createdAt(UPDATED_CREATED_AT);
        IngestEvent ingestEventDTO = ingestEventMapper.toDto(updatedIngestEvent);

        restIngestEventMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ingestEventDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(ingestEventDTO))
            )
            .andExpect(status().isOk());

        // Validate the IngestEvent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedIngestEventToMatchAllProperties(updatedIngestEvent);
    }

    @Test
    @Transactional
    void putNonExistingIngestEvent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ingestEvent.setId(longCount.incrementAndGet());

        // Create the IngestEvent
        IngestEvent ingestEventDTO = ingestEventMapper.toDto(ingestEvent);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIngestEventMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ingestEventDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(ingestEventDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IngestEvent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchIngestEvent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ingestEvent.setId(longCount.incrementAndGet());

        // Create the IngestEvent
        IngestEvent ingestEventDTO = ingestEventMapper.toDto(ingestEvent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIngestEventMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(ingestEventDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IngestEvent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamIngestEvent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ingestEvent.setId(longCount.incrementAndGet());

        // Create the IngestEvent
        IngestEvent ingestEventDTO = ingestEventMapper.toDto(ingestEvent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIngestEventMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ingestEventDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the IngestEvent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateIngestEventWithPatch() throws Exception {
        // Initialize the database
        insertedIngestEvent = ingestEventRepository.saveAndFlush(ingestEvent);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the ingestEvent using partial update
        IngestEventEntity partialUpdatedIngestEvent = new IngestEventEntity();
        partialUpdatedIngestEvent.setId(ingestEvent.getId());

        partialUpdatedIngestEvent.topic(UPDATED_TOPIC);

        restIngestEventMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIngestEvent.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedIngestEvent))
            )
            .andExpect(status().isOk());

        // Validate the IngestEvent in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertIngestEventUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedIngestEvent, ingestEvent),
            getPersistedIngestEvent(ingestEvent)
        );
    }

    @Test
    @Transactional
    void fullUpdateIngestEventWithPatch() throws Exception {
        // Initialize the database
        insertedIngestEvent = ingestEventRepository.saveAndFlush(ingestEvent);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the ingestEvent using partial update
        IngestEventEntity partialUpdatedIngestEvent = new IngestEventEntity();
        partialUpdatedIngestEvent.setId(ingestEvent.getId());

        partialUpdatedIngestEvent
            .runId(UPDATED_RUN_ID)
            .documentId(UPDATED_DOCUMENT_ID)
            .topic(UPDATED_TOPIC)
            .payload(UPDATED_PAYLOAD)
            .createdAt(UPDATED_CREATED_AT);

        restIngestEventMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIngestEvent.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedIngestEvent))
            )
            .andExpect(status().isOk());

        // Validate the IngestEvent in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertIngestEventUpdatableFieldsEquals(partialUpdatedIngestEvent, getPersistedIngestEvent(partialUpdatedIngestEvent));
    }

    @Test
    @Transactional
    void patchNonExistingIngestEvent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ingestEvent.setId(longCount.incrementAndGet());

        // Create the IngestEvent
        IngestEvent ingestEventDTO = ingestEventMapper.toDto(ingestEvent);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIngestEventMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ingestEventDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(ingestEventDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IngestEvent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchIngestEvent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ingestEvent.setId(longCount.incrementAndGet());

        // Create the IngestEvent
        IngestEvent ingestEventDTO = ingestEventMapper.toDto(ingestEvent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIngestEventMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(ingestEventDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IngestEvent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamIngestEvent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ingestEvent.setId(longCount.incrementAndGet());

        // Create the IngestEvent
        IngestEvent ingestEventDTO = ingestEventMapper.toDto(ingestEvent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIngestEventMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(ingestEventDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the IngestEvent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteIngestEvent() throws Exception {
        // Initialize the database
        insertedIngestEvent = ingestEventRepository.saveAndFlush(ingestEvent);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the ingestEvent
        restIngestEventMockMvc
            .perform(delete(ENTITY_API_URL_ID, ingestEvent.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return ingestEventRepository.count();
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

    protected IngestEventEntity getPersistedIngestEvent(IngestEventEntity ingestEvent) {
        return ingestEventRepository.findById(ingestEvent.getId()).orElseThrow();
    }

    protected void assertPersistedIngestEventToMatchAllProperties(IngestEventEntity expectedIngestEvent) {
        assertIngestEventAllPropertiesEquals(expectedIngestEvent, getPersistedIngestEvent(expectedIngestEvent));
    }

    protected void assertPersistedIngestEventToMatchUpdatableProperties(IngestEventEntity expectedIngestEvent) {
        assertIngestEventAllUpdatablePropertiesEquals(expectedIngestEvent, getPersistedIngestEvent(expectedIngestEvent));
    }
}
