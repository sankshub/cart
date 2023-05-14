package com.sank.bookshop.services.service;

import com.sank.bookshop.repos.entity.Author;
import com.sank.bookshop.repos.entity.Book;
import com.sank.bookshop.services.exceptions.DuplicateEntriesInCartException;
import com.sank.bookshop.services.exceptions.ShoppingCartException;
import com.sank.bookshop.services.model.DiscountOffer;
import com.sank.bookshop.services.model.DiscountedCart;
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
    private static final String EMPTY_CART_ERROR = "Cart is Empty, add items and request again";
    private static final String OFFER_MESSAGE = "Buy different copies of books to get maximum discount!";
    List<ShoppingOrder> cartWithoutQuantity = new ArrayList<>();
    List<ShoppingOrder> cartWithDuplicates = new ArrayList<>();
    List<ShoppingOrder> simpleCart = new ArrayList<>();

    Book cleanCodeBook = new Book();
    Book legacyCodeBook = new Book();
    @InjectMocks
    UniqueSetOfBooksDiscount discountCalculationService;

    @Before
    public void setUp() throws Exception {

        cleanCodeBook.setIsbn("123456789");
        cleanCodeBook.setTitle("Clean Code");
        Author author = new Author();
        author.setFirstName("Robert");
        author.setMiddleName(null);
        author.setLastName("Martin");
        cleanCodeBook.setAuthor(author);
        cleanCodeBook.setYearOfPublish("2008");
        cleanCodeBook.setPrice("50.0");

        legacyCodeBook.setIsbn("567891234");
        legacyCodeBook.setTitle("Working Effectively With Legacy Code");
        Author authorLegacyCode = new Author();
        authorLegacyCode.setFirstName("Michael");
        authorLegacyCode.setMiddleName("C.");
        authorLegacyCode.setLastName("Feathers");
        legacyCodeBook.setAuthor(authorLegacyCode);
        legacyCodeBook.setYearOfPublish("2004");
        legacyCodeBook.setPrice("60.0");

        ShoppingOrder orderWithZeroBooks = new ShoppingOrder(cleanCodeBook, 0);
        cartWithoutQuantity.add(orderWithZeroBooks);

        ShoppingOrder shoppingOrder = new ShoppingOrder(cleanCodeBook, 1);
        ShoppingOrder duplicateShoppingOrder = new ShoppingOrder(cleanCodeBook, 1);
        cartWithDuplicates.add(shoppingOrder);
        cartWithDuplicates.add(duplicateShoppingOrder);

        ShoppingOrder simpleShoppingOrder = new ShoppingOrder(cleanCodeBook, 1);
        simpleCart.add(simpleShoppingOrder);

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
    public void calculateDiscountWithNull() {
        Exception exception = Assert.assertThrows(ShoppingCartException.class, () -> {
            discountCalculationService.calculateDiscount(null);
        });
        Assert.assertEquals(EMPTY_CART_ERROR, exception.getMessage());
    }

    @Test
    public void calculateDiscountWithZeroBook() {
        Exception exception = Assert.assertThrows(ShoppingCartException.class, () -> {
            discountCalculationService.calculateDiscount(cartWithoutQuantity);
        });
        Assert.assertEquals(MINIMUM_BOOK_QUANTITY_ERROR, exception.getMessage());
    }

    @Test
    public void calculateDiscountWithDuplicateBook() {
        Exception exception = Assert.assertThrows(DuplicateEntriesInCartException.class, () -> {
            discountCalculationService.calculateDiscount(cartWithDuplicates);
        });
        Assert.assertEquals(DUPLICATE_BOOK_ENTRY_ERROR, exception.getMessage());
    }

    @Test
    public void getSameBookAsGivenInRequest() {
        DiscountedCart discountedCart = discountCalculationService.calculateDiscount(simpleCart);
        Assert.assertEquals(1, discountedCart.getBookSet()
                                             .size());
        Assert.assertEquals(1, discountedCart.getBookSet()
                                             .get(0)
                                             .getBooks()
                                             .size());
        Assert.assertTrue(discountedCart.getBookSet()
                                        .get(0)
                                        .getBooks()
                                        .contains(cleanCodeBook));
    }

    @Test
    public void getDiscountedAndRealCostForABook() {
        DiscountedCart discountedCart = discountCalculationService.calculateDiscount(simpleCart);
        Assert.assertEquals(cleanCodeBook.getPrice(), String.valueOf(discountedCart.getRealCostWithoutDiscount()));
        Assert.assertEquals(cleanCodeBook.getPrice(), String.valueOf(discountedCart.getTotalCostAfterDiscount()));
    }

    @Test
    public void multipleBooksOfSameCopyInRequest() {
        simpleCart.remove(0);
        simpleCart.add(new ShoppingOrder(legacyCodeBook, 10));
        DiscountedCart discountedCart = discountCalculationService.calculateDiscount(simpleCart);
        Assert.assertEquals(10, discountedCart.getBookSet()
                                              .size());
        Assert.assertEquals(1, discountedCart.getBookSet()
                                             .get(0)
                                             .getBooks()
                                             .size());
        Assert.assertTrue(discountedCart.getBookSet()
                                        .get(0)
                                        .getBooks()
                                        .contains(legacyCodeBook));
    }


}