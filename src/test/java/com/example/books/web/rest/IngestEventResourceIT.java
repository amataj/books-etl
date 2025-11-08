package com.example.books.web.rest;

import static com.example.books.domain.IngestEventAsserts.*;
import static com.example.books.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.books.IntegrationTest;
import com.example.books.adapter.web.rest.dto.IngestEventDTO;
import com.example.books.adapter.web.rest.mapper.IngestEventRestMapper;
import com.example.books.domain.core.ingestevent.IngestEvent;
import com.example.books.infrastructure.database.jpa.entity.IngestEventEntity;
import com.example.books.infrastructure.database.jpa.entity.IngestRunEntity;
import com.example.books.infrastructure.database.jpa.mapper.IngestEventEntityMapper;
import com.example.books.infrastructure.database.jpa.repository.IngestEventJpaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
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

    private static final String DEFAULT_TOPIC = "topic-1";
    private static final String UPDATED_TOPIC = "topic-2";

    private static final String DEFAULT_PAYLOAD = "{}";
    private static final String UPDATED_PAYLOAD = "{\"foo\":42}";

    private static final ZonedDateTime DEFAULT_CREATED_AT = ZonedDateTime.now(ZoneOffset.UTC).withNano(0);
    private static final ZonedDateTime UPDATED_CREATED_AT = DEFAULT_CREATED_AT.plusDays(1);

    private static final String ENTITY_API_URL = "/api/ingest-events";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private IngestEventJpaRepository ingestEventJpaRepository;

    @Autowired
    private IngestEventEntityMapper ingestEventEntityMapper;

    @Autowired
    private IngestEventRestMapper ingestEventRestMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restIngestEventMockMvc;

    private IngestEventEntity ingestEvent;

    private IngestEventEntity insertedIngestEvent;

    public static IngestEventEntity createEntity(EntityManager em) {
        IngestEventEntity ingestEvent = new IngestEventEntity()
            .runId(DEFAULT_RUN_ID)
            .documentId(DEFAULT_DOCUMENT_ID)
            .topic(DEFAULT_TOPIC)
            .payload(DEFAULT_PAYLOAD)
            .createdAt(DEFAULT_CREATED_AT);
        ingestEvent.setIngestRun(getOrCreateRun(em));
        return ingestEvent;
    }

    public static IngestEventEntity createUpdatedEntity(EntityManager em) {
        IngestEventEntity ingestEvent = new IngestEventEntity()
            .runId(UPDATED_RUN_ID)
            .documentId(UPDATED_DOCUMENT_ID)
            .topic(UPDATED_TOPIC)
            .payload(UPDATED_PAYLOAD)
            .createdAt(UPDATED_CREATED_AT);
        ingestEvent.setIngestRun(getOrCreateRun(em));
        return ingestEvent;
    }

    private static IngestRunEntity getOrCreateRun(EntityManager em) {
        List<IngestRunEntity> runs = TestUtil.findAll(em, IngestRunEntity.class);
        if (runs.isEmpty()) {
            IngestRunEntity run = IngestRunResourceIT.createEntity(em);
            em.persist(run);
            em.flush();
            return run;
        }
        return runs.get(0);
    }

    @BeforeEach
    void initTest() {
        ingestEvent = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedIngestEvent != null) {
            ingestEventJpaRepository.delete(insertedIngestEvent);
            insertedIngestEvent = null;
        }
    }

    @Test
    @Transactional
    void createIngestEvent() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        IngestEventDTO ingestEventDTO = toDto(ingestEvent);
        IngestEventDTO returnedIngestEventDTO = om.readValue(
            restIngestEventMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ingestEventDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            IngestEventDTO.class
        );

        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        IngestEvent returnedIngestEvent = ingestEventRestMapper.toDomain(returnedIngestEventDTO);
        assertIngestEventUpdatableFieldsEquals(returnedIngestEvent, getPersistedIngestEvent(returnedIngestEvent.id()));
        insertedIngestEvent = ingestEventJpaRepository.findById(returnedIngestEvent.id()).orElseThrow();
    }

    @Test
    @Transactional
    void createIngestEventWithExistingId() throws Exception {
        ingestEvent.setId(1L);
        IngestEventDTO ingestEventDTO = toDto(ingestEvent);

        long databaseSizeBeforeCreate = getRepositoryCount();

        restIngestEventMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ingestEventDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTopicIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        ingestEvent.setTopic(null);
        IngestEventDTO ingestEventDTO = toDto(ingestEvent);

        restIngestEventMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ingestEventDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPayloadIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        ingestEvent.setPayload(null);
        IngestEventDTO ingestEventDTO = toDto(ingestEvent);

        restIngestEventMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ingestEventDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllIngestEvents() throws Exception {
        insertedIngestEvent = ingestEventJpaRepository.saveAndFlush(ingestEvent);

        restIngestEventMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ingestEvent.getId().intValue())))
            .andExpect(jsonPath("$.[*].topic").value(hasItem(DEFAULT_TOPIC)))
            .andExpect(jsonPath("$.[*].payload").value(hasItem(DEFAULT_PAYLOAD)));
    }

    @Test
    @Transactional
    void getIngestEvent() throws Exception {
        insertedIngestEvent = ingestEventJpaRepository.saveAndFlush(ingestEvent);

        restIngestEventMockMvc
            .perform(get(ENTITY_API_URL_ID, ingestEvent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ingestEvent.getId().intValue()))
            .andExpect(jsonPath("$.topic").value(DEFAULT_TOPIC))
            .andExpect(jsonPath("$.payload").value(DEFAULT_PAYLOAD));
    }

    @Test
    @Transactional
    void getNonExistingIngestEvent() throws Exception {
        restIngestEventMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingIngestEvent() throws Exception {
        insertedIngestEvent = ingestEventJpaRepository.saveAndFlush(ingestEvent);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        IngestEventEntity updatedIngestEvent = ingestEventJpaRepository.findById(ingestEvent.getId()).orElseThrow();
        em.detach(updatedIngestEvent);
        updatedIngestEvent
            .runId(UPDATED_RUN_ID)
            .documentId(UPDATED_DOCUMENT_ID)
            .topic(UPDATED_TOPIC)
            .payload(UPDATED_PAYLOAD)
            .createdAt(UPDATED_CREATED_AT);
        IngestEventDTO ingestEventDTO = toDto(updatedIngestEvent);

        restIngestEventMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ingestEventDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(ingestEventDTO))
            )
            .andExpect(status().isOk());

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedIngestEventToMatchAllProperties(updatedIngestEvent);
    }

    @Test
    @Transactional
    void putNonExistingIngestEvent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ingestEvent.setId(longCount.incrementAndGet());
        IngestEventDTO ingestEventDTO = toDto(ingestEvent);

        restIngestEventMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ingestEventDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(ingestEventDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchIngestEvent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ingestEvent.setId(longCount.incrementAndGet());
        IngestEventDTO ingestEventDTO = toDto(ingestEvent);

        restIngestEventMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(ingestEventDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamIngestEvent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ingestEvent.setId(longCount.incrementAndGet());
        IngestEventDTO ingestEventDTO = toDto(ingestEvent);

        restIngestEventMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ingestEventDTO)))
            .andExpect(status().isMethodNotAllowed());

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateIngestEventWithPatch() throws Exception {
        insertedIngestEvent = ingestEventJpaRepository.saveAndFlush(ingestEvent);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        IngestEventEntity partialUpdatedIngestEvent = new IngestEventEntity();
        partialUpdatedIngestEvent.setId(ingestEvent.getId());
        partialUpdatedIngestEvent.topic(UPDATED_TOPIC).payload(UPDATED_PAYLOAD);

        restIngestEventMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIngestEvent.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedIngestEvent))
            )
            .andExpect(status().isOk());

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertIngestEventUpdatableFieldsEquals(
            toDomain(createUpdateProxyForBean(partialUpdatedIngestEvent, ingestEvent)),
            getPersistedIngestEvent(ingestEvent.getId())
        );
    }

    @Test
    @Transactional
    void fullUpdateIngestEventWithPatch() throws Exception {
        insertedIngestEvent = ingestEventJpaRepository.saveAndFlush(ingestEvent);

        long databaseSizeBeforeUpdate = getRepositoryCount();

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

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertIngestEventUpdatableFieldsEquals(
            toDomain(partialUpdatedIngestEvent),
            getPersistedIngestEvent(partialUpdatedIngestEvent.getId())
        );
    }

    @Test
    @Transactional
    void patchNonExistingIngestEvent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ingestEvent.setId(longCount.incrementAndGet());
        IngestEventDTO ingestEventDTO = toDto(ingestEvent);

        restIngestEventMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ingestEventDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(ingestEventDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchIngestEvent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ingestEvent.setId(longCount.incrementAndGet());
        IngestEventDTO ingestEventDTO = toDto(ingestEvent);

        restIngestEventMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(ingestEventDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamIngestEvent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ingestEvent.setId(longCount.incrementAndGet());
        IngestEventDTO ingestEventDTO = toDto(ingestEvent);

        restIngestEventMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(ingestEventDTO)))
            .andExpect(status().isMethodNotAllowed());

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteIngestEvent() throws Exception {
        insertedIngestEvent = ingestEventJpaRepository.saveAndFlush(ingestEvent);

        long databaseSizeBeforeDelete = getRepositoryCount();

        restIngestEventMockMvc
            .perform(delete(ENTITY_API_URL_ID, ingestEvent.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return ingestEventJpaRepository.count();
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

    protected IngestEvent getPersistedIngestEvent(Long id) {
        return ingestEventEntityMapper.toDomain(ingestEventJpaRepository.findById(id).orElseThrow());
    }

    protected void assertPersistedIngestEventToMatchAllProperties(IngestEventEntity expectedIngestEvent) {
        assertIngestEventAllPropertiesEquals(toDomain(expectedIngestEvent), getPersistedIngestEvent(expectedIngestEvent.getId()));
    }

    protected void assertPersistedIngestEventToMatchUpdatableProperties(IngestEventEntity expectedIngestEvent) {
        assertIngestEventAllUpdatablePropertiesEquals(toDomain(expectedIngestEvent), getPersistedIngestEvent(expectedIngestEvent.getId()));
    }

    private IngestEventDTO toDto(IngestEventEntity entity) {
        return ingestEventRestMapper.toDto(ingestEventEntityMapper.toDomain(entity));
    }

    private IngestEvent toDomain(IngestEventEntity entity) {
        return ingestEventEntityMapper.toDomain(entity);
    }
}
