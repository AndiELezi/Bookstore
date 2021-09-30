package com.mycompany.bookstore.service;

import com.mycompany.bookstore.domain.Book;
import com.mycompany.bookstore.domain.Rent;
import com.mycompany.bookstore.domain.Review;
import com.mycompany.bookstore.domain.User;
import com.mycompany.bookstore.repository.BookRepository;
import com.mycompany.bookstore.repository.RentRepository;
import com.mycompany.bookstore.repository.ReviewRepository;
import com.mycompany.bookstore.repository.UserRepository;
import com.mycompany.bookstore.security.SecurityUtils;
import com.mycompany.bookstore.service.dto.BookDTO;
import com.mycompany.bookstore.service.dto.ReviewDTO;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Service class for managing Reviews.
 */
@Service
public class ReviewService {
    final private ReviewRepository reviewRepository;
    final private UserRepository userRepository;
    final private BookRepository bookRepository;
    final private RentRepository rentRepository;

    public ReviewService(ReviewRepository reviewRepository, UserRepository userRepository, BookRepository bookRepository, RentRepository rentRepository) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.rentRepository = rentRepository;
    }

    public ReviewDTO addReview(ReviewDTO reviewDTO) {
        Optional<User> user = SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneWithAuthoritiesByLogin);
        Optional<Book> book = bookRepository.findByIsbnAndActiveTrue(reviewDTO.getBookIsbn());
        if (user.isPresent() && book.isPresent()) {
            Optional<Review> checkForExistingReview = reviewRepository.findFirstByUserAndAndBook(user.get(), book.get());
            Review review;
            if (checkForExistingReview.isPresent()) {
                review = checkForExistingReview.get();
            } else {
                review = new Review();
                review.setUser(user.get());
                review.setBook(book.get());
            }
            review.setRating(reviewDTO.getRating());
            review.setCreationDate(new Date(System.currentTimeMillis()));
            return new ReviewDTO(reviewRepository.saveAndFlush(review));

        } else {
            return new ReviewDTO();
        }
    }

    public boolean canUserReview(ReviewDTO reviewDTO) {
        Optional<User> user = SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneWithAuthoritiesByLogin);
        Optional<Book> book = bookRepository.findByIsbn(reviewDTO.getBookIsbn());
        if (!user.isPresent() || !book.isPresent()) {
            return false;
        }
        Optional<Rent> rent = rentRepository.findByUserAndAndBook(user.get(), book.get());
        if (!rent.isPresent()) {
            return false;
        }
        Date currentDate = new Date(System.currentTimeMillis());
        if (rent.get().getRentDate().compareTo(currentDate) > 0) {
            return false;
        }
        return true;

    }

    public List<BookDTO> mostRatedBooksOfWeek() {
        Long daysToMil = TimeUnit.DAYS.toMillis(7);
        Long timeBeforeOneWeek = System.currentTimeMillis() - daysToMil;
        Map<String, Double> bookDTODoubleMap = reviewRepository.findByCreationDateAfter(new Date(timeBeforeOneWeek))
            .stream()
            .collect(
                Collectors.groupingBy(
                    review -> review.getBook().getIsbn(), Collectors.averagingDouble(review -> review.getRating())
                )
            );
        List<BookDTO> bookDTOS = new ArrayList<>();
        for (String bookKey : bookDTODoubleMap.keySet()) {
            if (bookDTODoubleMap.get(bookKey) > 2.5) {
                Optional<BookDTO> bookDTO = bookRepository.findByIsbn(bookKey).map(BookDTO::new);
                if (bookDTO.isPresent()) {
                    bookDTOS.add(bookDTO.get());
                }
            }
        }
        return bookDTOS;
    }

    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }
}
