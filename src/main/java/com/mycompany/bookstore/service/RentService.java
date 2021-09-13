package com.mycompany.bookstore.service;

import com.mycompany.bookstore.domain.Book;
import com.mycompany.bookstore.domain.Rent;
import com.mycompany.bookstore.domain.User;
import com.mycompany.bookstore.repository.BookRepository;
import com.mycompany.bookstore.repository.RentRepository;
import com.mycompany.bookstore.repository.UserRepository;
import com.mycompany.bookstore.security.SecurityUtils;
import com.mycompany.bookstore.service.dto.RentDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.Optional;

/**
 * Service class for managing rents.
 */

@Service
public class RentService {
    private final Logger log = LoggerFactory.getLogger(RentService.class);
    private final RentRepository rentRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    public RentService(RentRepository rentRepository, UserRepository userRepository, BookRepository bookRepository) {
        this.rentRepository = rentRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

    public Page<RentDTO> getAllActiveRents(Pageable pageable) {
        return rentRepository.findAllByReturnedIsFalse(pageable).map(RentDTO::new);
    }

    public RentDTO createRent(String bookIsbn) {
        Optional<User> user = SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneWithAuthoritiesByLogin);
        Optional<Book> book = bookRepository.findByIsbnAndActiveTrue(bookIsbn);
        if (user.isPresent() && book.isPresent()) {
            Rent rent = new Rent();
            rent.setUser(user.get());
            rent.setBook(book.get());
            rent.setRentDate(new Date(System.currentTimeMillis()));
            rent.setReturned(false);
            return new RentDTO(rentRepository.save(rent));
        } else {
            return new RentDTO();
        }
    }
}
