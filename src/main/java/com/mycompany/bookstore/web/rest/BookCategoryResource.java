package com.mycompany.bookstore.web.rest;

import com.mycompany.bookstore.service.BookCategoryService;
import com.mycompany.bookstore.service.dto.BookCategoryDTO;
import com.mycompany.bookstore.web.rest.vm.BooksOnCategoryVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;

import java.util.List;

/**
 * Rest controller for managing book categories
 */

@RestController
@RequestMapping("/api")
public class BookCategoryResource {
    private final Logger log = LoggerFactory.getLogger(BookCategoryResource.class);
    private final BookCategoryService bookCategoryService;
    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public BookCategoryResource(BookCategoryService bookCategoryService) {
        this.bookCategoryService = bookCategoryService;
    }


    /**
     * {@code GET  /api/BookCategories}  : Gets a list of all the book categories
     *
     * @param pageable the information of the pages and size of the pages .
     * @return the {@link ResponseEntity} with status {@code 200 (ok)} and with a list of book categories.
     */

    @GetMapping("/BookCategories")
    public ResponseEntity<List<BookCategoryDTO>> getAllCategories(Pageable pageable) {
        final Page<BookCategoryDTO> page = bookCategoryService.getAllBookCategories(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


    /**
     * {@code GET  /api/BookCategoriesNumber}  : Gets a list of all the book categories with number of books it has
     *
     * @param pageable the information of the pages and size of the pages .
     * @return the {@link ResponseEntity} with status {@code 200 (ok)} and with a list of book categories with number of books.
     */

    @GetMapping("/BookCategoriesNumber")
    public ResponseEntity<List<BooksOnCategoryVM>> getAllCategoriesWithNrOfBooks(Pageable pageable) {
        final Page<BooksOnCategoryVM> page = bookCategoryService.getAllBookCategoriesWithBooks(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


    /**
     * {@code DELETE  /api/BookCategory/:categoryId}  : Deletes all book of a category
     *
     * @param categoryId the category of the book to be returned.
     * @return the {@link ResponseEntity} with status {@code 204 (no content)}.
     */

    @DeleteMapping("/BookCategory/{categoryId}")
    public ResponseEntity<Void> deleteBooksByCategory(@PathVariable Long categoryId) {
        bookCategoryService.deleteByCategory(categoryId);
        return ResponseEntity.noContent().headers(HeaderUtil.createAlert(applicationName, "Books deleted", categoryId.toString())).build();
    }
}
