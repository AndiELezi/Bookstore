package com.mycompany.bookstore.service;

import com.mycompany.bookstore.IntegrationTest;
import com.mycompany.bookstore.domain.Book;
import com.mycompany.bookstore.domain.BookCategory;
import com.mycompany.bookstore.repository.BookRepository;
import com.mycompany.bookstore.service.dto.BookDTO;
import com.mycompany.bookstore.service.mapper.BookMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.sql.Date;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link BookService}.
 */

@IntegrationTest
@Transactional
public class BookServiceIT {
    private static final boolean DEFAULT_ACTIVE = true;
    private static final Long DEFAULT_CATEGORY_ID = 1L;
    private static final String DEFAULT_CATEGORY_NAME = "Action";
    private static final String DEFAULT_ISBN = "ISBN123456789";
    private static final String DEFAULT_TITLE = "A test book";
    private static final Float DEFAULT_PRICE = 10.0F;
    private static final String DEFAULT_DESCRIPTION = "A test book description";

    @Autowired
    BookRepository bookRepository;

    @Autowired
    BookService bookService;

    @Autowired
    BookMapper bookMapper;

    Book book;

    BookDTO bookDTO;

    @BeforeEach
    public void init() {
        book = new Book();
        book.setIsbn(DEFAULT_ISBN);
        book.setTitle(DEFAULT_TITLE);
        book.setPrice(DEFAULT_PRICE);
        book.setDescription(DEFAULT_DESCRIPTION);
        book.setPublicationDate(Date.valueOf("1998-12-06"));
        book.setActive(DEFAULT_ACTIVE);
        BookCategory bookCategory = new BookCategory();
        bookCategory.setId(DEFAULT_CATEGORY_ID);
        bookCategory.setName(DEFAULT_CATEGORY_NAME);
        book.setCategory(bookCategory);
        bookDTO = new BookDTO(book);
    }

    @Test
    @Transactional
    public void TestBookCreation() {
        bookService.createBook(bookDTO);
        assertThat(bookRepository.findByIsbnAndActiveTrue(bookDTO.getIsbn())).isPresent();
        bookRepository.deleteById(bookDTO.getIsbn());
    }

    @Test
    @Transactional
    public void TestGetBookByIsbn() {
        bookService.createBook(bookDTO);
        assertThat(bookService.getBook(bookDTO.getIsbn()).getIsbn()).isNotNull();
        bookRepository.deleteById(bookDTO.getIsbn());
    }
}
