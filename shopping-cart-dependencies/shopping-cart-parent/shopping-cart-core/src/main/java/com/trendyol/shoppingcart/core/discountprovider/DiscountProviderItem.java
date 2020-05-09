package com.trendyol.shoppingcart.core.discountprovider;

import com.trendyol.shoppingcart.core.discountprovider.listener.DiscountProvidedEvent;
import com.trendyol.shoppingcart.core.discountprovider.listener.DiscountProviderEventListener;
import com.trendyol.shoppingcart.core.domain.discount.Discount;
import com.trendyol.shoppingcart.core.exception.InvalidValueException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class DiscountProviderItem implements DiscountProvider {

    private final static Logger logger = LoggerFactory.getLogger(DiscountProviderItem.class);

    protected final List<DiscountProviderEventListener> discountProviderEventListenerList;

    protected final Discount discount;

    public DiscountProviderItem(Discount discount) {
        this.discountProviderEventListenerList = new ArrayList<>();

        if (discount == null) {
            throw new InvalidValueException("Discount can not be null!");
        }

        this.discount = discount;
    }

    public abstract Boolean isValid();

    @Override
    public void provideDiscount() {
        if (isValid()) {
            DiscountProvidedEvent discountProvidedEvent = new DiscountProvidedEvent(discount);
            notifyDiscountProviderEventListeners(discountProvidedEvent);
        } else {
            logger.info("Discount can not be processed. Discount processor did not pass validation. Discount processor: {}", this);
        }
    }

    @Override
    public void registerDiscountProviderEventListener(DiscountProviderEventListener listener) {
        if (!discountProviderEventListenerList.contains(listener)) {
            discountProviderEventListenerList.add(listener);
        }
    }

    @Override
    public void unregisterDiscountProviderEventListener(DiscountProviderEventListener listener) {
        discountProviderEventListenerList.remove(listener);
    }

    @Override
    public void notifyDiscountProviderEventListeners(DiscountProvidedEvent discountProvidedEvent) {
        discountProviderEventListenerList.forEach(listener -> listener.discountProvided(discountProvidedEvent));
    }

    public Discount getDiscount() {
        return discount;
    }

    public List<DiscountProviderEventListener> getDiscountProviderEventListenerList() {
        return discountProviderEventListenerList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DiscountProviderItem that = (DiscountProviderItem) o;
        return discount.equals(that.discount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(discount);
    }

    @Override
    public String toString() {
        return "DiscountProcessor: " +
                "Discount=" + discount;
    }
}
