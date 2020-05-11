package com.trendyol.shoppingcart.client.trendyol.domain.discount;

import com.trendyol.shoppingcart.client.trendyol.domain.discount.validation.MinimumCartAmountValidationStrategy;
import com.trendyol.shoppingcart.core.domain.discount.Discount;
import com.trendyol.shoppingcart.core.domain.discount.calculation.AmountDiscountStrategy;
import com.trendyol.shoppingcart.core.domain.value.Amount;
import com.trendyol.shoppingcart.core.domain.value.DiscountName;

import java.util.Comparator;

public class MinimumCartAmountBasedAmountDiscount extends Discount {

    public MinimumCartAmountBasedAmountDiscount(Amount minimumCartAmount, Amount discountAmount) {
        super(discountName(), validationStrategy(minimumCartAmount), amountBasedCalculationStrategy(discountAmount));
    }

    private static DiscountName discountName() {
        String discountNameText = "MINIMUM_CART_AMOUNT_BASED_DISCOUNT";
        return DiscountName.valueOf(discountNameText);
    }

    private static MinimumCartAmountValidationStrategy validationStrategy(Amount minimumCartAmount) {
        return new MinimumCartAmountValidationStrategy(minimumCartAmount);
    }

    private static AmountDiscountStrategy amountBasedCalculationStrategy(Amount discountAmount) {
        return new AmountDiscountStrategy(discountAmount);
    }

    @Override
    public AmountDiscountStrategy getCalculationStrategy() {
        return (AmountDiscountStrategy) super.getCalculationStrategy();
    }

    @Override
    public MinimumCartAmountValidationStrategy getValidationStrategy() {
        return (MinimumCartAmountValidationStrategy) super.getValidationStrategy();
    }

    @Override
    public boolean isGreaterThan(Discount other) {
        if (other == null) {
            return true;
        }

        if (!(other instanceof MinimumCartAmountBasedAmountDiscount)) {
            return true;
        }

        MinimumCartAmountBasedAmountDiscount minimumCartAmountBasedAmountDiscount = (MinimumCartAmountBasedAmountDiscount) other;

        Comparator<MinimumCartAmountValidationStrategy> validationComparator = Comparator
                .comparing(MinimumCartAmountValidationStrategy::getMinimumCartAmount);

        Comparator<AmountDiscountStrategy> calculationComparator = Comparator
                .comparing(AmountDiscountStrategy::getDiscountAmount);

        return Comparator.comparing(MinimumCartAmountBasedAmountDiscount::getValidationStrategy, validationComparator)
                .thenComparing(MinimumCartAmountBasedAmountDiscount::getCalculationStrategy, calculationComparator)
                .compare(this, minimumCartAmountBasedAmountDiscount) > 0;
    }
}
