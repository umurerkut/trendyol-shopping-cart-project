package com.trendyol.shoppingcart.core.domain.value;

import com.trendyol.shoppingcart.core.exception.InvalidValueException;

import java.util.Comparator;
import java.util.Objects;

/*
DiscountName value object
 */
public final class DiscountName implements Comparable<DiscountName> {

    private final String value;

    private DiscountName(String value) {

        if (value == null) {
            throw new InvalidValueException("Discount name value can not be null.");
        }

        if ("".equals(value)) {
            throw new InvalidValueException("Discount name value can not be blank.");
        }

        this.value = value;
    }

    public static DiscountName valueOf(String value) {
        return new DiscountName(value);
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DiscountName discountName = (DiscountName) o;
        return value.equals(discountName.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public int compareTo(DiscountName o) {
        return Comparator.comparing(DiscountName::getValue).compare(this, o);
    }
}
