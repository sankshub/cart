package com.sank.bookshop.servicelayer.service;

import com.sank.bookshop.repolayer.entity.Book;
import com.sank.bookshop.servicelayer.exceptions.DuplicateEntriesInCartException;
import com.sank.bookshop.servicelayer.model.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class UniqueSetOfBooksDiscount implements DiscountCalculationService {
    private static final List<UniqueBookOffer> UNIQUE_BOOK_OFFERS = new ArrayList<>();
    private static final String DUPLICATE_BOOK_ENTRY_ERROR = "Duplicate book entry found in Cart, Remove it and request again";

    private UniqueSetOfBooksDiscount() {
        UNIQUE_BOOK_OFFERS.add(new UniqueBookOffer(2, 5));
        UNIQUE_BOOK_OFFERS.add(new UniqueBookOffer(3, 10));
        UNIQUE_BOOK_OFFERS.add(new UniqueBookOffer(4, 20));
        UNIQUE_BOOK_OFFERS.add(new UniqueBookOffer(5, 25));
    }

    private static DiscountedCart getDiscountedCart(BookSetCombination bestCombination) {
        double totalPriceWithDiscount = 0.0;
        double pricePerSet = 0.0;
        double totalPriceWithoutDiscount = 0.0;
        for (BookSet bookSet : bestCombination.getSet()) {
            for (Book book : bookSet.getBooks()) {
                pricePerSet += Double.parseDouble(book.getPrice());
                totalPriceWithoutDiscount += Double.parseDouble(book.getPrice());
            }
            pricePerSet = pricePerSet * (1.0 - (bookSet.getDiscount() / 100.0));
            bookSet.setDiscountedCost(pricePerSet);
            totalPriceWithDiscount += pricePerSet;
            pricePerSet = 0;
        }
        return new DiscountedCart(bestCombination.getSet(), totalPriceWithDiscount, totalPriceWithoutDiscount);
    }

    @Override
    public List<UniqueBookOffer> getCurrentDiscountOffer() {
        return UNIQUE_BOOK_OFFERS;
    }

    @Override
    public DiscountedCart calculateDiscount(List<ShoppingOrder> shoppingOrderList) {
        checkDuplicateItemsInCart(shoppingOrderList);
        BookSetCombination bestCombination = getBestCombinationBookSets(shoppingOrderList);
        return getDiscountedCart(bestCombination);
    }

    private void checkDuplicateItemsInCart(List<ShoppingOrder> shoppingOrderList) throws DuplicateEntriesInCartException {
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

    private BookSetCombination getBestCombinationBookSets(List<ShoppingOrder> shoppingCartItems) {
        List<BookSetCombination> bookSetCombinations = new ArrayList<>();
        for (int cartSize = shoppingCartItems.size(); cartSize >= 1; cartSize--) {
            bookSetCombinations.add(new BookSetCombination(createPossibleSets(shoppingCartItems, cartSize)));
        }

        if (bookSetCombinations.size() > 1)
            return selectBooksSetsWithMaxDiscount(bookSetCombinations);
        else
            return bookSetCombinations.get(0);
    }

    private List<BookSet> createPossibleSets(List<ShoppingOrder> shoppingCartItems, int maxSizeSet) {
        List<ShoppingOrder> remainingShoppingCartItems = cloneShoppingCartItems(shoppingCartItems);
        List<BookSet> setsOfDifferentBooks = new ArrayList<>();

        while (!remainingShoppingCartItems.isEmpty()) {
            final BookSet oneSetOfDifferentBooks = createNextSet(remainingShoppingCartItems, maxSizeSet);
            setsOfDifferentBooks.add(oneSetOfDifferentBooks);
        }

        return setsOfDifferentBooks;
    }

    private BookSet createNextSet(List<ShoppingOrder> remainingShoppingCartItems, int maxSizeSet) {
        HashSet<Book> books = new HashSet<>();

        for (ShoppingOrder item : new ArrayList<>(remainingShoppingCartItems)) {
            books.add(item.getBook());
            if (item.getQuantity() == 1)
                remainingShoppingCartItems.remove(item);
            else
                item.changeQuantity(item.getQuantity() - 1);
            if (books.size() == maxSizeSet)
                break;
        }

        return new BookSet(books, getDiscount(books.size()));

    }

    private BookSetCombination selectBooksSetsWithMaxDiscount(List<BookSetCombination> bookSetCombinations) {
        TreeMap<Integer, BookSetCombination> combinationHashMap = new TreeMap<>();
        for (BookSetCombination combination : bookSetCombinations) {
            combinationHashMap.put(combination.getSet()
                                              .stream()
                                              .mapToInt(BookSet::getDiscount)
                                              .sum(), combination);
        }
        return combinationHashMap.lastEntry() .getValue();
    }

    private List<ShoppingOrder> cloneShoppingCartItems(List<ShoppingOrder> shoppingCartItems) {
        return shoppingCartItems.stream()
                                .map(shoppingOrder -> new ShoppingOrder(shoppingOrder.getBook(), shoppingOrder.getQuantity()))
                                .collect(Collectors.toList());
    }

    private int getDiscount(int differentBooksCount) {
        int defaultDiscount = 0;

        for (UniqueBookOffer discount : UNIQUE_BOOK_OFFERS) {
            if (differentBooksCount == discount.getUniqueCopies())
                return discount.getDiscount();
        }

        return defaultDiscount;
    }
}
