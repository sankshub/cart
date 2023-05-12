package com.sank.bookshop.services.service;

import com.sank.bookshop.repos.entity.Book;
import com.sank.bookshop.services.exceptions.DuplicateEntriesInCartException;
import com.sank.bookshop.services.model.*;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class UniqueSetOfBooksDiscount implements DiscountCalculationService {
    private static final List<UniqueBookOffer> UNIQUE_BOOK_OFFERS = new ArrayList<>();
    private static final Integer MINIMUM_BASKET_SIZE = 1;
    private static final String DUPLICATE_BOOK_ENTRY_ERROR = "Duplicate book entry found in Cart, Remove it and request again";

    private UniqueSetOfBooksDiscount() {
        UNIQUE_BOOK_OFFERS.add(new UniqueBookOffer(2, 5));
        UNIQUE_BOOK_OFFERS.add(new UniqueBookOffer(3, 10));
        UNIQUE_BOOK_OFFERS.add(new UniqueBookOffer(4, 20));
        UNIQUE_BOOK_OFFERS.add(new UniqueBookOffer(5, 25));
    }

    private static DiscountedCart getDiscountedCart(BookBasket bookBasket) {
        double totalPriceWithDiscount = 0.0;
        double pricePerSet = 0.0;
        double totalPriceWithoutDiscount = 0.0;
        for (BookSet bookSet : bookBasket.getSet()) {
            for (Book book : bookSet.getBooks()) {
                pricePerSet += Double.parseDouble(book.getPrice());
                totalPriceWithoutDiscount += Double.parseDouble(book.getPrice());
            }
            pricePerSet = pricePerSet * (1.0 - (bookSet.getDiscount() / 100.0));
            bookSet.setDiscountedCost(pricePerSet);
            totalPriceWithDiscount += pricePerSet;
            pricePerSet = 0;
        }
        return new DiscountedCart(bookBasket.getSet(), totalPriceWithDiscount, totalPriceWithoutDiscount);
    }

    private static TreeSet<Integer> getListOfBasketSizeForDiscount() {
        return new TreeSet<>(UNIQUE_BOOK_OFFERS.stream()
                                               .collect(Collectors.groupingBy(UniqueBookOffer::getUniqueCopies))
                                               .keySet());
    }

    @Override
    public List<UniqueBookOffer> getCurrentDiscountOffer() {
        return UNIQUE_BOOK_OFFERS;
    }

    @Override
    public DiscountedCart calculateDiscount(List<ShoppingOrder> shoppingOrderList) {
        checkDuplicateItemsInCart(shoppingOrderList);
        BookBasket bestBasket = getBestCombinationBookSets(shoppingOrderList);
        return getDiscountedCart(bestBasket);
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

    private BookBasket getBestCombinationBookSets(List<ShoppingOrder> shoppingCartItems) {
        List<BookBasket> bookBaskets = new ArrayList<>();
        for (Integer currentBasketSize : getListOfBasketSizeForDiscount().descendingSet()) {
            bookBaskets.add(new BookBasket(createPossibleSets(shoppingCartItems, currentBasketSize)));
        }
        return bookBaskets.size() == MINIMUM_BASKET_SIZE ? bookBaskets.get(0) : selectBooksSetsWithMaxDiscount(bookBaskets);
    }

    private List<BookSet> createPossibleSets(List<ShoppingOrder> shoppingCartItems, Integer maxBasketSize) {
        List<ShoppingOrder> remainingShoppingCartItems = cloneShoppingCartItems(shoppingCartItems);
        List<BookSet> setsOfDifferentBooks = new ArrayList<>();
        TreeSet<Integer> allowedBasketSizes = getListOfBasketSizeForDiscount();
        allowedBasketSizes.add(MINIMUM_BASKET_SIZE);

        while (!remainingShoppingCartItems.isEmpty()) {
            allowedBasketSizes.removeIf(integer -> integer > remainingShoppingCartItems.size() || integer > maxBasketSize);
            setsOfDifferentBooks.add(createNextSet(remainingShoppingCartItems, allowedBasketSizes.last()));
        }
        return setsOfDifferentBooks;
    }

    private BookSet createNextSet(List<ShoppingOrder> remainingShoppingCartItems, Integer maxSetSize) {
        HashSet<Book> books = new HashSet<>();

        for (ShoppingOrder item : new ArrayList<>(remainingShoppingCartItems)) {
            books.add(item.getBook());
            if (item.getQuantity() == 1)
                remainingShoppingCartItems.remove(item);
            else
                item.changeQuantity(item.getQuantity() - 1);
            if (books.size() == maxSetSize)
                break;
        }

        return new BookSet(books, getDiscount(books.size()));

    }

    private BookBasket selectBooksSetsWithMaxDiscount(List<BookBasket> bookBaskets) {
        TreeMap<Integer, BookBasket> combinationHashMap = new TreeMap<>();
        for (BookBasket combination : bookBaskets) {
            combinationHashMap.put(combination.getSet()
                                              .stream()
                                              .mapToInt(BookSet::getDiscount)
                                              .sum(), combination);
        }
        return combinationHashMap.lastEntry()
                                 .getValue();
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
