package com.trendyol.shoppingcart.core.domain.discount.calculation;

import com.trendyol.shoppingcart.core.domain.ShoppingCart;
import com.trendyol.shoppingcart.core.domain.value.Amount;

public interface DiscountCalculationStrategy {

    Amount calculateDiscountAmount(ShoppingCart shoppingCart);
}
