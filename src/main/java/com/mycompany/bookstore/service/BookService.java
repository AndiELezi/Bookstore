package com.mycompany.bookstore.service;

import com.mycompany.bookstore.domain.Book;
import com.mycompany.bookstore.repository.BookRepository;
import com.mycompany.bookstore.service.dto.BookDTO;
import com.mycompany.bookstore.service.dto.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class BookService {
    private final Logger log = LoggerFactory.getLogger(BookService.class);
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository){
        this.bookRepository=bookRepository;
    }

    public Book createBook(Book book){
        return bookRepository.save(book);
    }

    public Optional<BookDTO> updateBook(BookDTO bookDTO){
        return Optional
            .of(bookRepository.findById(bookDTO.getIsbn()))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(
                bookToBeUpdated -> {
                    bookToBeUpdated.setTitle(bookDTO.getTitle());
                    bookToBeUpdated.setPublicationDate(bookDTO.getPublicationDate());
                    bookToBeUpdated.setPrice(bookDTO.getPrice());
                    bookToBeUpdated.setDescription(bookDTO.getDescription());
                    return  bookToBeUpdated;
                }
            )
            .map(BookDTO::new);
    }

    public Optional<Book> getBook(String isbn){
        return bookRepository.findById(isbn);
    }


    public void deleteBook(String isbn) {
        bookRepository
            .findById(isbn)
            .ifPresent(
                book -> {
                    bookRepository.delete(book);
                }
            );
    }

    public Page<BookDTO> getAllBooks(Pageable pageable){
        return  bookRepository.findAll(pageable).map(BookDTO::new);
    }

}
