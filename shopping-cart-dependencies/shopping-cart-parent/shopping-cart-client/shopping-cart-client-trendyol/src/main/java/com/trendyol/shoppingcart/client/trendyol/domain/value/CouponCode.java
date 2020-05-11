package com.trendyol.shoppingcart.client.trendyol.domain.value;

import com.trendyol.shoppingcart.core.exception.InvalidValueException;

import java.util.Comparator;
import java.util.Objects;

/*
CouponCode value object
 */
public final class CouponCode implements Comparable<CouponCode> {

    private final String value;

    private CouponCode(String value) {

        if (value == null) {
            throw new InvalidValueException("Coupon code value can not be null!");
        }

        if ("".equals(value)) {
            throw new InvalidValueException("Coupon code value can not be blank!");
        }

        this.value = value;
    }

    public static CouponCode valueOf(String value) {
        return new CouponCode(value);
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CouponCode couponCode = (CouponCode) o;
        return value.equals(couponCode.value);
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
    public int compareTo(CouponCode o) {
        return Comparator.comparing(CouponCode::getValue).compare(this, o);
    }
}
