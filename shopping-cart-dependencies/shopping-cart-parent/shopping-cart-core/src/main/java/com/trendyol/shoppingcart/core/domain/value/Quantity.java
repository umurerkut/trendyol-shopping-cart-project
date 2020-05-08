package com.trendyol.shoppingcart.core.domain.value;

import com.trendyol.shoppingcart.core.exception.InvalidValueException;

import java.util.Comparator;
import java.util.Objects;

/*
Quantity value object
 */
public final class Quantity implements Comparable<Quantity> {

    private final Integer value;

    private Quantity(Integer value) {

        if (value < 0) {
            throw new InvalidValueException("Quantity value can not be negative!");
        }

        this.value = value;
    }

    public static Quantity valueOf(Integer value) {
        if (value == null) {
            return ofZero();
        }
        return new Quantity(value);
    }

    public static Quantity ofZero() {
        return new Quantity(0);
    }

    public Quantity add(Quantity other) {
        return Quantity.valueOf(this.value + other.intValue());
    }

    public Quantity subtract(Quantity other) {
        return Quantity.valueOf(this.value - other.intValue());
    }

    public Quantity multiply(Integer multiplier) {
        return Quantity.valueOf(this.value * multiplier);
    }

    public Quantity divide(Integer divisor) {
        if (divisor == 0) {
            throw new InvalidValueException("Divisor value can not be negative!");
        }
        return Quantity.valueOf(this.value / divisor);
    }

    public Integer intValue() {
        return this.value;
    }

    public Double doubleValue() {
        return this.value.doubleValue();
    }

    public Boolean isZero() {
        return this.value == 0;
    }

    public Boolean isLessThan(Quantity other) {
        return this.value < other.intValue();
    }

    public Boolean isGreaterThan(Quantity other) {
        return this.value > other.intValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Quantity quantity = (Quantity) o;
        return value.equals(quantity.value);
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
    public int compareTo(Quantity o) {
        return Comparator.comparing(Quantity::intValue).compare(this, o);
    }
}
