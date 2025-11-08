package com.example.books.web.rest;

import static com.example.books.domain.BookPageTextAsserts.*;
import static com.example.books.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.books.IntegrationTest;
import com.example.books.domain.BookPageText;
import com.example.books.repository.BookPageTextRepository;
import com.example.books.service.BookPageTextService;
import com.example.books.service.dto.BookPageTextDTO;
import com.example.books.service.mapper.BookPageTextMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link BookPageTextResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class BookPageTextResourceIT {

    private static final String DEFAULT_DOCUMENT_ID = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENT_ID = "BBBBBBBBBB";

    private static final Integer DEFAULT_PAGE_NO = 1;
    private static final Integer UPDATED_PAGE_NO = 2;

    private static final String DEFAULT_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_TEXT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/book-page-texts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private BookPageTextRepository bookPageTextRepository;

    @Mock
    private BookPageTextRepository bookPageTextRepositoryMock;

    @Autowired
    private BookPageTextMapper bookPageTextMapper;

    @Mock
    private BookPageTextService bookPageTextServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBookPageTextMockMvc;

    private BookPageText bookPageText;

    private BookPageText insertedBookPageText;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BookPageText createEntity() {
        return new BookPageText().documentId(DEFAULT_DOCUMENT_ID).pageNo(DEFAULT_PAGE_NO).text(DEFAULT_TEXT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BookPageText createUpdatedEntity() {
        return new BookPageText().documentId(UPDATED_DOCUMENT_ID).pageNo(UPDATED_PAGE_NO).text(UPDATED_TEXT);
    }

    @BeforeEach
    void initTest() {
        bookPageText = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedBookPageText != null) {
            bookPageTextRepository.delete(insertedBookPageText);
            insertedBookPageText = null;
        }
    }

    @Test
    @Transactional
    void createBookPageText() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the BookPageText
        BookPageTextDTO bookPageTextDTO = bookPageTextMapper.toDto(bookPageText);
        var returnedBookPageTextDTO = om.readValue(
            restBookPageTextMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookPageTextDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            BookPageTextDTO.class
        );

        // Validate the BookPageText in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedBookPageText = bookPageTextMapper.toEntity(returnedBookPageTextDTO);
        assertBookPageTextUpdatableFieldsEquals(returnedBookPageText, getPersistedBookPageText(returnedBookPageText));

        insertedBookPageText = returnedBookPageText;
    }

    @Test
    @Transactional
    void createBookPageTextWithExistingId() throws Exception {
        // Create the BookPageText with an existing ID
        bookPageText.setId(1L);
        BookPageTextDTO bookPageTextDTO = bookPageTextMapper.toDto(bookPageText);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBookPageTextMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookPageTextDTO)))
            .andExpect(status().isBadRequest());

        // Validate the BookPageText in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDocumentIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        bookPageText.setDocumentId(null);

        // Create the BookPageText, which fails.
        BookPageTextDTO bookPageTextDTO = bookPageTextMapper.toDto(bookPageText);

        restBookPageTextMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookPageTextDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPageNoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        bookPageText.setPageNo(null);

        // Create the BookPageText, which fails.
        BookPageTextDTO bookPageTextDTO = bookPageTextMapper.toDto(bookPageText);

        restBookPageTextMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookPageTextDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBookPageTexts() throws Exception {
        // Initialize the database
        insertedBookPageText = bookPageTextRepository.saveAndFlush(bookPageText);

        // Get all the bookPageTextList
        restBookPageTextMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bookPageText.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentId").value(hasItem(DEFAULT_DOCUMENT_ID)))
            .andExpect(jsonPath("$.[*].pageNo").value(hasItem(DEFAULT_PAGE_NO)))
            .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBookPageTextsWithEagerRelationshipsIsEnabled() throws Exception {
        when(bookPageTextServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBookPageTextMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(bookPageTextServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBookPageTextsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(bookPageTextServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBookPageTextMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(bookPageTextRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getBookPageText() throws Exception {
        // Initialize the database
        insertedBookPageText = bookPageTextRepository.saveAndFlush(bookPageText);

        // Get the bookPageText
        restBookPageTextMockMvc
            .perform(get(ENTITY_API_URL_ID, bookPageText.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(bookPageText.getId().intValue()))
            .andExpect(jsonPath("$.documentId").value(DEFAULT_DOCUMENT_ID))
            .andExpect(jsonPath("$.pageNo").value(DEFAULT_PAGE_NO))
            .andExpect(jsonPath("$.text").value(DEFAULT_TEXT));
    }

    @Test
    @Transactional
    void getNonExistingBookPageText() throws Exception {
        // Get the bookPageText
        restBookPageTextMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBookPageText() throws Exception {
        // Initialize the database
        insertedBookPageText = bookPageTextRepository.saveAndFlush(bookPageText);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bookPageText
        BookPageText updatedBookPageText = bookPageTextRepository.findById(bookPageText.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedBookPageText are not directly saved in db
        em.detach(updatedBookPageText);
        updatedBookPageText.documentId(UPDATED_DOCUMENT_ID).pageNo(UPDATED_PAGE_NO).text(UPDATED_TEXT);
        BookPageTextDTO bookPageTextDTO = bookPageTextMapper.toDto(updatedBookPageText);

        restBookPageTextMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bookPageTextDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(bookPageTextDTO))
            )
            .andExpect(status().isOk());

        // Validate the BookPageText in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedBookPageTextToMatchAllProperties(updatedBookPageText);
    }

    @Test
    @Transactional
    void putNonExistingBookPageText() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bookPageText.setId(longCount.incrementAndGet());

        // Create the BookPageText
        BookPageTextDTO bookPageTextDTO = bookPageTextMapper.toDto(bookPageText);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBookPageTextMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bookPageTextDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(bookPageTextDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BookPageText in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBookPageText() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bookPageText.setId(longCount.incrementAndGet());

        // Create the BookPageText
        BookPageTextDTO bookPageTextDTO = bookPageTextMapper.toDto(bookPageText);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookPageTextMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(bookPageTextDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BookPageText in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBookPageText() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bookPageText.setId(longCount.incrementAndGet());

        // Create the BookPageText
        BookPageTextDTO bookPageTextDTO = bookPageTextMapper.toDto(bookPageText);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookPageTextMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookPageTextDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BookPageText in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBookPageTextWithPatch() throws Exception {
        // Initialize the database
        insertedBookPageText = bookPageTextRepository.saveAndFlush(bookPageText);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bookPageText using partial update
        BookPageText partialUpdatedBookPageText = new BookPageText();
        partialUpdatedBookPageText.setId(bookPageText.getId());

        partialUpdatedBookPageText.documentId(UPDATED_DOCUMENT_ID).pageNo(UPDATED_PAGE_NO).text(UPDATED_TEXT);

        restBookPageTextMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBookPageText.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBookPageText))
            )
            .andExpect(status().isOk());

        // Validate the BookPageText in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBookPageTextUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedBookPageText, bookPageText),
            getPersistedBookPageText(bookPageText)
        );
    }

    @Test
    @Transactional
    void fullUpdateBookPageTextWithPatch() throws Exception {
        // Initialize the database
        insertedBookPageText = bookPageTextRepository.saveAndFlush(bookPageText);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bookPageText using partial update
        BookPageText partialUpdatedBookPageText = new BookPageText();
        partialUpdatedBookPageText.setId(bookPageText.getId());

        partialUpdatedBookPageText.documentId(UPDATED_DOCUMENT_ID).pageNo(UPDATED_PAGE_NO).text(UPDATED_TEXT);

        restBookPageTextMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBookPageText.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBookPageText))
            )
            .andExpect(status().isOk());

        // Validate the BookPageText in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBookPageTextUpdatableFieldsEquals(partialUpdatedBookPageText, getPersistedBookPageText(partialUpdatedBookPageText));
    }

    @Test
    @Transactional
    void patchNonExistingBookPageText() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bookPageText.setId(longCount.incrementAndGet());

        // Create the BookPageText
        BookPageTextDTO bookPageTextDTO = bookPageTextMapper.toDto(bookPageText);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBookPageTextMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, bookPageTextDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(bookPageTextDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BookPageText in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBookPageText() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bookPageText.setId(longCount.incrementAndGet());

        // Create the BookPageText
        BookPageTextDTO bookPageTextDTO = bookPageTextMapper.toDto(bookPageText);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookPageTextMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(bookPageTextDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BookPageText in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBookPageText() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bookPageText.setId(longCount.incrementAndGet());

        // Create the BookPageText
        BookPageTextDTO bookPageTextDTO = bookPageTextMapper.toDto(bookPageText);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookPageTextMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(bookPageTextDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BookPageText in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBookPageText() throws Exception {
        // Initialize the database
        insertedBookPageText = bookPageTextRepository.saveAndFlush(bookPageText);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the bookPageText
        restBookPageTextMockMvc
            .perform(delete(ENTITY_API_URL_ID, bookPageText.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return bookPageTextRepository.count();
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

    protected BookPageText getPersistedBookPageText(BookPageText bookPageText) {
        return bookPageTextRepository.findById(bookPageText.getId()).orElseThrow();
    }

    protected void assertPersistedBookPageTextToMatchAllProperties(BookPageText expectedBookPageText) {
        assertBookPageTextAllPropertiesEquals(expectedBookPageText, getPersistedBookPageText(expectedBookPageText));
    }

    protected void assertPersistedBookPageTextToMatchUpdatableProperties(BookPageText expectedBookPageText) {
        assertBookPageTextAllUpdatablePropertiesEquals(expectedBookPageText, getPersistedBookPageText(expectedBookPageText));
    }
}
