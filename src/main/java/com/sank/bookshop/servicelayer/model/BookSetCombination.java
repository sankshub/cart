package com.sank.bookshop.servicelayer.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
@Data
@AllArgsConstructor
public class BookSetCombination {
    List<BookSet> set;
}
