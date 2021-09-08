package com.mycompany.bookstore.web.rest;

import com.mycompany.bookstore.domain.Book;
import com.mycompany.bookstore.service.BookService;
import com.mycompany.bookstore.service.dto.BookDTO;
import com.mycompany.bookstore.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * Rest controller for managing books
 */
@RestController
@RequestMapping("/api")
public class BookResource {
    private final Logger log = LoggerFactory.getLogger(BookResource.class);

    private final BookService bookService;


    public BookResource(BookService bookService) {
        this.bookService = bookService;
    }


    /**
     * {@code GET  /api/books/:isbn}  : Gets a book by its isbn
     *
     * @param isbn the isbn of the book to be returned.
     * @return the {@link ResponseEntity} with status {@code 200 (ok)} and with body the book, or with status {@code 404 (Not Found)} if the login or email is already in use.
     */
    @GetMapping("/books/{isbn}")
    public ResponseEntity<Book> getBookByIsbn(@PathVariable String isbn) {
        Optional<Book> book = bookService.getBook(isbn);
        if (book.isPresent()) {
            return new ResponseEntity<Book>(book.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * {@code GET  /api/books}  : Gets a list of all the books
     *
     * @param pageable the information of the pages and size of the pages .
     * @return the {@link ResponseEntity} with status {@code 200 (ok)} and with a list of books.
     */
    @GetMapping("/books")
    public ResponseEntity<List<BookDTO>> getAllBooks(Pageable pageable) {
        final Page<BookDTO> page = bookService.getAllBooks(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * {@code POST  /api/books}  : Creates a book
     *
     * @param book The book to be created.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the created book, or with status {@code 409 (Conflict)} if the book isbn is already registered.
     */
    @PostMapping("/books")
    public ResponseEntity<Book> addBook(@Valid @RequestBody Book book) {
        Optional<Book> checkBook = bookService.getBook(book.getIsbn());
        if (checkBook.isPresent()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(bookService.createBook(book), HttpStatus.CREATED);
    }
}
