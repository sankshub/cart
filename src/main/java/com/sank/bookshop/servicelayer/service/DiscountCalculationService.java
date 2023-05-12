package com.sank.bookshop.servicelayer.service;

import com.sank.bookshop.servicelayer.model.DiscountedCart;
import com.sank.bookshop.servicelayer.model.ShoppingOrder;
import com.sank.bookshop.servicelayer.model.UniqueBookOffer;

import java.util.List;
public interface DiscountCalculationService {
    DiscountedCart calculateDiscount(List<ShoppingOrder> shoppingOrderList);
    List<UniqueBookOffer> getCurrentDiscountOffer();
}
