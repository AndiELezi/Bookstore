package com.mycompany.bookstore.service;

import com.mycompany.bookstore.repository.BookSeriesRepository;
import com.mycompany.bookstore.service.dto.BookSeriesDTO;
import com.mycompany.bookstore.web.rest.vm.BooksOnCategoryVM;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class BookSeriesService {
    final private BookSeriesRepository bookSeriesRepository;

    public BookSeriesService(BookSeriesRepository bookSeriesRepository) {
        this.bookSeriesRepository = bookSeriesRepository;
    }

    public Page<BookSeriesDTO> getAllBookSeries(Pageable pageable) {
        return bookSeriesRepository.findAll(pageable).map(BookSeriesDTO::new);
    }

    public Optional<BookSeriesDTO> findBookSeries(Long bookSeriesId) {
        return bookSeriesRepository.findById(bookSeriesId).map(BookSeriesDTO::new);
    }

}
