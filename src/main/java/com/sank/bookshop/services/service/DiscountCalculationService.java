package com.sank.bookshop.services.service;

import com.sank.bookshop.services.model.DiscountOffer;
import com.sank.bookshop.services.model.DiscountedCart;
import com.sank.bookshop.services.model.ShoppingOrder;

import java.util.List;
public interface DiscountCalculationService {
    DiscountedCart calculateDiscount(List<ShoppingOrder> shoppingOrderList);

    List<DiscountOffer> getCurrentDiscountOffer();
}
