package com.trendyol.shoppingcart.client.trendyol.discountprovider.coupon;

import com.trendyol.shoppingcart.client.trendyol.domain.value.CouponCode;
import com.trendyol.shoppingcart.core.discountprovider.DiscountProviderItem;
import com.trendyol.shoppingcart.core.domain.discount.Discount;
import com.trendyol.shoppingcart.core.exception.InvalidValueException;

import java.time.Instant;
import java.util.Objects;

public abstract class Coupon extends DiscountProviderItem {

    protected final CouponCode couponCode;
    protected final Instant expireTime;

    public Coupon(Discount discount, CouponCode couponCode) {
        this(discount, couponCode, null);
    }

    public Coupon(Discount discount, CouponCode couponCode, Instant expireTime) {
        super(discount);

        if (couponCode == null) {
            throw new InvalidValueException("Coupon code can not be null!");
        }

        this.expireTime = expireTime;
        this.couponCode = couponCode;
    }

    @Override
    public boolean isValid() {
        if (expireTime == null) {
            //if the expire time is null, the coupon is valid forever
            return true;
        }
        return Instant.now().isBefore(expireTime);
    }

    public Instant getExpireTime() {
        return expireTime;
    }

    public CouponCode getCouponCode() {
        return couponCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Coupon coupon = (Coupon) o;

        if (!couponCode.equals(coupon.couponCode)) return false;
        return Objects.equals(expireTime, coupon.expireTime);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + couponCode.hashCode();
        result = 31 * result + (expireTime != null ? expireTime.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Coupon: " +
                "CouponCode=" + couponCode +
                ", ExpireTime=" + expireTime;
    }
}
