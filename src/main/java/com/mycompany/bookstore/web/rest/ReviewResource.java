package com.mycompany.bookstore.web.rest;

import com.mycompany.bookstore.domain.Review;
import com.mycompany.bookstore.service.ReviewService;
import com.mycompany.bookstore.service.dto.BookDTO;
import com.mycompany.bookstore.service.dto.ReviewDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Rest controller for managing reviews
 */

@RestController
@RequestMapping("/api")
public class ReviewResource {
    final private ReviewService reviewService;
    private final Logger log = LoggerFactory.getLogger(ReviewResource.class);

    public ReviewResource(ReviewService reviewService) {
        this.reviewService = reviewService;
    }


    /**
     * {@code POST  /api/reviews}  : Creates a review
     *
     * @param reviewDTO The review to be created.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the created review,
     * or with status {@code 409 (Conflict)} if the user cannot make a review for this book
     */

    @PostMapping("/reviews")
    public ResponseEntity<ReviewDTO> createReview(@RequestBody ReviewDTO reviewDTO) {
        if (!reviewService.canUserReview(reviewDTO)) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(reviewService.addReview(reviewDTO), HttpStatus.CREATED);
    }

    /**
     * {@code GET  /api/reviews}  : Gets all reviews
     *
     * @return the {@link ResponseEntity} with status {@code 200 (ok)} and with the reviews .
     */
    @GetMapping("/reviews")
    public ResponseEntity<List<Review>> getAllReviews() {
        return new ResponseEntity<>(reviewService.getAllReviews(), HttpStatus.OK);
    }

    /**
     * {@code GET  /api/weeklyRecommendations}  : Gets books with the average rating above 2.5(average) only in last week
     *
     * @return the {@link ResponseEntity} with status {@code 200 (ok)} and with the books .
     */
    @GetMapping("/weeklyRecommendations")
    public ResponseEntity<List<BookDTO>> getWeeklyRecommendations() {
        return new ResponseEntity<>(reviewService.mostRatedBooksOfWeek(), HttpStatus.OK);
    }
}
