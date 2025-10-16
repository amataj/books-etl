package com.example.books.web.rest;

import static com.example.books.domain.BookFileAsserts.*;
import static com.example.books.web.rest.TestUtil.createUpdateProxyForBean;
import static com.example.books.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.books.IntegrationTest;
import com.example.books.domain.BookFile;
import com.example.books.repository.BookFileRepository;
import com.example.books.service.BookFileService;
import com.example.books.service.dto.BookFileDTO;
import com.example.books.service.mapper.BookFileMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link BookFileResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
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

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private BookFileRepository bookFileRepository;

    @Mock
    private BookFileRepository bookFileRepositoryMock;

    @Autowired
    private BookFileMapper bookFileMapper;

    @Mock
    private BookFileService bookFileServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBookFileMockMvc;

    private BookFile bookFile;

    private BookFile insertedBookFile;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BookFile createEntity() {
        return new BookFile()
            .pathNorm(DEFAULT_PATH_NORM)
            .sha256(DEFAULT_SHA_256)
            .sizeBytes(DEFAULT_SIZE_BYTES)
            .mtime(DEFAULT_MTIME)
            .storageUri(DEFAULT_STORAGE_URI)
            .firstSeenAt(DEFAULT_FIRST_SEEN_AT)
            .lastSeenAt(DEFAULT_LAST_SEEN_AT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BookFile createUpdatedEntity() {
        return new BookFile()
            .pathNorm(UPDATED_PATH_NORM)
            .sha256(UPDATED_SHA_256)
            .sizeBytes(UPDATED_SIZE_BYTES)
            .mtime(UPDATED_MTIME)
            .storageUri(UPDATED_STORAGE_URI)
            .firstSeenAt(UPDATED_FIRST_SEEN_AT)
            .lastSeenAt(UPDATED_LAST_SEEN_AT);
    }

    @BeforeEach
    void initTest() {
        bookFile = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedBookFile != null) {
            bookFileRepository.delete(insertedBookFile);
            insertedBookFile = null;
        }
    }

    @Test
    @Transactional
    void createBookFile() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the BookFile
        BookFileDTO bookFileDTO = bookFileMapper.toDto(bookFile);
        var returnedBookFileDTO = om.readValue(
            restBookFileMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookFileDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            BookFileDTO.class
        );

        // Validate the BookFile in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedBookFile = bookFileMapper.toEntity(returnedBookFileDTO);
        assertBookFileUpdatableFieldsEquals(returnedBookFile, getPersistedBookFile(returnedBookFile));

        insertedBookFile = returnedBookFile;
    }

    @Test
    @Transactional
    void createBookFileWithExistingId() throws Exception {
        // Create the BookFile with an existing ID
        bookFile.setId(1L);
        BookFileDTO bookFileDTO = bookFileMapper.toDto(bookFile);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBookFileMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookFileDTO)))
            .andExpect(status().isBadRequest());

        // Validate the BookFile in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSha256IsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        bookFile.setSha256(null);

        // Create the BookFile, which fails.
        BookFileDTO bookFileDTO = bookFileMapper.toDto(bookFile);

        restBookFileMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookFileDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBookFiles() throws Exception {
        // Initialize the database
        insertedBookFile = bookFileRepository.saveAndFlush(bookFile);

        // Get all the bookFileList
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

    @SuppressWarnings({ "unchecked" })
    void getAllBookFilesWithEagerRelationshipsIsEnabled() throws Exception {
        when(bookFileServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBookFileMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(bookFileServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBookFilesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(bookFileServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBookFileMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(bookFileRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getBookFile() throws Exception {
        // Initialize the database
        insertedBookFile = bookFileRepository.saveAndFlush(bookFile);

        // Get the bookFile
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
        // Get the bookFile
        restBookFileMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBookFile() throws Exception {
        // Initialize the database
        insertedBookFile = bookFileRepository.saveAndFlush(bookFile);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bookFile
        BookFile updatedBookFile = bookFileRepository.findById(bookFile.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedBookFile are not directly saved in db
        em.detach(updatedBookFile);
        updatedBookFile
            .pathNorm(UPDATED_PATH_NORM)
            .sha256(UPDATED_SHA_256)
            .sizeBytes(UPDATED_SIZE_BYTES)
            .mtime(UPDATED_MTIME)
            .storageUri(UPDATED_STORAGE_URI)
            .firstSeenAt(UPDATED_FIRST_SEEN_AT)
            .lastSeenAt(UPDATED_LAST_SEEN_AT);
        BookFileDTO bookFileDTO = bookFileMapper.toDto(updatedBookFile);

        restBookFileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bookFileDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(bookFileDTO))
            )
            .andExpect(status().isOk());

        // Validate the BookFile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedBookFileToMatchAllProperties(updatedBookFile);
    }

    @Test
    @Transactional
    void putNonExistingBookFile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bookFile.setId(longCount.incrementAndGet());

        // Create the BookFile
        BookFileDTO bookFileDTO = bookFileMapper.toDto(bookFile);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBookFileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bookFileDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(bookFileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BookFile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBookFile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bookFile.setId(longCount.incrementAndGet());

        // Create the BookFile
        BookFileDTO bookFileDTO = bookFileMapper.toDto(bookFile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookFileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(bookFileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BookFile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBookFile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bookFile.setId(longCount.incrementAndGet());

        // Create the BookFile
        BookFileDTO bookFileDTO = bookFileMapper.toDto(bookFile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookFileMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookFileDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BookFile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBookFileWithPatch() throws Exception {
        // Initialize the database
        insertedBookFile = bookFileRepository.saveAndFlush(bookFile);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bookFile using partial update
        BookFile partialUpdatedBookFile = new BookFile();
        partialUpdatedBookFile.setId(bookFile.getId());

        partialUpdatedBookFile
            .pathNorm(UPDATED_PATH_NORM)
            .sha256(UPDATED_SHA_256)
            .storageUri(UPDATED_STORAGE_URI)
            .firstSeenAt(UPDATED_FIRST_SEEN_AT);

        restBookFileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBookFile.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBookFile))
            )
            .andExpect(status().isOk());

        // Validate the BookFile in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBookFileUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedBookFile, bookFile), getPersistedBookFile(bookFile));
    }

    @Test
    @Transactional
    void fullUpdateBookFileWithPatch() throws Exception {
        // Initialize the database
        insertedBookFile = bookFileRepository.saveAndFlush(bookFile);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bookFile using partial update
        BookFile partialUpdatedBookFile = new BookFile();
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

        // Validate the BookFile in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBookFileUpdatableFieldsEquals(partialUpdatedBookFile, getPersistedBookFile(partialUpdatedBookFile));
    }

    @Test
    @Transactional
    void patchNonExistingBookFile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bookFile.setId(longCount.incrementAndGet());

        // Create the BookFile
        BookFileDTO bookFileDTO = bookFileMapper.toDto(bookFile);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBookFileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, bookFileDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(bookFileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BookFile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBookFile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bookFile.setId(longCount.incrementAndGet());

        // Create the BookFile
        BookFileDTO bookFileDTO = bookFileMapper.toDto(bookFile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookFileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(bookFileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BookFile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBookFile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bookFile.setId(longCount.incrementAndGet());

        // Create the BookFile
        BookFileDTO bookFileDTO = bookFileMapper.toDto(bookFile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookFileMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(bookFileDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BookFile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBookFile() throws Exception {
        // Initialize the database
        insertedBookFile = bookFileRepository.saveAndFlush(bookFile);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the bookFile
        restBookFileMockMvc
            .perform(delete(ENTITY_API_URL_ID, bookFile.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return bookFileRepository.count();
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

    protected BookFile getPersistedBookFile(BookFile bookFile) {
        return bookFileRepository.findById(bookFile.getId()).orElseThrow();
    }

    protected void assertPersistedBookFileToMatchAllProperties(BookFile expectedBookFile) {
        assertBookFileAllPropertiesEquals(expectedBookFile, getPersistedBookFile(expectedBookFile));
    }

    protected void assertPersistedBookFileToMatchUpdatableProperties(BookFile expectedBookFile) {
        assertBookFileAllUpdatablePropertiesEquals(expectedBookFile, getPersistedBookFile(expectedBookFile));
    }
}
