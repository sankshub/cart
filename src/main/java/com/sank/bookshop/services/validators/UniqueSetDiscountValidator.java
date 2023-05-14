package com.sank.bookshop.services.validators;

import com.sank.bookshop.services.exceptions.DuplicateEntriesInCartException;
import com.sank.bookshop.services.exceptions.ShoppingCartException;
import com.sank.bookshop.services.model.ShoppingOrder;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class UniqueSetDiscountValidator {

    private static final String DUPLICATE_BOOK_ENTRY_ERROR = "Duplicate book entry found in Cart, Remove it and request again";
    private static final String MINIMUM_BOOK_QUANTITY_ERROR = "Minimum 1 quantity required per order Check and request again";
    private static final String EMPTY_CART_ERROR = "Cart is Empty, add items and request again";

    private UniqueSetDiscountValidator() {
    }

    public static void validateShoppingCart(List<ShoppingOrder> shoppingOrderList) throws DuplicateEntriesInCartException {
        checkCartItems(shoppingOrderList);
        checkMinQuantityPerOrder(shoppingOrderList);
        checkDuplicateItemsInCart(shoppingOrderList);
    }

    private static void checkCartItems(List<ShoppingOrder> shoppingOrderList) throws ShoppingCartException {
        if (CollectionUtils.isEmpty(shoppingOrderList))
            throw new ShoppingCartException(EMPTY_CART_ERROR);

    }

    private static void checkMinQuantityPerOrder(List<ShoppingOrder> shoppingOrderList) throws ShoppingCartException {
        if (shoppingOrderList.stream()
                             .anyMatch(book -> book.getQuantity() == null || book.getQuantity() < 1))
            throw new ShoppingCartException(MINIMUM_BOOK_QUANTITY_ERROR);

    }

    private static void checkDuplicateItemsInCart(List<ShoppingOrder> shoppingOrderList) throws DuplicateEntriesInCartException {
        boolean isDuplicateBookFound = shoppingOrderList.stream()
                                                        .map(ShoppingOrder::getBook)
                                                        .collect(Collectors.toList())
                                                        .stream()
                                                        .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                                                        .entrySet()
                                                        .stream()
                                                        .anyMatch(m -> m.getValue() > 1);
        if (isDuplicateBookFound) throw new DuplicateEntriesInCartException(DUPLICATE_BOOK_ENTRY_ERROR);
    }
}
