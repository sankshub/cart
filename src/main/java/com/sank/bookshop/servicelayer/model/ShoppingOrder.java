package com.sank.bookshop.servicelayer.model;

import com.sank.bookshop.repolayer.entity.Book;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ShoppingOrder {

    private Book book;
    private Integer quantity;

    public void changeQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
