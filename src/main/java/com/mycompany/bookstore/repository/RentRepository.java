package com.mycompany.bookstore.repository;

import com.mycompany.bookstore.domain.Rent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the {@link Rent} entity.
 */

@Repository
public interface RentRepository extends JpaRepository<Rent, Long> {
    Page<Rent> findAllByReturnedIsFalse(Pageable pageable);
}
