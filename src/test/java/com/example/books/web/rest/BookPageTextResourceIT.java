package com.example.books.web.rest;

import static com.example.books.domain.BookPageTextAsserts.*;
import static com.example.books.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.books.IntegrationTest;
import com.example.books.adapter.web.rest.dto.BookPageTextDTO;
import com.example.books.adapter.web.rest.mapper.BookPageTextRestMapper;
import com.example.books.domain.core.bookpagetext.BookPageText;
import com.example.books.infrastructure.database.jpa.entity.BookEntity;
import com.example.books.infrastructure.database.jpa.entity.BookPageTextEntity;
import com.example.books.infrastructure.database.jpa.mapper.BookPageTextEntityMapper;
import com.example.books.infrastructure.database.jpa.repository.BookPageTextJpaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link BookPageTextResource} REST controller.
 */
@IntegrationTest
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

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private BookPageTextJpaRepository bookPageTextJpaRepository;

    @Autowired
    private BookPageTextEntityMapper bookPageTextEntityMapper;

    @Autowired
    private BookPageTextRestMapper bookPageTextRestMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBookPageTextMockMvc;

    private BookPageTextEntity bookPageText;

    private BookPageTextEntity insertedBookPageText;

    public static BookPageTextEntity createEntity(EntityManager em) {
        BookPageTextEntity bookPageText = new BookPageTextEntity()
            .documentId(DEFAULT_DOCUMENT_ID)
            .pageNo(DEFAULT_PAGE_NO)
            .text(DEFAULT_TEXT);
        bookPageText.setBook(getOrCreateBook(em));
        return bookPageText;
    }

    public static BookPageTextEntity createUpdatedEntity(EntityManager em) {
        BookPageTextEntity bookPageText = new BookPageTextEntity()
            .documentId(UPDATED_DOCUMENT_ID)
            .pageNo(UPDATED_PAGE_NO)
            .text(UPDATED_TEXT);
        bookPageText.setBook(getOrCreateBook(em));
        return bookPageText;
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
        bookPageText = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedBookPageText != null) {
            bookPageTextJpaRepository.delete(insertedBookPageText);
            insertedBookPageText = null;
        }
    }

    @Test
    @Transactional
    void createBookPageText() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        BookPageTextDTO bookPageTextDTO = toDto(bookPageText);
        BookPageTextDTO returnedBookPageTextDTO = om.readValue(
            restBookPageTextMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookPageTextDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            BookPageTextDTO.class
        );

        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        BookPageText returnedBookPageText = bookPageTextRestMapper.toDomain(returnedBookPageTextDTO);
        assertBookPageTextUpdatableFieldsEquals(returnedBookPageText, getPersistedBookPageText(returnedBookPageText.id()));
        insertedBookPageText = bookPageTextJpaRepository.findById(returnedBookPageText.id()).orElseThrow();
    }

    @Test
    @Transactional
    void createBookPageTextWithExistingId() throws Exception {
        bookPageText.setId(1L);
        BookPageTextDTO bookPageTextDTO = toDto(bookPageText);

        long databaseSizeBeforeCreate = getRepositoryCount();

        restBookPageTextMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookPageTextDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDocumentIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        bookPageText.setDocumentId(null);

        BookPageTextDTO bookPageTextDTO = toDto(bookPageText);

        restBookPageTextMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookPageTextDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPageNoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        bookPageText.setPageNo(null);
        BookPageTextDTO bookPageTextDTO = toDto(bookPageText);

        restBookPageTextMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookPageTextDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBookPageTexts() throws Exception {
        insertedBookPageText = bookPageTextJpaRepository.saveAndFlush(bookPageText);

        restBookPageTextMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bookPageText.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentId").value(hasItem(DEFAULT_DOCUMENT_ID)))
            .andExpect(jsonPath("$.[*].pageNo").value(hasItem(DEFAULT_PAGE_NO)))
            .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT)));
    }

    @Test
    @Transactional
    void getBookPageText() throws Exception {
        insertedBookPageText = bookPageTextJpaRepository.saveAndFlush(bookPageText);

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
        restBookPageTextMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBookPageText() throws Exception {
        insertedBookPageText = bookPageTextJpaRepository.saveAndFlush(bookPageText);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        BookPageTextEntity updatedBookPageText = bookPageTextJpaRepository.findById(bookPageText.getId()).orElseThrow();
        em.detach(updatedBookPageText);
        updatedBookPageText.documentId(UPDATED_DOCUMENT_ID).pageNo(UPDATED_PAGE_NO).text(UPDATED_TEXT);
        BookPageTextDTO bookPageTextDTO = toDto(updatedBookPageText);

        restBookPageTextMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bookPageTextDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(bookPageTextDTO))
            )
            .andExpect(status().isOk());

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedBookPageTextToMatchAllProperties(updatedBookPageText);
    }

    @Test
    @Transactional
    void putNonExistingBookPageText() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bookPageText.setId(longCount.incrementAndGet());
        BookPageTextDTO bookPageTextDTO = toDto(bookPageText);

        restBookPageTextMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bookPageTextDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(bookPageTextDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBookPageText() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bookPageText.setId(longCount.incrementAndGet());
        BookPageTextDTO bookPageTextDTO = toDto(bookPageText);

        restBookPageTextMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(bookPageTextDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBookPageText() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bookPageText.setId(longCount.incrementAndGet());
        BookPageTextDTO bookPageTextDTO = toDto(bookPageText);

        restBookPageTextMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookPageTextDTO)))
            .andExpect(status().isMethodNotAllowed());

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBookPageTextWithPatch() throws Exception {
        insertedBookPageText = bookPageTextJpaRepository.saveAndFlush(bookPageText);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        BookPageTextEntity partialUpdatedBookPageText = new BookPageTextEntity();
        partialUpdatedBookPageText.setId(bookPageText.getId());
        partialUpdatedBookPageText.pageNo(UPDATED_PAGE_NO);

        restBookPageTextMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBookPageText.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBookPageText))
            )
            .andExpect(status().isOk());

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBookPageTextUpdatableFieldsEquals(
            toDomain(createUpdateProxyForBean(partialUpdatedBookPageText, bookPageText)),
            getPersistedBookPageText(bookPageText.getId())
        );
    }

    @Test
    @Transactional
    void fullUpdateBookPageTextWithPatch() throws Exception {
        insertedBookPageText = bookPageTextJpaRepository.saveAndFlush(bookPageText);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        BookPageTextEntity partialUpdatedBookPageText = new BookPageTextEntity();
        partialUpdatedBookPageText.setId(bookPageText.getId());
        partialUpdatedBookPageText.documentId(UPDATED_DOCUMENT_ID).pageNo(UPDATED_PAGE_NO).text(UPDATED_TEXT);

        restBookPageTextMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBookPageText.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBookPageText))
            )
            .andExpect(status().isOk());

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBookPageTextUpdatableFieldsEquals(
            toDomain(partialUpdatedBookPageText),
            getPersistedBookPageText(partialUpdatedBookPageText.getId())
        );
    }

    @Test
    @Transactional
    void patchNonExistingBookPageText() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bookPageText.setId(longCount.incrementAndGet());
        BookPageTextDTO bookPageTextDTO = toDto(bookPageText);

        restBookPageTextMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, bookPageTextDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(bookPageTextDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBookPageText() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bookPageText.setId(longCount.incrementAndGet());
        BookPageTextDTO bookPageTextDTO = toDto(bookPageText);

        restBookPageTextMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(bookPageTextDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBookPageText() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bookPageText.setId(longCount.incrementAndGet());
        BookPageTextDTO bookPageTextDTO = toDto(bookPageText);

        restBookPageTextMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(bookPageTextDTO)))
            .andExpect(status().isMethodNotAllowed());

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBookPageText() throws Exception {
        insertedBookPageText = bookPageTextJpaRepository.saveAndFlush(bookPageText);

        long databaseSizeBeforeDelete = getRepositoryCount();

        restBookPageTextMockMvc
            .perform(delete(ENTITY_API_URL_ID, bookPageText.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return bookPageTextJpaRepository.count();
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

    protected BookPageText getPersistedBookPageText(Long id) {
        return bookPageTextEntityMapper.toDomain(bookPageTextJpaRepository.findById(id).orElseThrow());
    }

    protected void assertPersistedBookPageTextToMatchAllProperties(BookPageTextEntity expectedBookPageText) {
        assertBookPageTextAllPropertiesEquals(toDomain(expectedBookPageText), getPersistedBookPageText(expectedBookPageText.getId()));
    }

    protected void assertPersistedBookPageTextToMatchUpdatableProperties(BookPageTextEntity expectedBookPageText) {
        assertBookPageTextAllUpdatablePropertiesEquals(
            toDomain(expectedBookPageText),
            getPersistedBookPageText(expectedBookPageText.getId())
        );
    }

    private BookPageTextDTO toDto(BookPageTextEntity entity) {
        return bookPageTextRestMapper.toDto(bookPageTextEntityMapper.toDomain(entity));
    }

    private BookPageText toDomain(BookPageTextEntity entity) {
        return bookPageTextEntityMapper.toDomain(entity);
    }
}
