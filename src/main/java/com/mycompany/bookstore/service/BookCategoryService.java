package com.mycompany.bookstore.service;

import com.mycompany.bookstore.domain.Book;
import com.mycompany.bookstore.domain.BookCategory;
import com.mycompany.bookstore.repository.BookCategoryRepository;
import com.mycompany.bookstore.service.dto.BookCategoryDTO;
import com.mycompany.bookstore.service.dto.BookDTO;
import com.mycompany.bookstore.web.rest.vm.BooksOnCategoryVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
/**
 * Service class for managing book categories.
 */

@Service
@Transactional
public class BookCategoryService {
    private final Logger log = LoggerFactory.getLogger(BookCategoryService.class);
    private final BookCategoryRepository bookCategoryRepository;

    public BookCategoryService(BookCategoryRepository bookCategoryRepository){
        this.bookCategoryRepository=bookCategoryRepository;
    }

    public Page<BookCategoryDTO> getAllBookCategories(Pageable pageable){
        return  bookCategoryRepository.findAll(pageable).map(BookCategoryDTO::new);
    }

    public Page<BooksOnCategoryVM> getAllBookCategoriesWithBooks(Pageable pageable){
        return  bookCategoryRepository.findAll(pageable).map(BooksOnCategoryVM::new);
    }

    public Optional<BookCategory> getBookCategory(Long id){
        return bookCategoryRepository.findById(id);
    }

    public void deleteByCategory(Long categoryId){
        bookCategoryRepository.findById(categoryId).ifPresent(bookCategory->{
            bookCategory.getBooks().forEach(book -> {
                book.setActive(false);
            });
            bookCategoryRepository.save(bookCategory);
        });
    }

}
