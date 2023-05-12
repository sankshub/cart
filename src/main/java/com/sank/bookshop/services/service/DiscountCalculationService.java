package com.sank.bookshop.services.service;

import com.sank.bookshop.services.model.DiscountedCart;
import com.sank.bookshop.services.model.ShoppingOrder;
import com.sank.bookshop.services.model.UniqueBookOffer;

import java.util.List;
public interface DiscountCalculationService {
    DiscountedCart calculateDiscount(List<ShoppingOrder> shoppingOrderList);
    List<UniqueBookOffer> getCurrentDiscountOffer();
}
