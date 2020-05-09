package com.trendyol.shoppingcart.core.discountprovider;


import com.trendyol.shoppingcart.core.discountprovider.listener.DiscountProvidedEvent;
import com.trendyol.shoppingcart.core.discountprovider.listener.DiscountProviderEventListener;

import java.util.LinkedHashSet;
import java.util.Set;

public class DiscountProviderGroup implements DiscountProvider {

    private final Set<DiscountProvider> childDiscountProviders;

    public DiscountProviderGroup() {
        this.childDiscountProviders = new LinkedHashSet<>();
    }

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

    public void addDiscountProvider(DiscountProvider discountProcessor) {
        this.childDiscountProviders.add(discountProcessor);
    }

    public void removeDiscountProvider(DiscountProvider discountProcessor) {
        this.childDiscountProviders.remove(discountProcessor);
    }

    public Set<DiscountProvider> getChildDiscountProviders() {
        return childDiscountProviders;
    }
}
