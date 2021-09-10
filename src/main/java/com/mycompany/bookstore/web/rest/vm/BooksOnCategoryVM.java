package com.mycompany.bookstore.web.rest.vm;

import com.mycompany.bookstore.domain.BookCategory;

import java.util.stream.Collectors;

/**
 * View Model object for storing the name and number of books for a category.
 */

public class BooksOnCategoryVM {
    private String categoryName;
    private int numberOfBooks;

    public BooksOnCategoryVM() {
    }

    public BooksOnCategoryVM(BookCategory bookCategory) {
        this.categoryName = bookCategory.getName();
        this.numberOfBooks = bookCategory.getBooks()
            .stream()
            .filter(book -> book.isActive())
            .collect(Collectors.toList())
            .size();
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getNumberOfBooks() {
        return numberOfBooks;
    }

    public void setNumberOfBooks(int numberOfBooks) {
        this.numberOfBooks = numberOfBooks;
    }
}
