package com.sank.bookshop.services.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
@AllArgsConstructor
@Data
public class DiscountedCart {
    List<BookSet> bookSet;
    Double totalCostAfterDiscount;
    Double realCostWithoutDiscount;
}
