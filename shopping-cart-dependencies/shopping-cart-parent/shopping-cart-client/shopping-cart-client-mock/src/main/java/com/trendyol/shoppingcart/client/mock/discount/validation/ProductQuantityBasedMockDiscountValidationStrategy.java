package com.trendyol.shoppingcart.client.mock.discount.validation;

import com.trendyol.shoppingcart.core.domain.ShoppingCart;
import com.trendyol.shoppingcart.core.domain.discount.validation.DiscountValidationStrategy;
import com.trendyol.shoppingcart.core.domain.value.Quantity;
import com.trendyol.shoppingcart.core.exception.InvalidValueException;

public class ProductQuantityBasedMockDiscountValidationStrategy implements DiscountValidationStrategy {

    private final Quantity minimumQuantityOfProducts;

    public ProductQuantityBasedMockDiscountValidationStrategy(Quantity minimumQuantityOfProducts) {
        if (minimumQuantityOfProducts == null) {
            throw new InvalidValueException("Minimum quantity value of products in cart can not be null!");
        }
        if (minimumQuantityOfProducts.isZero()) {
            throw new InvalidValueException("Minimum quantity value of products in cart can not be zero!");
        }
        this.minimumQuantityOfProducts = minimumQuantityOfProducts;
    }

    //If cart has more than minimumQuantityOfProducts items, then apply discount
    @Override
    public boolean isValid(ShoppingCart shoppingCart) {
        Quantity totalQuantityOfProductsInCart = shoppingCart.getTotalQuantityOfProductsInCart();
        return totalQuantityOfProductsInCart.isGreaterThan(minimumQuantityOfProducts)
                || totalQuantityOfProductsInCart.equals(minimumQuantityOfProducts);
    }

    public Quantity getMinimumQuantityOfProducts() {
        return minimumQuantityOfProducts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProductQuantityBasedMockDiscountValidationStrategy that = (ProductQuantityBasedMockDiscountValidationStrategy) o;

        return minimumQuantityOfProducts.equals(that.minimumQuantityOfProducts);
    }

    @Override
    public int hashCode() {
        return minimumQuantityOfProducts.hashCode();
    }

    @Override
    public String toString() {
        return "MinimumQuantityOfProducts=" + minimumQuantityOfProducts;
    }
}
