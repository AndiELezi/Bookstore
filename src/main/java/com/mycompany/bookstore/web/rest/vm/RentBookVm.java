package com.mycompany.bookstore.web.rest.vm;

public class RentBookVm {
    private String isbn;

    public RentBookVm() {
    }

    public RentBookVm(String isbn) {
        this.isbn = isbn;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
}
