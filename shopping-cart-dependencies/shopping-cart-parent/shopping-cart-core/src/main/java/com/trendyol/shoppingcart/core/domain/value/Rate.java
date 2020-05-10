package com.trendyol.shoppingcart.core.domain.value;

import com.trendyol.shoppingcart.core.exception.InvalidValueException;

import java.util.Comparator;
import java.util.Objects;

/*
Rate value object
 */
public final class Rate implements Comparable<Rate> {

    private final Double value;

    private Rate(Double value) {

        if (value < 0D) {
            throw new InvalidValueException("Rate value can not be less than " + 0D + '!');
        }

        if (value > 100D) {
            throw new InvalidValueException("Rate value can not be greater than " + 100D + '!');
        }

        this.value = value;

    }

    public static Rate valueOf(Double value) {
        if (value == null) {
            return ofZero();
        }
        return new Rate(value);
    }

    public static Rate ofZero() {
        return new Rate(0D);
    }

    public Integer intValue() {
        return this.value.intValue();
    }

    public Double doubleValue() {
        return this.value;
    }

    public boolean isZero() {
        return this.value == 0D;
    }

    public boolean isLessThan(Rate other) {
        return this.value < other.doubleValue();
    }

    public boolean isGreaterThan(Rate other) {
        return this.value > other.doubleValue();
    }

    public Rate add(Rate other) {
        return Rate.valueOf(this.value + other.doubleValue());
    }

    public Rate subtract(Rate other) {
        return Rate.valueOf(this.value - other.doubleValue());
    }

    public Rate multiply(Double multiplier) {
        return Rate.valueOf(this.value * multiplier);
    }

    public Rate divide(Double divisor) {
        if (divisor == 0) {
            throw new InvalidValueException("Divisor value can not be negative!");
        }
        return Rate.valueOf(this.value / divisor);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rate rate = (Rate) o;
        return value.equals(rate.value);
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
    public int compareTo(Rate o) {
        return Comparator.comparing(Rate::doubleValue).compare(this, o);
    }
}
