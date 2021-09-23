package com.mycompany.bookstore.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Book series used to represent the series a book belongs to
 */
@Entity
@Table(name = "jhi_book_series")
public class BookSeries {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Size(max = 50)
    @Column(length = 50)
    private String name;

    @JsonManagedReference
    @OneToMany(mappedBy = "series")
    @OrderBy("publicationDate ASC ")
    public List<Book> books;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    @Override
    public String toString() {
        return "BookSeries{" +
            "id='" + id + '\'' +
            ", name='" + name + '\'' +
            "}";
    }
}
