package com.trendyol.shoppingcart.core.domain.discount;

import com.trendyol.shoppingcart.core.domain.discount.calculation.MockDiscountCalculationStrategy;
import com.trendyol.shoppingcart.core.domain.discount.validation.ProductQuantityBasedMockDiscountValidationStrategy;
import com.trendyol.shoppingcart.core.domain.value.Amount;
import com.trendyol.shoppingcart.core.domain.value.DiscountName;
import com.trendyol.shoppingcart.core.domain.value.Quantity;
import com.trendyol.shoppingcart.core.exception.InvalidValueException;

import java.util.Comparator;

//Mock discount: If more then or same number of minimumQuantityOfProducts items in cart, then apply discount amount of dismountAmount
public class ProductQuantityBasedMockDiscount extends Discount<ProductQuantityBasedMockDiscountValidationStrategy, MockDiscountCalculationStrategy> {

    public ProductQuantityBasedMockDiscount(Quantity minimumQuantityOfProducts, Amount discountAmount) {
        super(discountName(), validationStrategy(minimumQuantityOfProducts), calculationStrategy(discountAmount));
    }

    private static DiscountName discountName() {
        return DiscountName.valueOf("PRODUCT_QUANTITY_BASED_MOCK_DISCOUNT");
    }

    private static ProductQuantityBasedMockDiscountValidationStrategy validationStrategy(Quantity minimumQuantityOfProducts) {
        if (minimumQuantityOfProducts == null) {
            throw new InvalidValueException("Minimum quantity value of products in cart can not be null!");
        }
        if (minimumQuantityOfProducts.isZero()) {
            throw new InvalidValueException("Minimum quantity value of products in cart can not be zero!");
        }
        return new ProductQuantityBasedMockDiscountValidationStrategy(minimumQuantityOfProducts);
    }

    private static MockDiscountCalculationStrategy calculationStrategy(Amount discountAmount) {
        if (discountAmount == null) {
            throw new InvalidValueException("Amount of discount to be applied can not be null!");
        }
        if (discountAmount.isZero()) {
            throw new InvalidValueException("Amount of discount to be applied can not be zero!");
        }
        if (discountAmount.isNegative()) {
            throw new InvalidValueException("Amount of discount to be applied can not be negative!");
        }
        return new MockDiscountCalculationStrategy(discountAmount);
    }

    @Override
    public boolean isGreaterThan(Discount<ProductQuantityBasedMockDiscountValidationStrategy, MockDiscountCalculationStrategy> other) {
        if (other == null) {
            return true;
        }
        ProductQuantityBasedMockDiscount productQuantityBasedMockDiscount = (ProductQuantityBasedMockDiscount) other;

        Comparator<ProductQuantityBasedMockDiscountValidationStrategy> validationComparator = Comparator
                .comparing(ProductQuantityBasedMockDiscountValidationStrategy::getMinimumQuantityOfProducts);

        Comparator<MockDiscountCalculationStrategy> calculationComparator = Comparator
                .comparing(MockDiscountCalculationStrategy::getDiscountAmount);

        return Comparator.comparing(ProductQuantityBasedMockDiscount::getValidationStrategy, validationComparator)
                .thenComparing(ProductQuantityBasedMockDiscount::getCalculationStrategy, calculationComparator)
                .compare(this, productQuantityBasedMockDiscount) > 0;
    }
}
