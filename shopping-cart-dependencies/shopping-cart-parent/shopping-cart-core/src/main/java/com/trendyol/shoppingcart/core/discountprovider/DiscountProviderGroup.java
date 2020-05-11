package com.trendyol.shoppingcart.core.discountprovider;


import com.trendyol.shoppingcart.core.discountprovider.listener.DiscountProvidedEvent;
import com.trendyol.shoppingcart.core.discountprovider.listener.DiscountProviderEventListener;

import java.util.LinkedHashSet;
import java.util.Set;

public class DiscountProviderGroup implements DiscountProvider {

    private final Set<DiscountProvider> childDiscountProviders = new LinkedHashSet<>();

    @Override
    public void provideDiscount() {
        childDiscountProviders.forEach(DiscountProvider::provideDiscount);
    }

    @Override
    public void registerDiscountProviderEventListener(DiscountProviderEventListener listener) {
        childDiscountProviders.forEach(discountProcessor -> discountProcessor.registerDiscountProviderEventListener(listener));
    }

    @Override
    public void unregisterDiscountProviderEventListener(DiscountProviderEventListener listener) {
        childDiscountProviders.forEach(discountProcessor -> discountProcessor.unregisterDiscountProviderEventListener(listener));
    }

    @Override
    public void notifyDiscountProviderEventListeners(DiscountProvidedEvent discountProvidedEvent) {
        childDiscountProviders.forEach(discountProcessor -> discountProcessor.notifyDiscountProviderEventListeners(discountProvidedEvent));
    }

    public void addDiscountProvider(DiscountProvider discountProvider) {
        this.childDiscountProviders.add(discountProvider);
    }

    public void removeDiscountProvider(DiscountProvider discountProvider) {
        this.childDiscountProviders.remove(discountProvider);
    }

    public Set<DiscountProvider> getChildDiscountProviders() {
        return childDiscountProviders;
    }
}
