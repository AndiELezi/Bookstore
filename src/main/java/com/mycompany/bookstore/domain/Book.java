package com.mycompany.bookstore.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Date;
import java.util.List;

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

    @Column(name = "active")
    private boolean active;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "category_id")
    BookCategory category;

    @JsonBackReference
    @NotFound(action = NotFoundAction.IGNORE)
    @ManyToOne
    @JoinColumn(name = "series_id")
    BookSeries series;

    @JsonManagedReference
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "book")
    public List<Review> reviews;

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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public BookCategory getCategory() {
        return category;
    }

    public void setCategory(BookCategory category) {
        this.category = category;
    }

    public BookSeries getSeries() {
        return series;
    }

    public void setSeries(BookSeries series) {
        this.series = series;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
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
