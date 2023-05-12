package com.sank.bookshop.frontlayer.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ShoppingCart {
    @ApiModelProperty(example = "123456789123456789")
    @NotBlank
    @NotNull
    private String isbn;
    @NotNull
    @Min(1)
    @Max(10)
    private Integer quantity;
}
