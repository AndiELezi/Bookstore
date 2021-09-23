package com.mycompany.bookstore.repository;

import com.mycompany.bookstore.domain.BookSeries;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the {@link BookSeries} entity.
 */

@Repository
public interface BookSeriesRepository extends JpaRepository<BookSeries, Long> {
}
