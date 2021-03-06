package com.trendyol.shoppingcart.client.mock.discount.validation;

import com.trendyol.shoppingcart.core.domain.Product;
import com.trendyol.shoppingcart.core.domain.ShoppingCart;
import com.trendyol.shoppingcart.core.domain.discount.validation.DiscountValidationStrategy;
import com.trendyol.shoppingcart.core.domain.value.Quantity;
import com.trendyol.shoppingcart.core.exception.InvalidValueException;

public class ProductTypeBasedMockDiscountValidationStrategy implements DiscountValidationStrategy {

    private final Product product;

    public ProductTypeBasedMockDiscountValidationStrategy(Product product) {
        if (product == null) {
            throw new InvalidValueException("Given product can not be null!");
        }
        this.product = product;
    }

    //If cart specific product, then apply discount
    @Override
    public boolean isValid(ShoppingCart shoppingCart) {
        return shoppingCart.getQuantityOfProductInCart(product).isGreaterThan(Quantity.ofZero());
    }

    public Product getProduct() {
        return product;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProductTypeBasedMockDiscountValidationStrategy that = (ProductTypeBasedMockDiscountValidationStrategy) o;

        return product.equals(that.product);
    }

    @Override
    public int hashCode() {
        return product.hashCode();
    }

    @Override
    public String toString() {
        return "Product=" + product;
    }
}
