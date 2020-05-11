package com.trendyol.shoppingcart.core.domain.discount.calculation;

import com.trendyol.shoppingcart.core.domain.ShoppingCart;
import com.trendyol.shoppingcart.core.domain.value.Amount;
import com.trendyol.shoppingcart.core.exception.InvalidValueException;

public class AmountDiscountStrategy implements DiscountCalculationStrategy {

    private final Amount discountAmount;

    public AmountDiscountStrategy(Amount discountAmount) {
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

    @Override
    public Amount calculateDiscountAmount(ShoppingCart shoppingCart) {
        return discountAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AmountDiscountStrategy that = (AmountDiscountStrategy) o;

        return discountAmount.equals(that.discountAmount);
    }

    @Override
    public int hashCode() {
        return discountAmount.hashCode();
    }

    @Override
    public String toString() {
        return "DiscountAmount=" + discountAmount;
    }

    public Amount getDiscountAmount() {
        return discountAmount;
    }
}
