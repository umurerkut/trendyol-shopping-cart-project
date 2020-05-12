package com.trendyol.shoppingcart.client.trendyol.domain.discount.calculation;

import com.trendyol.shoppingcart.client.trendyol.domain.value.Rate;
import com.trendyol.shoppingcart.core.domain.Category;
import com.trendyol.shoppingcart.core.domain.ShoppingCart;
import com.trendyol.shoppingcart.core.domain.value.Amount;
import com.trendyol.shoppingcart.core.exception.InvalidValueException;

public class CategoryBasedRateDiscountStrategy extends RateDiscountStrategy {

    private final Category category;

    public CategoryBasedRateDiscountStrategy(Category category, Rate discountRate) {
        super(discountRate);

        if (category == null) {
            throw new InvalidValueException("Category of discount to be applied can not be null!");
        }

        this.category = category;
    }

    @Override
    public Amount calculateDiscountAmount(ShoppingCart shoppingCart) {
        Amount categoryProductTotalPrice = shoppingCart.getTotalPriceOfProductsBelongToCategoryInCart(category);
        return Amount.valueOf(categoryProductTotalPrice.multiply(discountRate.doubleValue()).divide(100D).doubleValue());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        CategoryBasedRateDiscountStrategy that = (CategoryBasedRateDiscountStrategy) o;

        return category.equals(that.category);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + category.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Category=" + category.getTitle() +
                ", DiscountRate=" + discountRate + '%';
    }

    public Category getCategory() {
        return category;
    }
}
