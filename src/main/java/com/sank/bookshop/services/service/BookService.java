package com.sank.bookshop.services.service;

import com.sank.bookshop.repos.entity.Book;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface BookService {
    @NotNull List<Book> findAll();
    Book findByIsbn(String isbn);

}
