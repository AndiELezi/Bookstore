package com.mycompany.bookstore.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Date;

/**
 * A book
 */
@Entity
@Table(name = "jhi_book")
public class Book extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @NotNull
    @Size(max = 13, min = 13)
    @Column(length = 13)
    private String isbn;

    @Size(max = 100)
    @Column(name = "title", length = 100)
    private String title;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date publicationDate;

    @Column(name = "price")
    private float price;

    @Size(max = 300)
    @Column(name = "description", length = 100)
    private String description;


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
