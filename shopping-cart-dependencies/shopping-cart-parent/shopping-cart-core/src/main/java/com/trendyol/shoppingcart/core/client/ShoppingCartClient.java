package com.trendyol.shoppingcart.core.client;

import com.trendyol.shoppingcart.core.discountprovider.DiscountProvider;
import com.trendyol.shoppingcart.core.domain.ShoppingCart;
import com.trendyol.shoppingcart.core.exception.InvalidValueException;
import com.trendyol.shoppingcart.core.service.DeliveryCostService;
import com.trendyol.shoppingcart.core.service.ShoppingCartService;

public class ShoppingCartClient {

    protected final DeliveryCostService deliveryCostService;
    protected final ShoppingCartService shoppingCartService;

    public ShoppingCartClient(ShoppingCartService shoppingCartService) {
        this(null, shoppingCartService);
    }

    public ShoppingCartClient(DeliveryCostService deliveryCostService, ShoppingCartService shoppingCartService) {
        if (shoppingCartService == null) {
            throw new InvalidValueException("Shopping cart service can not be null!");
        }
        this.deliveryCostService = deliveryCostService;
        this.shoppingCartService = shoppingCartService;
    }

    public void provideDiscounts(ShoppingCart shoppingCart, DiscountProvider discountProvider) {
        if (discountProvider != null) {
            discountProvider.registerDiscountProviderEventListener(shoppingCart);
            discountProvider.provideDiscount();
        }
    }

    public void submitCart(ShoppingCart shoppingCart, DiscountProvider discountProvider) {
        if (shoppingCart == null) {
            throw new InvalidValueException("Shopping cart can not be null!");
        }
        provideDiscounts(shoppingCart, discountProvider);
        shoppingCart.applyDiscounts();
        if (deliveryCostService != null) {
            shoppingCart.setDeliveryCost(deliveryCostService.calculateFor(shoppingCart));
        }
        ShoppingCart savedShoppingCart = shoppingCartService.save(shoppingCart);
        savedShoppingCart.print();
    }

    public DeliveryCostService getDeliveryCostService() {
        return deliveryCostService;
    }

    public ShoppingCartService getShoppingCartService() {
        return shoppingCartService;
    }
}
