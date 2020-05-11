package com.trendyol.shoppingcart.client.trendyol.discountprovider.coupon;

import com.trendyol.shoppingcart.client.trendyol.domain.discount.MinimumCartAmountBasedAmountDiscount;
import com.trendyol.shoppingcart.client.trendyol.domain.discount.MinimumCartAmountBasedRateDiscount;
import com.trendyol.shoppingcart.client.trendyol.domain.value.CouponCode;
import com.trendyol.shoppingcart.client.trendyol.domain.value.Rate;
import com.trendyol.shoppingcart.core.domain.value.Amount;

import java.time.Instant;

public class MinimumCartAmountBasedDiscountCoupon extends Coupon {

    public MinimumCartAmountBasedDiscountCoupon(CouponCode couponCode, Amount minimumCartAmount, Rate discountRate) {
        this(couponCode, minimumCartAmount, discountRate, null);
    }

    public MinimumCartAmountBasedDiscountCoupon(CouponCode couponCode, Amount minimumCartAmount, Amount discountAmount) {
        this(couponCode, minimumCartAmount, discountAmount, null);
    }

    public MinimumCartAmountBasedDiscountCoupon(CouponCode couponCode, Amount minimumCartAmount, Rate discountRate, Instant expireTime) {
        super(new MinimumCartAmountBasedRateDiscount(minimumCartAmount, discountRate), couponCode, expireTime);
    }

    public MinimumCartAmountBasedDiscountCoupon(CouponCode couponCode, Amount minimumCartAmount, Amount discountAmount, Instant expireTime) {
        super(new MinimumCartAmountBasedAmountDiscount(minimumCartAmount, discountAmount), couponCode, expireTime);
    }
}
