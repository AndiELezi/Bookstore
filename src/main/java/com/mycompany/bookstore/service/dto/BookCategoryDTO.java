package com.mycompany.bookstore.service.dto;

import com.mycompany.bookstore.domain.Book;
import com.mycompany.bookstore.domain.BookCategory;

/**
 * A DTO representing a book category
 */
public class BookCategoryDTO {
    private String name;

    public BookCategoryDTO() {
    }

    public BookCategoryDTO(BookCategory bookCategory) {
        this.name = bookCategory.getName();
    }

    public String getCategoryName() {
        return name;
    }

    public void setCategoryName(String categoryName) {
        this.name = categoryName;
    }
}
