package com.sank.bookshop.servicelayer.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DuplicateEntriesInCartException extends RuntimeException {

    public DuplicateEntriesInCartException(String message) {
        super(message);
    }

    private DuplicateEntriesInCartException() {
    }
}
