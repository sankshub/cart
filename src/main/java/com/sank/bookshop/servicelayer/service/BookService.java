package com.sank.bookshop.servicelayer.service;

import com.sank.bookshop.repolayer.entity.Book;
import javax.validation.constraints.NotNull;
import java.util.List;

public interface BookService {
    @NotNull List<Book> findAll();
    Book findByIsbn(String isbn);

}
