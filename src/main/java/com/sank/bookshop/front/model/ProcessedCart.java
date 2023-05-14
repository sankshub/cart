package com.sank.bookshop.front.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class ProcessedCart {
    List<BookSet> bookSet;
    @ApiModelProperty(position = 1, example = "500.00€")
    String totalCostAfterDiscount;
    @ApiModelProperty(position = 2, example = "800.00€")
    String realCostWithoutDiscount;
}
