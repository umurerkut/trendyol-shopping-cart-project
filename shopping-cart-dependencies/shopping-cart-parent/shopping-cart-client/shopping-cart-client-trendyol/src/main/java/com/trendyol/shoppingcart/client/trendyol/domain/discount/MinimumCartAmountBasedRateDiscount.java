package com.trendyol.shoppingcart.client.trendyol.domain.discount;

import com.trendyol.shoppingcart.client.trendyol.domain.discount.calculation.RateDiscountStrategy;
import com.trendyol.shoppingcart.client.trendyol.domain.discount.validation.MinimumCartAmountValidationStrategy;
import com.trendyol.shoppingcart.client.trendyol.domain.value.Rate;
import com.trendyol.shoppingcart.core.domain.discount.Discount;
import com.trendyol.shoppingcart.core.domain.value.Amount;
import com.trendyol.shoppingcart.core.domain.value.DiscountName;

import java.util.Comparator;

public class MinimumCartAmountBasedRateDiscount extends Discount {

    public MinimumCartAmountBasedRateDiscount(Amount minimumCartAmount, Rate discountRate) {
        super(discountName(), validationStrategy(minimumCartAmount), rateBasedCalculationStrategy(discountRate));
    }

    private static DiscountName discountName() {
        String discountNameText = "MINIMUM_CART_AMOUNT_BASED_DISCOUNT";
        return DiscountName.valueOf(discountNameText);
    }

    private static MinimumCartAmountValidationStrategy validationStrategy(Amount minimumCartAmount) {
        return new MinimumCartAmountValidationStrategy(minimumCartAmount);
    }

    private static RateDiscountStrategy rateBasedCalculationStrategy(Rate discountRate) {
        return new RateDiscountStrategy(discountRate);
    }

    @Override
    public RateDiscountStrategy getCalculationStrategy() {
        return (RateDiscountStrategy) super.getCalculationStrategy();
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

        if (!(other instanceof MinimumCartAmountBasedRateDiscount)) {
            return true;
        }

        MinimumCartAmountBasedRateDiscount minimumCartAmountBasedRateDiscount = (MinimumCartAmountBasedRateDiscount) other;

        Comparator<MinimumCartAmountValidationStrategy> validationComparator = Comparator
                .comparing(MinimumCartAmountValidationStrategy::getMinimumCartAmount);

        Comparator<RateDiscountStrategy> calculationComparator = Comparator
                .comparing(RateDiscountStrategy::getDiscountRate);

        return Comparator.comparing(MinimumCartAmountBasedRateDiscount::getValidationStrategy, validationComparator)
                .thenComparing(MinimumCartAmountBasedRateDiscount::getCalculationStrategy, calculationComparator)
                .compare(this, minimumCartAmountBasedRateDiscount) > 0;
    }
}
