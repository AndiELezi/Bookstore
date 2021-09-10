package com.mycompany.bookstore.service;

import com.mycompany.bookstore.IntegrationTest;
import com.mycompany.bookstore.domain.Book;
import com.mycompany.bookstore.domain.BookCategory;
import com.mycompany.bookstore.repository.BookCategoryRepository;
import com.mycompany.bookstore.repository.BookRepository;
import com.mycompany.bookstore.service.mapper.BookMapper;
import com.mycompany.bookstore.web.rest.vm.BooksOnCategoryVM;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import java.sql.Date;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * Unit tests for {@link BookCategoryService}.
 */
@IntegrationTest
@Transactional
public class BookCategoryServiceIT {
    private static final int NUMBER_OF_CATEGORIES = 4;
    private static final boolean DEFAULT_ACTIVE = true;
    private static final Long DEFAULT_CATEGORY_ID = 1L;
    private static final String DEFAULT_CATEGORY_NAME = "Action";
    private static final String DEFAULT_ISBN = "ISBN123456789";
    private static final String DEFAULT_TITLE = "A test book";
    private static final Float DEFAULT_PRICE = 10.0F;
    private static final String DEFAULT_DESCRIPTION = "A test book description";
    Book book;
    @Autowired
    BookCategoryRepository bookCategoryRepository;
    @Autowired
    BookCategoryService bookCategoryService;
    @Autowired
    BookRepository bookRepository;

    @BeforeEach
    public void init() {
        book = new Book();
        book.setIsbn(DEFAULT_ISBN);
        book.setTitle(DEFAULT_TITLE);
        book.setPrice(DEFAULT_PRICE);
        book.setDescription(DEFAULT_DESCRIPTION);
        book.setPublicationDate(Date.valueOf("1998-12-06"));
        book.setActive(DEFAULT_ACTIVE);
        BookCategory bookCategory = bookCategoryRepository.findById(DEFAULT_CATEGORY_ID).orElse(new BookCategory());
        assertThat(bookCategory.getId()).isEqualTo(DEFAULT_CATEGORY_ID);
        book.setCategory(bookCategory);
    }

    @Test
    @Transactional
    public void TestNumberOfBooksInCategories() {
        bookRepository.saveAndFlush(book);
        Pageable pageable = PageRequest.of(0, 4);
        assertThat(bookCategoryService
            .getAllBookCategoriesWithBooks(pageable)
            .stream()
            .filter(
                booksOnCategoryVM -> booksOnCategoryVM
                    .getCategoryName()
                    .compareTo("Action") == 0)
            .findFirst()
            .orElse(new BooksOnCategoryVM()).getNumberOfBooks())
            .isNotNull().isEqualTo(1);
        bookRepository.delete(book);
    }

    @Test
    @Transactional
    public void TestDeleteByCategory() {
        bookRepository.saveAndFlush(book);
        Pageable pageable = PageRequest.of(0, 4);
        assertThat(bookRepository.findAllByActiveTrue(pageable)
            .get()
            .collect(Collectors.toList())
            .size())
            .isEqualTo(1);
        bookCategoryService.deleteByCategory(DEFAULT_CATEGORY_ID);
        assertThat(bookRepository.findAllByActiveTrue(pageable)
            .get()
            .collect(Collectors.toList())
            .size())
            .isEqualTo(0);
    }
}
