package com.mycompany.bookstore.repository;

import com.mycompany.bookstore.domain.Authority;
import com.mycompany.bookstore.domain.BookCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the {@link BookCategory} entity.
 */

@Repository
public interface BookCategoryRepository extends JpaRepository<BookCategory,Long> {

}
