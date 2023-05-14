package com.sank.bookshop.services.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class OrderQuantityException extends RuntimeException {

    public OrderQuantityException(String message) {
        super(message);
    }

    private OrderQuantityException() {
    }
}
