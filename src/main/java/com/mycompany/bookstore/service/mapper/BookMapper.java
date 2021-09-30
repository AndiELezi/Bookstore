package com.mycompany.bookstore.service.mapper;

import com.mycompany.bookstore.domain.Book;
import com.mycompany.bookstore.domain.BookCategory;
import com.mycompany.bookstore.domain.BookSeries;
import com.mycompany.bookstore.service.dto.BookDTO;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookMapper {

    public List<BookDTO> booksToBookDTOs(List<Book> books) {
        return books.stream().filter(Objects::nonNull).map(this::bookToBookDTO).collect(Collectors.toList());
    }

    public BookDTO bookToBookDTO(Book book) {
        return new BookDTO(book);
    }

    public Book BookDTOToBook(BookDTO bookDTO) {
        Book book = new Book();
        book.setIsbn(bookDTO.getIsbn());
        book.setTitle(bookDTO.getTitle());
        book.setPrice(bookDTO.getPrice());
        book.setDescription(bookDTO.getDescription());
        book.setPublicationDate(bookDTO.getPublicationDate());
        book.setActive(true);
        BookCategory bookCategory = new BookCategory();
        bookCategory.setId(bookDTO.getCategoryId());
        BookSeries bookSeries=new BookSeries();
        bookSeries.setId(bookDTO.getBookSeriesId());
        book.setSeries(bookSeries);
        book.setCategory(bookCategory);
        return book;
    }

    public Book bookFromIsbn(String isbn) {
        if (isbn == null) {
            return null;
        }
        Book book = new Book();
        book.setIsbn(isbn);
        return book;
    }


}
