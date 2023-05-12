package com.sank.bookshop.repolayer.entity;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

@Data

public class Author {
    private String firstName;
    private String middleName;
    private String lastName;

    public String getFullName() {
        return Stream.of(getFirstName(), getMiddleName(), getLastName())
                .filter(StringUtils::isNotBlank)
                .collect(joining(" "));
    }
}
