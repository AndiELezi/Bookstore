package com.mycompany.bookstore.service.dto;

import com.mycompany.bookstore.domain.Review;

import java.sql.Date;

public class ReviewDTO {
    private String bookIsbn;
    private Long rating;
    private Date creationDate;

    public ReviewDTO() {
    }

    public ReviewDTO(Review review) {
        this.bookIsbn = review.getBook().getIsbn();
        this.rating = review.getRating();
        this.creationDate = review.getCreationDate();
    }

    public String getBookIsbn() {
        return bookIsbn;
    }

    public void setBookIsbn(String bookIsbn) {
        this.bookIsbn = bookIsbn;
    }

    public Long getRating() {
        return rating;
    }

    public void setRating(Long rating) {
        this.rating = rating;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}
