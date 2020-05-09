package com.trendyol.shoppingcart.core.discountprovider.listener;

import com.trendyol.shoppingcart.core.domain.discount.Discount;

public class DiscountProvidedEvent {

    protected final Discount discount;

    public DiscountProvidedEvent(Discount discount) {
        this.discount = discount;
    }

    public Discount getDiscount() {
        return discount;
    }
}
