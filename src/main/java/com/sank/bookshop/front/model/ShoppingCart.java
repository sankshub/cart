package com.sank.bookshop.front.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ShoppingCart {
    @ApiModelProperty(example = "123456789123456789")
    @NotBlank
    @NotNull
    private String isbn;
    @NotNull
    @ApiModelProperty(example = "2")
    private Integer quantity;
}
