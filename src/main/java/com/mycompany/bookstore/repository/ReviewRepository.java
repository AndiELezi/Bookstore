package com.mycompany.bookstore.repository;

import com.mycompany.bookstore.domain.Book;
import com.mycompany.bookstore.domain.Review;
import com.mycompany.bookstore.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Optional<Review> findFirstByUserAndAndBook(User user, Book book);

    List<Review> findByCreationDateAfter(Date date);
}
