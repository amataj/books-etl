package com.example.books.web.rest;

import static com.example.books.domain.BookFileAsserts.*;
import static com.example.books.web.rest.TestUtil.createUpdateProxyForBean;
import static com.example.books.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.books.IntegrationTest;
import com.example.books.adapter.web.rest.dto.BookFileDTO;
import com.example.books.adapter.web.rest.mapper.BookFileRestMapper;
import com.example.books.domain.core.bookfile.BookFile;
import com.example.books.infrastructure.database.jpa.entity.BookEntity;
import com.example.books.infrastructure.database.jpa.entity.BookFileEntity;
import com.example.books.infrastructure.database.jpa.mapper.BookFileEntityMapper;
import com.example.books.infrastructure.database.jpa.repository.BookFileJpaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
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
 * Integration tests for the {@link BookFileResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BookFileResourceIT {

    private static final String DEFAULT_PATH_NORM = "AAAAAAAAAA";
    private static final String UPDATED_PATH_NORM = "BBBBBBBBBB";

    private static final String DEFAULT_SHA_256 = "AAAAAAAAAA";
    private static final String UPDATED_SHA_256 = "BBBBBBBBBB";

    private static final Long DEFAULT_SIZE_BYTES = 1L;
    private static final Long UPDATED_SIZE_BYTES = 2L;

    private static final ZonedDateTime DEFAULT_MTIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_MTIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_STORAGE_URI = "AAAAAAAAAA";
    private static final String UPDATED_STORAGE_URI = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_FIRST_SEEN_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_FIRST_SEEN_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_LAST_SEEN_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LAST_SEEN_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/book-files";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private BookFileJpaRepository bookFileJpaRepository;

    @Autowired
    private BookFileEntityMapper bookFileEntityMapper;

    @Autowired
    private BookFileRestMapper bookFileRestMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBookFileMockMvc;

    private BookFileEntity bookFile;

    private BookFileEntity insertedBookFile;

    public static BookFileEntity createEntity(EntityManager em) {
        BookFileEntity bookFile = new BookFileEntity()
            .pathNorm(DEFAULT_PATH_NORM)
            .sha256(DEFAULT_SHA_256)
            .sizeBytes(DEFAULT_SIZE_BYTES)
            .mtime(DEFAULT_MTIME)
            .storageUri(DEFAULT_STORAGE_URI)
            .firstSeenAt(DEFAULT_FIRST_SEEN_AT)
            .lastSeenAt(DEFAULT_LAST_SEEN_AT);
        bookFile.setBook(getOrCreateBook(em));
        return bookFile;
    }

    public static BookFileEntity createUpdatedEntity(EntityManager em) {
        BookFileEntity bookFile = new BookFileEntity()
            .pathNorm(UPDATED_PATH_NORM)
            .sha256(UPDATED_SHA_256)
            .sizeBytes(UPDATED_SIZE_BYTES)
            .mtime(UPDATED_MTIME)
            .storageUri(UPDATED_STORAGE_URI)
            .firstSeenAt(UPDATED_FIRST_SEEN_AT)
            .lastSeenAt(UPDATED_LAST_SEEN_AT);
        bookFile.setBook(getOrCreateBook(em));
        return bookFile;
    }

    private static BookEntity getOrCreateBook(EntityManager em) {
        List<BookEntity> books = TestUtil.findAll(em, BookEntity.class);
        if (books.isEmpty()) {
            BookEntity book = BookResourceIT.createEntity();
            em.persist(book);
            em.flush();
            return book;
        }
        return books.get(0);
    }

    @BeforeEach
    void initTest() {
        bookFile = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedBookFile != null) {
            bookFileJpaRepository.delete(insertedBookFile);
            insertedBookFile = null;
        }
    }

    @Test
    @Transactional
    void createBookFile() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        BookFileDTO bookFileDTO = toDto(bookFile);
        BookFileDTO returnedBookFileDTO = om.readValue(
            restBookFileMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookFileDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            BookFileDTO.class
        );

        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        BookFile returnedBookFile = bookFileRestMapper.toDomain(returnedBookFileDTO);
        assertBookFileUpdatableFieldsEquals(returnedBookFile, getPersistedBookFile(returnedBookFile.id()));
        insertedBookFile = bookFileJpaRepository.findById(returnedBookFile.id()).orElseThrow();
    }

    @Test
    @Transactional
    void createBookFileWithExistingId() throws Exception {
        bookFile.setId(1L);
        BookFileDTO bookFileDTO = toDto(bookFile);

        long databaseSizeBeforeCreate = getRepositoryCount();

        restBookFileMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookFileDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSha256IsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        bookFile.setSha256(null);
        BookFileDTO bookFileDTO = toDto(bookFile);

        restBookFileMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookFileDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBookFiles() throws Exception {
        insertedBookFile = bookFileJpaRepository.saveAndFlush(bookFile);

        restBookFileMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bookFile.getId().intValue())))
            .andExpect(jsonPath("$.[*].pathNorm").value(hasItem(DEFAULT_PATH_NORM)))
            .andExpect(jsonPath("$.[*].sha256").value(hasItem(DEFAULT_SHA_256)))
            .andExpect(jsonPath("$.[*].sizeBytes").value(hasItem(DEFAULT_SIZE_BYTES.intValue())))
            .andExpect(jsonPath("$.[*].mtime").value(hasItem(sameInstant(DEFAULT_MTIME))))
            .andExpect(jsonPath("$.[*].storageUri").value(hasItem(DEFAULT_STORAGE_URI)))
            .andExpect(jsonPath("$.[*].firstSeenAt").value(hasItem(sameInstant(DEFAULT_FIRST_SEEN_AT))))
            .andExpect(jsonPath("$.[*].lastSeenAt").value(hasItem(sameInstant(DEFAULT_LAST_SEEN_AT))));
    }

    @Test
    @Transactional
    void getBookFile() throws Exception {
        insertedBookFile = bookFileJpaRepository.saveAndFlush(bookFile);

        restBookFileMockMvc
            .perform(get(ENTITY_API_URL_ID, bookFile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(bookFile.getId().intValue()))
            .andExpect(jsonPath("$.pathNorm").value(DEFAULT_PATH_NORM))
            .andExpect(jsonPath("$.sha256").value(DEFAULT_SHA_256))
            .andExpect(jsonPath("$.sizeBytes").value(DEFAULT_SIZE_BYTES.intValue()))
            .andExpect(jsonPath("$.mtime").value(sameInstant(DEFAULT_MTIME)))
            .andExpect(jsonPath("$.storageUri").value(DEFAULT_STORAGE_URI))
            .andExpect(jsonPath("$.firstSeenAt").value(sameInstant(DEFAULT_FIRST_SEEN_AT)))
            .andExpect(jsonPath("$.lastSeenAt").value(sameInstant(DEFAULT_LAST_SEEN_AT)));
    }

    @Test
    @Transactional
    void getNonExistingBookFile() throws Exception {
        restBookFileMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBookFile() throws Exception {
        insertedBookFile = bookFileJpaRepository.saveAndFlush(bookFile);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        BookFileEntity updatedBookFile = bookFileJpaRepository.findById(bookFile.getId()).orElseThrow();
        em.detach(updatedBookFile);
        updatedBookFile
            .pathNorm(UPDATED_PATH_NORM)
            .sha256(UPDATED_SHA_256)
            .sizeBytes(UPDATED_SIZE_BYTES)
            .mtime(UPDATED_MTIME)
            .storageUri(UPDATED_STORAGE_URI)
            .firstSeenAt(UPDATED_FIRST_SEEN_AT)
            .lastSeenAt(UPDATED_LAST_SEEN_AT);
        BookFileDTO bookFileDTO = toDto(updatedBookFile);

        restBookFileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bookFileDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(bookFileDTO))
            )
            .andExpect(status().isOk());

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedBookFileToMatchAllProperties(updatedBookFile);
    }

    @Test
    @Transactional
    void putNonExistingBookFile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bookFile.setId(longCount.incrementAndGet());
        BookFileDTO bookFileDTO = toDto(bookFile);

        restBookFileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bookFileDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(bookFileDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBookFile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bookFile.setId(longCount.incrementAndGet());
        BookFileDTO bookFileDTO = toDto(bookFile);

        restBookFileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(bookFileDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBookFile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bookFile.setId(longCount.incrementAndGet());
        BookFileDTO bookFileDTO = toDto(bookFile);

        restBookFileMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookFileDTO)))
            .andExpect(status().isMethodNotAllowed());

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBookFileWithPatch() throws Exception {
        insertedBookFile = bookFileJpaRepository.saveAndFlush(bookFile);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        BookFileEntity partialUpdatedBookFile = new BookFileEntity();
        partialUpdatedBookFile.setId(bookFile.getId());
        partialUpdatedBookFile.sha256(UPDATED_SHA_256).storageUri(UPDATED_STORAGE_URI).lastSeenAt(UPDATED_LAST_SEEN_AT);

        restBookFileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBookFile.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBookFile))
            )
            .andExpect(status().isOk());

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBookFileUpdatableFieldsEquals(
            toDomain(createUpdateProxyForBean(partialUpdatedBookFile, bookFile)),
            getPersistedBookFile(bookFile.getId())
        );
    }

    @Test
    @Transactional
    void fullUpdateBookFileWithPatch() throws Exception {
        insertedBookFile = bookFileJpaRepository.saveAndFlush(bookFile);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        BookFileEntity partialUpdatedBookFile = new BookFileEntity();
        partialUpdatedBookFile.setId(bookFile.getId());
        partialUpdatedBookFile
            .pathNorm(UPDATED_PATH_NORM)
            .sha256(UPDATED_SHA_256)
            .sizeBytes(UPDATED_SIZE_BYTES)
            .mtime(UPDATED_MTIME)
            .storageUri(UPDATED_STORAGE_URI)
            .firstSeenAt(UPDATED_FIRST_SEEN_AT)
            .lastSeenAt(UPDATED_LAST_SEEN_AT);

        restBookFileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBookFile.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBookFile))
            )
            .andExpect(status().isOk());

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBookFileUpdatableFieldsEquals(toDomain(partialUpdatedBookFile), getPersistedBookFile(partialUpdatedBookFile.getId()));
    }

    @Test
    @Transactional
    void patchNonExistingBookFile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bookFile.setId(longCount.incrementAndGet());
        BookFileDTO bookFileDTO = toDto(bookFile);

        restBookFileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, bookFileDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(bookFileDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBookFile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bookFile.setId(longCount.incrementAndGet());
        BookFileDTO bookFileDTO = toDto(bookFile);

        restBookFileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(bookFileDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBookFile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bookFile.setId(longCount.incrementAndGet());
        BookFileDTO bookFileDTO = toDto(bookFile);

        restBookFileMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(bookFileDTO)))
            .andExpect(status().isMethodNotAllowed());

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBookFile() throws Exception {
        insertedBookFile = bookFileJpaRepository.saveAndFlush(bookFile);

        long databaseSizeBeforeDelete = getRepositoryCount();

        restBookFileMockMvc
            .perform(delete(ENTITY_API_URL_ID, bookFile.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return bookFileJpaRepository.count();
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

    protected BookFile getPersistedBookFile(Long id) {
        return bookFileEntityMapper.toDomain(bookFileJpaRepository.findById(id).orElseThrow());
    }

    protected void assertPersistedBookFileToMatchAllProperties(BookFileEntity expectedBookFile) {
        assertBookFileAllPropertiesEquals(toDomain(expectedBookFile), getPersistedBookFile(expectedBookFile.getId()));
    }

    protected void assertPersistedBookFileToMatchUpdatableProperties(BookFileEntity expectedBookFile) {
        assertBookFileAllUpdatablePropertiesEquals(toDomain(expectedBookFile), getPersistedBookFile(expectedBookFile.getId()));
    }

    private BookFileDTO toDto(BookFileEntity entity) {
        return bookFileRestMapper.toDto(bookFileEntityMapper.toDomain(entity));
    }

    private BookFile toDomain(BookFileEntity entity) {
        return bookFileEntityMapper.toDomain(entity);
    }
}
