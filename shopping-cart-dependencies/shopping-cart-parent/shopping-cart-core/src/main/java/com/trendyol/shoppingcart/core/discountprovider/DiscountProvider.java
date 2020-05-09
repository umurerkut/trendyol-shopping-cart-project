package com.trendyol.shoppingcart.core.discountprovider;


import com.trendyol.shoppingcart.core.discountprovider.listener.DiscountProvidedEvent;
import com.trendyol.shoppingcart.core.discountprovider.listener.DiscountProviderEventListener;

public interface DiscountProvider {

    void provideDiscount();

    void registerDiscountProviderEventListener(DiscountProviderEventListener listener);

    void unregisterDiscountProviderEventListener(DiscountProviderEventListener listener);

    void notifyDiscountProviderEventListeners(DiscountProvidedEvent discountProvidedEvent);
}
