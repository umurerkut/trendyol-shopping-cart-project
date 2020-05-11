package com.trendyol.shoppingcart.core.client;

import com.trendyol.shoppingcart.core.discountprovider.DiscountProviderGroup;
import com.trendyol.shoppingcart.core.domain.ShoppingCart;
import com.trendyol.shoppingcart.core.exception.InvalidValueException;
import com.trendyol.shoppingcart.core.service.DeliveryCostService;
import com.trendyol.shoppingcart.core.service.ShoppingCartService;

public abstract class ShoppingCartClient {

    protected final DeliveryCostService deliveryCostService;
    protected final ShoppingCartService shoppingCartService;

    protected final DiscountProviderGroup ruleDiscountProvider = new DiscountProviderGroup();

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

    private void provideDiscounts(ShoppingCart shoppingCart) {
        ruleDiscountProvider.registerDiscountProviderEventListener(shoppingCart);
        ruleDiscountProvider.provideDiscount();
    }

    public void submitCart(ShoppingCart shoppingCart) {
        if (shoppingCart == null) {
            throw new InvalidValueException("Shopping cart can not be null!");
        }
        provideDiscounts(shoppingCart);
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

    public DiscountProviderGroup getRuleDiscountProvider() {
        return ruleDiscountProvider;
    }
}
