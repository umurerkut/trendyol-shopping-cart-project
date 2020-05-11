package com.trendyol.shoppingcart.client.mock.discount;

import com.trendyol.shoppingcart.client.mock.discount.calculation.MockDiscountCalculationStrategy;
import com.trendyol.shoppingcart.client.mock.discount.validation.ProductQuantityBasedMockDiscountValidationStrategy;
import com.trendyol.shoppingcart.core.domain.discount.Discount;
import com.trendyol.shoppingcart.core.domain.value.Amount;
import com.trendyol.shoppingcart.core.domain.value.DiscountName;
import com.trendyol.shoppingcart.core.domain.value.Quantity;

import java.util.Comparator;

//Mock discount: If more then or same number of minimumQuantityOfProducts items in cart, then apply discount amount of dismountAmount
public class ProductQuantityBasedMockDiscount extends Discount {

    public ProductQuantityBasedMockDiscount(Quantity minimumQuantityOfProducts, Amount discountAmount) {
        super(discountName(), validationStrategy(minimumQuantityOfProducts), calculationStrategy(discountAmount));
    }

    private static DiscountName discountName() {
        return DiscountName.valueOf("PRODUCT_QUANTITY_BASED_MOCK_DISCOUNT");
    }

    private static ProductQuantityBasedMockDiscountValidationStrategy validationStrategy(Quantity minimumQuantityOfProducts) {
        return new ProductQuantityBasedMockDiscountValidationStrategy(minimumQuantityOfProducts);
    }

    private static MockDiscountCalculationStrategy calculationStrategy(Amount discountAmount) {
        return new MockDiscountCalculationStrategy(discountAmount);
    }

    @Override
    public MockDiscountCalculationStrategy getCalculationStrategy() {
        return (MockDiscountCalculationStrategy) super.getCalculationStrategy();
    }

    @Override
    public ProductQuantityBasedMockDiscountValidationStrategy getValidationStrategy() {
        return (ProductQuantityBasedMockDiscountValidationStrategy) super.getValidationStrategy();
    }

    @Override
    public boolean isGreaterThan(Discount other) {
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
