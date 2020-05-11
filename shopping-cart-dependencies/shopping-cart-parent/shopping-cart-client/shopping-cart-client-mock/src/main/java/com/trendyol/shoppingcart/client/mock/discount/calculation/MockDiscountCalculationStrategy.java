package com.trendyol.shoppingcart.client.mock.discount.calculation;

import com.trendyol.shoppingcart.core.domain.ShoppingCart;
import com.trendyol.shoppingcart.core.domain.discount.calculation.DiscountCalculationStrategy;
import com.trendyol.shoppingcart.core.domain.value.Amount;
import com.trendyol.shoppingcart.core.exception.InvalidValueException;

public class MockDiscountCalculationStrategy implements DiscountCalculationStrategy {

    private final Amount discountAmount;

    public MockDiscountCalculationStrategy(Amount discountAmount) {
        if (discountAmount == null) {
            throw new InvalidValueException("Amount of discount to be applied can not be null!");
        }
        if (discountAmount.isZero()) {
            throw new InvalidValueException("Amount of discount to be applied can not be zero!");
        }
        if (discountAmount.isNegative()) {
            throw new InvalidValueException("Amount of discount to be applied can not be negative!");
        }
        this.discountAmount = discountAmount;
    }

    //Apply discount amount of discountAmount if discount is valid
    @Override
    public Amount calculateDiscountAmount(ShoppingCart shoppingCart) {
        return discountAmount;
    }

    public Amount getDiscountAmount() {
        return discountAmount;
    }
}
