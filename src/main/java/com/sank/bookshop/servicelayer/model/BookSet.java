package com.sank.bookshop.servicelayer.model;


import com.sank.bookshop.repolayer.entity.Book;
import lombok.Data;

import java.util.Set;

@Data
public class BookSet {
    private final Set<Book> books;
    private final Integer discount;
    private Double discountedCost;

    public BookSet(Set<Book> books, Integer discount) {
        this.books = books;
        this.discount = discount;
    }
}
