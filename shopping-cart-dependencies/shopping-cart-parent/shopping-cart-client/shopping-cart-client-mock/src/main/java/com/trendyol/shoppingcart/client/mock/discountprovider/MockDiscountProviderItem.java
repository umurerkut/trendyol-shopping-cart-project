package com.trendyol.shoppingcart.client.mock.discountprovider;

import com.trendyol.shoppingcart.core.discountprovider.DiscountProviderItem;
import com.trendyol.shoppingcart.core.domain.discount.Discount;

import java.time.Instant;
import java.util.Objects;

public class MockDiscountProviderItem extends DiscountProviderItem {

    private final Instant expireTime;

    public MockDiscountProviderItem(Discount discount) {
        this(discount, null);
    }

    public MockDiscountProviderItem(Discount discount, Instant expireTime) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        MockDiscountProviderItem that = (MockDiscountProviderItem) o;

        return Objects.equals(expireTime, that.expireTime);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (expireTime != null ? expireTime.hashCode() : 0);
        return result;
    }
}
