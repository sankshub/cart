package com.sank.bookshop.services.service;

import com.sank.bookshop.repos.entity.Author;
import com.sank.bookshop.repos.entity.Book;
import com.sank.bookshop.services.exceptions.DuplicateEntriesInCartException;
import com.sank.bookshop.services.exceptions.OrderQuantityException;
import com.sank.bookshop.services.model.DiscountOffer;
import com.sank.bookshop.services.model.ShoppingOrder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class UniqueSetOfBooksDiscountTest {
    private static final String MINIMUM_BOOK_QUANTITY_ERROR = "Minimum 1 quantity required per order Check and request again";
    private static final String DUPLICATE_BOOK_ENTRY_ERROR = "Duplicate book entry found in Cart, Remove it and request again";
    private static final String OFFER_MESSAGE = "Buy different copies of books to get maximum discount!";
    List<ShoppingOrder> cartWithoutQuantity = new ArrayList<>();
    List<ShoppingOrder> cartWithDupicates = new ArrayList<>();
    @InjectMocks
    UniqueSetOfBooksDiscount discountCalculationService;

    @Before
    public void setUp() throws Exception {
        Book book = new Book();
        book.setIsbn("123456789");
        book.setTitle("Clean Code");
        Author author = new Author();
        author.setFirstName("Robert");
        author.setMiddleName(null);
        author.setLastName("Martin");
        book.setAuthor(author);
        book.setYearOfPublish("2008");
        book.setPrice("50");

        ShoppingOrder orderWithZeroBooks = new ShoppingOrder(book, 0);
        cartWithoutQuantity.add(orderWithZeroBooks);

        ShoppingOrder ShoppingOrder = new ShoppingOrder(book, 1);
        ShoppingOrder duplicateShoppingOrder = new ShoppingOrder(book, 1);
        cartWithDupicates.add(ShoppingOrder);
        cartWithDupicates.add(duplicateShoppingOrder);

    }

    @Test
    public void getCurrentDiscountOfferAsList() {
        Assert.assertTrue(discountCalculationService.getCurrentDiscountOffer()
                                                    .size() > 1);
    }

    @Test
    public void getCurrentDiscountOfferContainsDiscountMessage() {
        List<DiscountOffer> discountOffers = discountCalculationService.getCurrentDiscountOffer();
        Assert.assertEquals(OFFER_MESSAGE, discountOffers.get(0)
                                                         .getDiscountMessage());
    }

    @Test
    public void calculateDiscountWithZeroBook() {
        Exception exception = Assert.assertThrows(OrderQuantityException.class, () -> {
            discountCalculationService.calculateDiscount(cartWithoutQuantity);
        });
        Assert.assertEquals(MINIMUM_BOOK_QUANTITY_ERROR, exception.getMessage());
    }

    @Test
    public void calculateDiscountWithDuplicateBook() {
        Exception exception = Assert.assertThrows(DuplicateEntriesInCartException.class, () -> {
            discountCalculationService.calculateDiscount(cartWithDupicates);
        });
        Assert.assertEquals(DUPLICATE_BOOK_ENTRY_ERROR, exception.getMessage());
    }
}