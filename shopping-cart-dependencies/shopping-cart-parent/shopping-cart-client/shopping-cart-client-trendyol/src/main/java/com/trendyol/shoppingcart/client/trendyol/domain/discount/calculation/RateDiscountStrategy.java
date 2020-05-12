package com.trendyol.shoppingcart.client.trendyol.domain.discount.calculation;

import com.trendyol.shoppingcart.client.trendyol.domain.value.Rate;
import com.trendyol.shoppingcart.core.domain.ShoppingCart;
import com.trendyol.shoppingcart.core.domain.discount.calculation.DiscountCalculationStrategy;
import com.trendyol.shoppingcart.core.domain.value.Amount;
import com.trendyol.shoppingcart.core.exception.InvalidValueException;

public class RateDiscountStrategy implements DiscountCalculationStrategy {

    protected final Rate discountRate;

    public RateDiscountStrategy(Rate discountRate) {

        if (discountRate == null) {
            throw new InvalidValueException("Rate of discount to be applied can not be null!");
        }
        this.discountRate = discountRate;
    }

    @Override
    public Amount calculateDiscountAmount(ShoppingCart shoppingCart) {
        return Amount.valueOf(shoppingCart.getCartAmount().multiply(discountRate.doubleValue()).divide(100D).doubleValue());

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RateDiscountStrategy that = (RateDiscountStrategy) o;

        return discountRate.equals(that.discountRate);
    }

    @Override
    public int hashCode() {
        return discountRate.hashCode();
    }

    @Override
    public String toString() {
        return "DiscountRate=" + discountRate + '%';
    }

    public Rate getDiscountRate() {
        return discountRate;
    }
}
