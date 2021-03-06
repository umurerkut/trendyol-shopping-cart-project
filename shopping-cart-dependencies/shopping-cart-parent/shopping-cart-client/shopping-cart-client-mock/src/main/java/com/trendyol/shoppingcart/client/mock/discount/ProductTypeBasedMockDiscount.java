package com.trendyol.shoppingcart.client.mock.discount;

import com.trendyol.shoppingcart.client.mock.discount.validation.ProductTypeBasedMockDiscountValidationStrategy;
import com.trendyol.shoppingcart.core.domain.Product;
import com.trendyol.shoppingcart.core.domain.discount.Discount;
import com.trendyol.shoppingcart.core.domain.discount.calculation.AmountDiscountStrategy;
import com.trendyol.shoppingcart.core.domain.value.Amount;
import com.trendyol.shoppingcart.core.domain.value.DiscountName;
import com.trendyol.shoppingcart.core.exception.InvalidValueException;

import java.util.Comparator;

//Mock discount: If more specific product exists in cart, then apply discount amount of dismountAmount
public class ProductTypeBasedMockDiscount extends Discount {

    public ProductTypeBasedMockDiscount(Product product, Amount discountAmount) {
        super(discountName(product), validationStrategy(product), calculationStrategy(discountAmount));
    }

    private static DiscountName discountName(Product product) {
        if (product == null) {
            throw new InvalidValueException("Given product can not be null!");
        }
        String discountNameText = (product.getTitle().getValue() + "_PRODUCT_TYPE_BASED_MOCK_DISCOUNT").replace(" ", "_").toUpperCase();
        return DiscountName.valueOf(discountNameText);
    }

    private static ProductTypeBasedMockDiscountValidationStrategy validationStrategy(Product product) {
        return new ProductTypeBasedMockDiscountValidationStrategy(product);
    }

    private static AmountDiscountStrategy calculationStrategy(Amount discountAmount) {
        return new AmountDiscountStrategy(discountAmount);
    }

    @Override
    public AmountDiscountStrategy getCalculationStrategy() {
        return (AmountDiscountStrategy) super.getCalculationStrategy();
    }

    @Override
    public ProductTypeBasedMockDiscountValidationStrategy getValidationStrategy() {
        return (ProductTypeBasedMockDiscountValidationStrategy) super.getValidationStrategy();
    }

    @Override
    public boolean isGreaterThan(Discount other) {
        if (other == null) {
            return true;
        }

        if (!(other instanceof ProductTypeBasedMockDiscount)) {
            return true;
        }

        ProductTypeBasedMockDiscount productQuantityBasedMockDiscount = (ProductTypeBasedMockDiscount) other;

        Comparator<AmountDiscountStrategy> calculationComparator = Comparator
                .comparing(AmountDiscountStrategy::getDiscountAmount);

        return Comparator
                .comparing(ProductTypeBasedMockDiscount::getCalculationStrategy, calculationComparator)
                .compare(this, productQuantityBasedMockDiscount) > 0;
    }
}
