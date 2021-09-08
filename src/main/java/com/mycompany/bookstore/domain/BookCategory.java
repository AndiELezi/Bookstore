package com.mycompany.bookstore.domain;


import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
/**
 * Book category used to represent the category a book belongs to
 */
@Entity
@Table(name = "jhi_book_category")
public class BookCategory implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Size(max = 50)
    @Column(length = 50)
    private String name;

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

    @Override
    public String toString() {
        return "Category{" +
            "id='" + id + '\'' +
            ", name='" + name + '\'' +
            "}";
    }
}
