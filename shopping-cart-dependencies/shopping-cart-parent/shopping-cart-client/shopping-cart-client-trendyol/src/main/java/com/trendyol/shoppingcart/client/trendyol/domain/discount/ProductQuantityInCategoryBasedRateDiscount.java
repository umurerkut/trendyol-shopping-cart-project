package com.trendyol.shoppingcart.client.trendyol.domain.discount;

import com.trendyol.shoppingcart.client.trendyol.domain.discount.calculation.CategoryBasedRateDiscountStrategy;
import com.trendyol.shoppingcart.client.trendyol.domain.discount.validation.ProductQuantityInCategoryValidationStrategy;
import com.trendyol.shoppingcart.client.trendyol.domain.value.Rate;
import com.trendyol.shoppingcart.core.domain.Category;
import com.trendyol.shoppingcart.core.domain.discount.Discount;
import com.trendyol.shoppingcart.core.domain.value.DiscountName;
import com.trendyol.shoppingcart.core.domain.value.Quantity;
import com.trendyol.shoppingcart.core.exception.InvalidValueException;

import java.util.Comparator;

public class ProductQuantityInCategoryBasedRateDiscount extends Discount {

    public ProductQuantityInCategoryBasedRateDiscount(Category category, Quantity minimumProductQuantityInCategory, Rate discountRate) {
        super(discountName(category), validationStrategy(category, minimumProductQuantityInCategory), categoryBasedRateDiscountCalculationStrategy(category, discountRate));
    }

    private static DiscountName discountName(Category category) {
        if (category == null) {
            throw new InvalidValueException("Given category can not be null!");
        }
        String discountNameText = (category.getTitle().getValue() + "_CATEGORY_TYPE_BASED_DISCOUNT").replace(" ", "_").toUpperCase();
        return DiscountName.valueOf(discountNameText);
    }

    private static ProductQuantityInCategoryValidationStrategy validationStrategy(Category category, Quantity minimumProductQuantityInCategory) {
        return new ProductQuantityInCategoryValidationStrategy(category, minimumProductQuantityInCategory);
    }

    private static CategoryBasedRateDiscountStrategy categoryBasedRateDiscountCalculationStrategy(Category category, Rate discountRate) {
        return new CategoryBasedRateDiscountStrategy(category, discountRate);
    }

    @Override
    public CategoryBasedRateDiscountStrategy getCalculationStrategy() {
        return (CategoryBasedRateDiscountStrategy) super.getCalculationStrategy();
    }

    @Override
    public ProductQuantityInCategoryValidationStrategy getValidationStrategy() {
        return (ProductQuantityInCategoryValidationStrategy) super.getValidationStrategy();
    }

    @Override
    public boolean isGreaterThan(Discount other) {
        if (other == null) {
            return true;
        }

        if (!(other instanceof ProductQuantityInCategoryBasedRateDiscount)) {
            return true;
        }

        ProductQuantityInCategoryBasedRateDiscount productQuantityBasedMockDiscount = (ProductQuantityInCategoryBasedRateDiscount) other;

        if (productQuantityBasedMockDiscount.getValidationStrategy().getCategory().equals(getValidationStrategy().getCategory())) {
            Comparator<ProductQuantityInCategoryValidationStrategy> validationComparator = Comparator
                    .comparing(ProductQuantityInCategoryValidationStrategy::getMinimumProductQuantityInCategory);

            Comparator<CategoryBasedRateDiscountStrategy> calculationComparator = Comparator
                    .comparing(CategoryBasedRateDiscountStrategy::getDiscountRate);

            return Comparator.comparing(ProductQuantityInCategoryBasedRateDiscount::getValidationStrategy, validationComparator)
                    .thenComparing(ProductQuantityInCategoryBasedRateDiscount::getCalculationStrategy, calculationComparator)
                    .compare(this, productQuantityBasedMockDiscount) > 0;
        } else {
            return true;
        }
    }
}
