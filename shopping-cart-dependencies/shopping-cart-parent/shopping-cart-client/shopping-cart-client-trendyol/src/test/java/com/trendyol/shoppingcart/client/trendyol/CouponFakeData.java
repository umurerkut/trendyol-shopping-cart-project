package com.trendyol.shoppingcart.client.trendyol;

import com.trendyol.shoppingcart.client.trendyol.discountprovider.coupon.Coupon;
import com.trendyol.shoppingcart.client.trendyol.discountprovider.coupon.MinimumCartAmountBasedDiscountCoupon;
import com.trendyol.shoppingcart.client.trendyol.domain.value.CouponCode;
import com.trendyol.shoppingcart.client.trendyol.domain.value.Rate;
import com.trendyol.shoppingcart.core.domain.value.Amount;

public final class CouponFakeData {

    public static Coupon createCouponIfCartAmountGreaterEquals50ThenApplyDiscountAmountOf5 = createCouponIfCartAmountGreaterEquals50ThenApplyDiscountAmountOf5();
    public static Coupon createCouponIfCartAmountGreaterEquals100ThenApplyDiscountRateOf10 = createCouponIfCartAmountGreaterEquals100ThenApplyDiscountRateOf10();

    private static Coupon createCouponIfCartAmountGreaterEquals50ThenApplyDiscountAmountOf5() {
        String couponCodeText = "COUPON-1";
        return new MinimumCartAmountBasedDiscountCoupon(CouponCode.valueOf(couponCodeText), Amount.valueOf(50D), Amount.valueOf(5D));
    }

    private static Coupon createCouponIfCartAmountGreaterEquals100ThenApplyDiscountRateOf10() {
        String couponCodeText = "COUPON-2";
        return new MinimumCartAmountBasedDiscountCoupon(CouponCode.valueOf(couponCodeText), Amount.valueOf(100D), Rate.valueOf(10D));
    }
}
