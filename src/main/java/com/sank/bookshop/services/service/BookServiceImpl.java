package com.sank.bookshop.services.service;

import com.sank.bookshop.repos.entity.Book;
import com.sank.bookshop.repos.repository.BookRepo;
import com.sank.bookshop.services.exceptions.BookNotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {
    private static final String BOOK_NOT_FOUND_ERROR = "Requested book not found, Please try with valid ISBN ";
    @Autowired
    BookRepo bookRepo;

    @Override
    public List<Book> findAll() {
        return bookRepo.findAll();
    }

    @Override
    public Book findByIsbn(String isbn) throws BookNotFoundException {
        List<Book> bookList = bookRepo.findAll();
        return bookList.stream().filter(book -> StringUtils.equalsIgnoreCase(book.getIsbn(), isbn)).findAny().orElseThrow(()->new BookNotFoundException(BOOK_NOT_FOUND_ERROR));
    }
}
