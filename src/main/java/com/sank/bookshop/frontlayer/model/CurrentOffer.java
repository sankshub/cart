package com.sank.bookshop.frontlayer.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CurrentOffer {
    @ApiModelProperty(example = "More books more savings")
    private String discountMessage;
    @ApiModelProperty(position = 1, example = "5")
    private Integer uniqueCopiesOfBooks;
    @ApiModelProperty(position = 2, example = "25%")
    private String discount;
}
