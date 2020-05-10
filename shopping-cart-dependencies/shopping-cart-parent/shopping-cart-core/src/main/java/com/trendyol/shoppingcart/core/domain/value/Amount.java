package com.trendyol.shoppingcart.core.domain.value;

import com.trendyol.shoppingcart.core.exception.InvalidValueException;

import java.util.Comparator;
import java.util.Objects;

/*
Amount value object
 */
public final class Amount implements Comparable<Amount> {

    private final Double value;

    private Amount(Double value) {
        this.value = value;
    }

    public static Amount valueOf(Double value) {
        if (value == null) {
            return ofZero();
        }
        return new Amount(value);
    }

    public static Amount ofZero() {
        return new Amount(0D);
    }

    public Integer intValue() {
        return this.value.intValue();
    }

    public Double doubleValue() {
        return this.value;
    }

    public boolean isNegative() {
        return this.value < 0D;
    }

    public boolean isPositive() {
        return this.value > 0D;
    }

    public boolean isZero() {
        return this.value == 0D;
    }

    public boolean isLessThan(Amount other) {
        return this.value < other.doubleValue();
    }

    public boolean isGreaterThan(Amount other) {
        return this.value > other.doubleValue();
    }

    public Amount add(Amount other) {
        return Amount.valueOf(this.value + other.doubleValue());
    }

    public Amount subtract(Amount other) {
        return Amount.valueOf(this.value - other.doubleValue());
    }

    public Amount multiply(Double multiplier) {
        return Amount.valueOf(this.value * multiplier);
    }

    public Amount divide(Double divisor) {
        if (divisor == 0) {
            throw new InvalidValueException("Divisor value can not be negative!");
        }
        return Amount.valueOf(this.value / divisor);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Amount amount = (Amount) o;
        return value.equals(amount.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public int compareTo(Amount o) {
        return Comparator.comparing(Amount::doubleValue).compare(this, o);
    }
}
