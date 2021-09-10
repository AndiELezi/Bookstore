package com.mycompany.bookstore.service.mapper;

import com.mycompany.bookstore.domain.Book;
import com.mycompany.bookstore.domain.BookCategory;
import com.mycompany.bookstore.service.dto.BookDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link BookMapper}.
 */
class BookMapperTest {

    private static final boolean DEFAULT_ACTIVE = true;
    private static final Long DEFAULT_CATEGORY_ID = 1L;
    private static final String DEFAULT_CATEGORY_NAME = "Action";
    private static final String DEFAULT_ISBN = "ISBN123456789";
    private static final String DEFAULT_TITLE = "A test book";
    private static final Float DEFAULT_PRICE = 10.0F;
    private static final String DEFAULT_DESCRIPTION = "A test book description";

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
        book.setPublicationDate(Date.valueOf("1998-12-06"));
        book.setActive(DEFAULT_ACTIVE);
        book.setCreatedBy("User");
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
    void booksToBookDTOsShouldMapOnlyNonNullBooks() {
        List<Book> books = new ArrayList<>();
        books.add(book);
        books.add(null);

        List<BookDTO> bookDTOS = bookMapper.booksToBookDTOs(books);

        assertThat(bookDTOS).isNotEmpty().size().isEqualTo(1);
    }


    @Test
    void testBookFromIsbn() {
        assertThat(bookMapper.bookFromIsbn(DEFAULT_ISBN).getIsbn()).isEqualTo(DEFAULT_ISBN);
        assertThat(bookMapper.bookFromIsbn(null)).isNull();
    }
}
