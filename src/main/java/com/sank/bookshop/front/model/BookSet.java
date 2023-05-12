package com.sank.bookshop.front.model;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class BookSet {
    private List<BookResponse> books;
    @ApiModelProperty(position = 1, example = "25%")
    private String discountApplied;
    @ApiModelProperty(position = 2, example = "125.00€")
    private String costOfSetAfterDiscount;
    @ApiModelProperty(position = 3, example = "225.00€")
    private String realCostOfSetWithoutDiscount;

}
