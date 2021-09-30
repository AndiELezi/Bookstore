package com.mycompany.bookstore.web.rest;

import com.mycompany.bookstore.service.BookSeriesService;
import com.mycompany.bookstore.service.dto.BookSeriesDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

/**
 * Rest controller for managing books series
 */

@RestController
@RequestMapping("/api")
public class BookSeriesResource {
    final private BookSeriesService bookSeriesService;

    public BookSeriesResource(BookSeriesService bookSeriesService) {
        this.bookSeriesService = bookSeriesService;
    }


    /**
     * {@code GET  /api/bookSeries}  : Gets all book series
     *
     * @return the {@link ResponseEntity} with status {@code 200 (ok)} and with the bookSeries with average rating.
     */

    @GetMapping("/bookSeries")
    public ResponseEntity<List<BookSeriesDTO>> getAllBookSeries(Pageable pageable) {
        Page<BookSeriesDTO> bookSeriesDTOS = bookSeriesService.getAllBookSeries(pageable);
        return new ResponseEntity<>(bookSeriesDTOS.getContent(), HttpStatus.OK);
    }

    /**
     * {@code GET  /api/bookSeries/:id}  : Gets a book series by its isbn
     *
     * @param id the id of the book series to be returned.
     * @return the {@link ResponseEntity} with status {@code 200 (ok)} and with body the book series, or with status {@code 404 (Not Found)} if the login or email is already in use.
     */
    @GetMapping("/bookSeries/{id}")
    public ResponseEntity<BookSeriesDTO> getBookSeries(@PathVariable Long id) {
        Optional<BookSeriesDTO> bookSeriesDTOOptional = bookSeriesService.findBookSeries(id);
        if (!bookSeriesDTOOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(bookSeriesDTOOptional.get(), HttpStatus.OK);
        }
    }
}
