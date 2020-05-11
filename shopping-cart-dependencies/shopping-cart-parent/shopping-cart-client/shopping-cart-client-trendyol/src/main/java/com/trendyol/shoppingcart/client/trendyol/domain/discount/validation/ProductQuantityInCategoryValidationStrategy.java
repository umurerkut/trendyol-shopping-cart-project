package com.trendyol.shoppingcart.client.trendyol.domain.discount.validation;

import com.trendyol.shoppingcart.core.domain.Category;
import com.trendyol.shoppingcart.core.domain.ShoppingCart;
import com.trendyol.shoppingcart.core.domain.discount.validation.DiscountValidationStrategy;
import com.trendyol.shoppingcart.core.domain.value.Quantity;
import com.trendyol.shoppingcart.core.exception.InvalidValueException;

public class ProductQuantityInCategoryValidationStrategy implements DiscountValidationStrategy {

    private final Category category;
    private final Quantity minimumProductQuantityInCategory;

    public ProductQuantityInCategoryValidationStrategy(Category category, Quantity minimumProductQuantityInCategory) {

        if (category == null) {
            throw new InvalidValueException("Given category can not be null!");
        }

        if (minimumProductQuantityInCategory == null) {
            throw new InvalidValueException("Minimum product quantity in category can not be null!");
        }

        this.category = category;
        this.minimumProductQuantityInCategory = minimumProductQuantityInCategory;
    }


    @Override
    public boolean isValid(ShoppingCart shoppingCart) {
        Quantity productQuantityInCategory = shoppingCart.getQuantityOfProductsBelongsToCategoryInCart(category);
        return (productQuantityInCategory.isGreaterThan(minimumProductQuantityInCategory) || productQuantityInCategory.equals(minimumProductQuantityInCategory));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProductQuantityInCategoryValidationStrategy that = (ProductQuantityInCategoryValidationStrategy) o;

        if (!category.equals(that.category)) return false;
        return minimumProductQuantityInCategory.equals(that.minimumProductQuantityInCategory);
    }

    @Override
    public int hashCode() {
        int result = category.hashCode();
        result = 31 * result + minimumProductQuantityInCategory.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Category=" + category.getTitle() +
                ", MinimumProductQuantityInCategory=" + minimumProductQuantityInCategory;
    }

    public Category getCategory() {
        return category;
    }

    public Quantity getMinimumProductQuantityInCategory() {
        return minimumProductQuantityInCategory;
    }
}
