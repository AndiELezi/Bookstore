package com.mycompany.bookstore.service.dto;

import com.mycompany.bookstore.domain.Rent;

import java.sql.Date;

/**
 * A DTO representing a rent
 */
public class RentDTO {
    private Date rentStartDate;
    private String userEmail;
    private String userFullName;
    private String bookIsbn;
    private String bookName;

    public RentDTO() {
    }

    public RentDTO(Rent rent){
        this.rentStartDate=rent.getRentDate();
        this.userEmail=rent.getUser().getEmail();
        this.userFullName=String.join(" ",rent.getUser().getFirstName(),rent.getUser().getLastName());
        this.bookIsbn=rent.getBook().getIsbn();
        this.bookName=rent.getBook().getTitle();
    }

    public Date getRentStartDate() {
        return rentStartDate;
    }

    public void setRentStartDate(Date rentStartDate) {
        this.rentStartDate = rentStartDate;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userLogin) {
        this.userEmail = userLogin;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public String getBookIsbn() {
        return bookIsbn;
    }

    public void setBookIsbn(String bookIsbn) {
        this.bookIsbn = bookIsbn;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }
}
