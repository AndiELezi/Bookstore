package com.mycompany.bookstore.service.dto;

import com.mycompany.bookstore.domain.Book;
import com.mycompany.bookstore.domain.BookCategory;
import org.springframework.format.annotation.NumberFormat;

import javax.validation.constraints.NotNull;
import java.sql.Date;

/**
 * A DTO representing a book
 */
public class BookDTO {

    private String isbn;
    private String title;
    private Date publicationDate;
    private float price;
    private String description;
    @NotNull
    private Long categoryId;
    private String categoryName;

    public BookDTO() {
    }

    public BookDTO(Book book) {
        this.isbn = book.getIsbn();
        this.title = book.getTitle();
        this.publicationDate = book.getPublicationDate();
        this.price = book.getPrice();
        ;
        this.description = book.getDescription();
        this.categoryId = book.getCategory().getId();
        this.categoryName = book.getCategory().getName();
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(Date publicationDate) {
        this.publicationDate = publicationDate;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }


    @Override
    public String toString() {
        return "BookDTO{" +
            "isbn='" + isbn + '\'' +
            ", title='" + title + '\'' +
            ", price='" + price + '\'' +
            ", publication date='" + publicationDate + '\'' +
            ", description='" + description + '\'' +
            "}";
    }
}
