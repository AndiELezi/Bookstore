package com.mycompany.bookstore.web.rest;

import com.mycompany.bookstore.IntegrationTest;
import com.mycompany.bookstore.domain.Book;
import com.mycompany.bookstore.domain.BookCategory;
import com.mycompany.bookstore.repository.BookRepository;
import com.mycompany.bookstore.security.AuthoritiesConstants;
import com.mycompany.bookstore.service.dto.BookDTO;
import com.mycompany.bookstore.service.mapper.BookMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * Unit tests for {@link BookResource}.
 */

@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser(authorities = AuthoritiesConstants.ADMIN)
public class BookResourceIT {
    private static final boolean DEFAULT_ACTIVE = true;
    private static final Long DEFAULT_CATEGORY_ID = 1L;
    private static final String DEFAULT_CATEGORY_NAME = "Action";
    private static final String DEFAULT_ISBN = "ISBN123456789";
    private static final String DEFAULT_TITLE = "A test book";
    private static final Float DEFAULT_PRICE = 10.0F;
    private static final String DEFAULT_DESCRIPTION = "A test book description";
    private static final Date DEFAULT_PUBLICATION_DATE = Date.valueOf("1998-12-06");

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    BookRepository bookRepository;

    private BookMapper bookMapper;
    private Book book;
    private BookDTO bookDTO;


    @BeforeEach
    public void init() {
        bookMapper = new BookMapper();
        book = new Book();
        book.setIsbn(DEFAULT_ISBN);
        book.setTitle(DEFAULT_TITLE);
        book.setPrice(DEFAULT_PRICE);
        book.setDescription(DEFAULT_DESCRIPTION);
        book.setPublicationDate(DEFAULT_PUBLICATION_DATE);
        book.setActive(DEFAULT_ACTIVE);
        book.setCreatedBy("Admin");
        book.setCreatedDate(new java.util.Date(System.currentTimeMillis()).toInstant());
        book.setLastModifiedBy("Admin");
        book.setLastModifiedDate(new java.util.Date(System.currentTimeMillis()).toInstant());
        BookCategory bookCategory = new BookCategory();
        bookCategory.setId(DEFAULT_CATEGORY_ID);
        bookCategory.setName(DEFAULT_CATEGORY_NAME);
        book.setCategory(bookCategory);
        bookDTO = new BookDTO(book);
    }

    @Test
    @Transactional
    public void TestCreateBook() throws Exception {
        int databaseSizeBeforeCreate = bookRepository.findAll().size();
        mockMvc
            .perform(
                post("/api/books").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bookDTO))
            )
            .andExpect(status().isCreated());

        assertPersistedBooks(
            books -> {
                assertThat(books).hasSize(databaseSizeBeforeCreate + 1);
                Book testBook = books.get(books.size() - 1);
                assertThat(testBook.getIsbn()).isEqualTo(DEFAULT_ISBN);
                assertThat(testBook.getTitle()).isEqualTo(DEFAULT_TITLE);
                assertThat(testBook.getPrice()).isEqualTo(DEFAULT_PRICE);
                assertThat(testBook.getPublicationDate()).isEqualTo(DEFAULT_PUBLICATION_DATE);
            }
        );

    }

    @Test
    @Transactional
    public void TestCreateBookWithSameIsbn() throws Exception {
        bookRepository.saveAndFlush(book);
        int databaseSizeBeforeCreate = bookRepository.findAll().size();
        mockMvc
            .perform(
                post("/api/books").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bookDTO))
            )
            .andExpect(status().isConflict());

        assertPersistedBooks(
            books -> {
                assertThat(books).hasSize(databaseSizeBeforeCreate);
            }
        );

    }

    @Test
    @Transactional
    void getAllBooks() throws Exception {
        bookRepository.saveAndFlush(book);
        mockMvc
            .perform(get("/api/books?sort=isbn,desc").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].isbn").value(hasItem(DEFAULT_ISBN)))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].price").value(hasItem((double) DEFAULT_PRICE)));


    }

    private void assertPersistedBooks(Consumer<List<Book>> bookAssertion) {
        bookAssertion.accept(bookRepository.findAll());
    }
}
