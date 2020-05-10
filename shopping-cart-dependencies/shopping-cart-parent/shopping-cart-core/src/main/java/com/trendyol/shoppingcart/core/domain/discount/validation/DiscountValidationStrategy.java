package com.trendyol.shoppingcart.core.domain.discount.validation;

import com.trendyol.shoppingcart.core.domain.ShoppingCart;

public interface DiscountValidationStrategy {

    boolean isValid(ShoppingCart shoppingCart);
}
