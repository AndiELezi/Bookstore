package com.mycompany.bookstore.repository;

import com.mycompany.bookstore.domain.Book;
import com.mycompany.bookstore.domain.Rent;
import com.mycompany.bookstore.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the {@link Rent} entity.
 */

@Repository
public interface RentRepository extends JpaRepository<Rent, Long> {
    Page<Rent> findAllByReturnedIsFalse(Pageable pageable);

    List<Rent> findAllByReturnedIsFalse();

    Optional<Rent> findByBookAndReturnedIsFalse(Book book);

    Optional<Rent> findByUserAndAndBook(User user, Book book);

    List<Rent> findAllByUserAndReturnedIsFalse(User user);


}
