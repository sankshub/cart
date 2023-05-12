package com.sank.bookshop.frontlayer.controller;

import com.sank.bookshop.frontlayer.exceptions.ExceptionResponse;
import com.sank.bookshop.frontlayer.mapper.RequestResponseMapper;
import com.sank.bookshop.frontlayer.model.BookResponse;
import com.sank.bookshop.frontlayer.model.CurrentOffer;
import com.sank.bookshop.frontlayer.model.ProcessedCart;
import com.sank.bookshop.frontlayer.model.ShoppingCart;
import com.sank.bookshop.servicelayer.model.ShoppingOrder;
import com.sank.bookshop.servicelayer.service.BookService;
import com.sank.bookshop.servicelayer.service.UniqueSetOfBooksDiscount;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping(value = "/api/")
public class ShoppingController {
    @Autowired
    BookService bookService;
    @Autowired
    UniqueSetOfBooksDiscount uniqueSetOfBooksDiscount;

    @ApiOperation(value = "Get All Books", notes = "Returns all available books")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = BookResponse.class),
            @ApiResponse(code = 404, message = "Not found - No books available right now"),
            @ApiResponse(code = 500, message = "Internal server error", response = ExceptionResponse.class)})
    @GetMapping(value = "books", produces = "application/json")
    public @NotNull HttpEntity<List<BookResponse>> getAllBooks() {
        return new HttpEntity<>(RequestResponseMapper.MAPPER.mapToBookModelList(bookService.findAll()));
    }

    @ApiOperation(value = "Get Discount perks", notes = "Returns all available discount rates")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = BookResponse.class),
            @ApiResponse(code = 404, message = "Not found - No books available right now"),
            @ApiResponse(code = 500, message = "Internal server error", response = ExceptionResponse.class)})
    @GetMapping(value = "discounts", produces = "application/json")
    public @NotNull HttpEntity<List<CurrentOffer>> getCurrentOffers() {
        return new HttpEntity<>(RequestResponseMapper.MAPPER.mapToDiscountOfferModelList(uniqueSetOfBooksDiscount.getCurrentDiscountOffer()));
    }

    @ApiOperation(value = "Process shopping cart with possible discount", notes = "Process shopping cart with possible discount and returns")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = ProcessedCart.class),
            @ApiResponse(code = 404, message = "Not found - No Discount found"),
            @ApiResponse(code = 400, message = "Bad Request - Duplicate entries Found"),
            @ApiResponse(code = 500, message = "Internal server error", response = ExceptionResponse.class)})
    @PostMapping(value = "processDiscount", consumes = "application/json", produces = "application/json")
    public @NotNull HttpEntity<ProcessedCart> processDiscount(@Valid @RequestBody List<ShoppingCart> cart) {
        List<ShoppingOrder> orders = RequestResponseMapper.MAPPER.mapShoppingCartEntityList(cart, bookService);
        return new HttpEntity<>(RequestResponseMapper.MAPPER.mapCartModel(uniqueSetOfBooksDiscount.calculateDiscount(orders)));
    }
}
