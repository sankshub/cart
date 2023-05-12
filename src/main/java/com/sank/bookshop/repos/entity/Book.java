package com.sank.bookshop.repos.entity;

import lombok.Data;

@Data
public class Book {
    private String isbn;
    private String title;
    private Author author;
    private String yearOfPublish;
    private String price;

}
