package com.trendyol.shoppingcart.client.trendyol.domain.discount;

import com.trendyol.shoppingcart.client.trendyol.domain.discount.validation.ProductQuantityInCategoryValidationStrategy;
import com.trendyol.shoppingcart.core.domain.Category;
import com.trendyol.shoppingcart.core.domain.discount.Discount;
import com.trendyol.shoppingcart.core.domain.discount.calculation.AmountDiscountStrategy;
import com.trendyol.shoppingcart.core.domain.value.Amount;
import com.trendyol.shoppingcart.core.domain.value.DiscountName;
import com.trendyol.shoppingcart.core.domain.value.Quantity;
import com.trendyol.shoppingcart.core.exception.InvalidValueException;

import java.util.Comparator;

public class ProductQuantityInCategoryBasedAmountDiscount extends Discount {

    public ProductQuantityInCategoryBasedAmountDiscount(Category category, Quantity minimumProductQuantityInCategory, Amount discountAmount) {
        super(discountName(category), validationStrategy(category, minimumProductQuantityInCategory), amountBasedCalculationStrategy(discountAmount));
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

    private static AmountDiscountStrategy amountBasedCalculationStrategy(Amount discountAmount) {
        return new AmountDiscountStrategy(discountAmount);
    }

    @Override
    public AmountDiscountStrategy getCalculationStrategy() {
        return (AmountDiscountStrategy) super.getCalculationStrategy();
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

        if (!(other instanceof ProductQuantityInCategoryBasedAmountDiscount)) {
            return true;
        }

        ProductQuantityInCategoryBasedAmountDiscount productQuantityBasedMockDiscount = (ProductQuantityInCategoryBasedAmountDiscount) other;

        if (productQuantityBasedMockDiscount.getValidationStrategy().getCategory().equals(getValidationStrategy().getCategory())) {
            Comparator<ProductQuantityInCategoryValidationStrategy> validationComparator = Comparator
                    .comparing(ProductQuantityInCategoryValidationStrategy::getMinimumProductQuantityInCategory);

            Comparator<AmountDiscountStrategy> calculationComparator = Comparator
                    .comparing(AmountDiscountStrategy::getDiscountAmount);

            return Comparator.comparing(ProductQuantityInCategoryBasedAmountDiscount::getValidationStrategy, validationComparator)
                    .thenComparing(ProductQuantityInCategoryBasedAmountDiscount::getCalculationStrategy, calculationComparator)
                    .compare(this, productQuantityBasedMockDiscount) > 0;
        } else {
            return true;
        }
    }
}
