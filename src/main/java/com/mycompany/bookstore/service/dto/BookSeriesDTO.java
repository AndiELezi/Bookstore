package com.mycompany.bookstore.service.dto;

import com.mycompany.bookstore.domain.Book;
import com.mycompany.bookstore.domain.BookSeries;

public class BookSeriesDTO {
    private String name;
    private float averageRating;

    public BookSeriesDTO() {
    }

    public BookSeriesDTO(BookSeries bookSeries) {
        this.name = bookSeries.getName();
        this.averageRating = 0;
        calculateRating(bookSeries);
    }

    private void calculateRating(BookSeries bookSeries) {
        int ratedBooks = 0;
        for (Book book : bookSeries.getBooks()) {
            BookDTO bookDTO = new BookDTO(book);
            if (bookDTO.getAverageRating() > 0) {
                this.averageRating += bookDTO.getAverageRating();
                ratedBooks++;
            }
        }
        if (ratedBooks > 0) {
            this.averageRating = this.averageRating / ratedBooks;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(float averageRating) {
        this.averageRating = averageRating;
    }
}
