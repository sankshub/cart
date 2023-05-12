package com.sank.bookshop.servicelayer.service;

import com.sank.bookshop.servicelayer.exceptions.BookNotFoundException;
import com.sank.bookshop.repolayer.entity.Book;
import com.sank.bookshop.repolayer.repository.BookRepo;
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
