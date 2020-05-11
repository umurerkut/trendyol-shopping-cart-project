package com.trendyol.shoppingcart.client.trendyol.domain.discount.validation;

import com.trendyol.shoppingcart.core.domain.ShoppingCart;
import com.trendyol.shoppingcart.core.domain.discount.validation.DiscountValidationStrategy;
import com.trendyol.shoppingcart.core.domain.value.Amount;
import com.trendyol.shoppingcart.core.exception.InvalidValueException;

public class MinimumCartAmountValidationStrategy implements DiscountValidationStrategy {

    protected final Amount minimumCartAmount;

    public MinimumCartAmountValidationStrategy(Amount minimumCartAmount) {

        if (minimumCartAmount == null) {
            throw new InvalidValueException("Minimum cart amount can not be null!");
        }

        if (minimumCartAmount.isNegative()) {
            throw new InvalidValueException("Minimum cart amount can not be negative!");
        }

        this.minimumCartAmount = minimumCartAmount;
    }

    @Override
    public boolean isValid(ShoppingCart shoppingCart) {
        return (shoppingCart.getCartAmount().isGreaterThan(minimumCartAmount) || shoppingCart.getCartAmount().equals(minimumCartAmount));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MinimumCartAmountValidationStrategy that = (MinimumCartAmountValidationStrategy) o;

        return minimumCartAmount.equals(that.minimumCartAmount);
    }

    @Override
    public int hashCode() {
        return minimumCartAmount.hashCode();
    }

    @Override
    public String toString() {
        return "MinimumCartAmount=" + minimumCartAmount;
    }

    public Amount getMinimumCartAmount() {
        return minimumCartAmount;
    }
}
