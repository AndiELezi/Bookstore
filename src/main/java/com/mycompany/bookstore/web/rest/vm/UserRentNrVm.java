package com.mycompany.bookstore.web.rest.vm;

public class UserRentNrVm {
    private String userLogin;
    private Long nrOfRentedBooks;

    public UserRentNrVm() {
    }

    public UserRentNrVm(String userLogin, Long nrOfRentedBooks) {
        this.userLogin = userLogin;
        this.nrOfRentedBooks = nrOfRentedBooks;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public Long getNrOfRentedBooks() {
        return nrOfRentedBooks;
    }

    public void setNrOfRentedBooks(Long nrOfRentedBooks) {
        this.nrOfRentedBooks = nrOfRentedBooks;
    }
}
