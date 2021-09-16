package com.mycompany.bookstore.service;

import com.mycompany.bookstore.domain.Book;
import com.mycompany.bookstore.domain.Rent;
import com.mycompany.bookstore.domain.User;
import com.mycompany.bookstore.repository.BookRepository;
import com.mycompany.bookstore.repository.RentRepository;
import com.mycompany.bookstore.repository.UserRepository;
import com.mycompany.bookstore.security.SecurityUtils;
import com.mycompany.bookstore.service.dto.RentDTO;
import com.mycompany.bookstore.web.rest.vm.UserRentNrVm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Service class for managing rents.
 */

@Service
public class RentService {
    private static final int MAX_RENTED_BOOKS = 3;
    private static final int BOOK_MAX_RENT_DAYS = 14;
    private final Logger log = LoggerFactory.getLogger(RentService.class);
    private final RentRepository rentRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final MailService mailService;

    public RentService(RentRepository rentRepository, UserRepository userRepository, BookRepository bookRepository, MailService mailService) {
        this.rentRepository = rentRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.mailService = mailService;
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

    public boolean canUserRentBook() {
        Optional<User> user = SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneWithAuthoritiesByLogin);
        if (user.isEmpty()) {
            return false;
        }
        return rentRepository.findAllByUserAndReturnedIsFalse(user.get()).size() < MAX_RENTED_BOOKS;
    }

    public boolean checkBookAvailability(String bookIsbn) {
        Optional<Book> book = bookRepository.findByIsbnAndActiveTrue(bookIsbn);
        if (book.isEmpty()) {
            return false;
        }
        return rentRepository.findByBookAndReturnedIsFalse(book.get()).isEmpty();
    }

    public Optional<UserRentNrVm> userWithMostRents() {
        List<UserRentNrVm> userRentNrVmList = new ArrayList<>();

        rentRepository.findAllByReturnedIsFalse()
            .stream()
            .collect(Collectors.groupingBy(Rent::getUser, Collectors.counting()))
            .forEach((user, nrOfRents) -> {
                userRentNrVmList.add(new UserRentNrVm(user.getLogin(), nrOfRents));
            });

        return userRentNrVmList.stream()
            .sorted(Comparator.comparingLong(UserRentNrVm::getNrOfRentedBooks).reversed())
            .findFirst();
    }

    @Scheduled(cron = "0 0 8 * * ?")
    public void notifyUsersToReturnBook() {
        rentRepository.findAllByReturnedIsFalse().forEach(rent -> {
            long differenceInMilliseconds = System.currentTimeMillis() - rent.getRentDate().getTime();
            long days = TimeUnit.MILLISECONDS.toDays(differenceInMilliseconds);
            if (days > BOOK_MAX_RENT_DAYS) {
                mailService.sendRentDelayMail(rent);
            }
        });

    }


}
