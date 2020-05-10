package com.trendyol.shoppingcart.core.discountprovider;

import com.trendyol.shoppingcart.core.domain.discount.Discount;

import java.time.Instant;

public class MockDiscountProvider extends DiscountProviderItem {

    private final Instant expireTime;

    public MockDiscountProvider(Discount discount) {
        this(discount, null);
    }

    public MockDiscountProvider(Discount discount, Instant expireTime) {
        super(discount);
        this.expireTime = expireTime;
    }

    @Override
    public boolean isValid() {
        if (expireTime == null) {
            return true;
        }
        return expireTime.isAfter(Instant.now());
    }

    public Instant getExpireTime() {
        return expireTime;
    }
}
