package com.mycompany.bookstore.repository;

import com.mycompany.bookstore.domain.Authority;
import com.mycompany.bookstore.domain.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data JPA repository for the {@link Book} entity.
 */
@Repository
public interface BookRepository extends JpaRepository<Book, String> {
    public Page<Book> findAllByActiveTrue(Pageable pageable);

    public Optional<Book> findByIsbnAndActiveTrue(String isbn);

    public Optional<Book> findByIsbn(String isbn);
}
