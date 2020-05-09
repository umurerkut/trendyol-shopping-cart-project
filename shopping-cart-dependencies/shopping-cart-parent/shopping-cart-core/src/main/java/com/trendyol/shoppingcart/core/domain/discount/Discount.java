package com.trendyol.shoppingcart.core.domain.discount;

import com.trendyol.shoppingcart.core.domain.ShoppingCart;
import com.trendyol.shoppingcart.core.domain.discount.calculation.DiscountCalculationStrategy;
import com.trendyol.shoppingcart.core.domain.discount.validation.DiscountValidationStrategy;
import com.trendyol.shoppingcart.core.domain.value.Amount;
import com.trendyol.shoppingcart.core.domain.value.DiscountName;
import com.trendyol.shoppingcart.core.exception.InvalidValueException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Discount<V extends DiscountValidationStrategy, C extends DiscountCalculationStrategy> {

    private final static Logger logger = LoggerFactory.getLogger(Discount.class);

    protected final DiscountName discountName;
    protected final V validationStrategy;
    protected final C calculationStrategy;

    protected Amount discountAmount;


    public Discount(DiscountName discountName, V validationStrategy, C calculationStrategy) {

        if (discountName == null) {
            throw new InvalidValueException("Discount name an not be null!");
        }

        if (validationStrategy == null) {
            throw new InvalidValueException("Validation strategy can not be null!");
        }

        if (calculationStrategy == null) {
            throw new InvalidValueException("Calculation strategy can not be null!");
        }

        this.discountName = discountName;
        this.validationStrategy = validationStrategy;
        this.calculationStrategy = calculationStrategy;
        this.discountAmount = Amount.ofZero();
    }

    public void addTo(ShoppingCart shoppingCart) {
        if (validationStrategy.isValid(shoppingCart)) {
            this.discountAmount = calculationStrategy.calculateDiscountAmount(shoppingCart);
            Discount<V, C> discount = shoppingCart.getDiscountMap().get(discountName);
            if (this.isGreaterThan(discount)) {
                shoppingCart.getDiscountMap().put(discountName, this);
            } else {
                logger.info("{} overrides {}.", discount, this);
            }
        } else {
            logger.info("Discount does not meet with validation: {}.", this);
        }
    }

    public V getValidationStrategy() {
        return validationStrategy;
    }

    public C getCalculationStrategy() {
        return calculationStrategy;
    }

    public Amount getDiscountAmount() {
        return discountAmount;
    }

    public DiscountName getDiscountName() {
        return discountName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Discount discount = (Discount) o;

        if (!validationStrategy.equals(discount.validationStrategy)) return false;
        if (!calculationStrategy.equals(discount.calculationStrategy)) return false;
        if (!discountAmount.equals(discount.discountAmount)) return false;
        return discountName.equals(discount.discountName);
    }

    @Override
    public int hashCode() {
        int result = validationStrategy.hashCode();
        result = 31 * result + calculationStrategy.hashCode();
        result = 31 * result + discountAmount.hashCode();
        result = 31 * result + discountName.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "DiscountName=" + discountName +
                ", CalculatedDiscountAmount=" + discountAmount +
                ", " + validationStrategy +
                ", " + calculationStrategy;
    }

    public abstract boolean isGreaterThan(Discount<V, C> other);
}
