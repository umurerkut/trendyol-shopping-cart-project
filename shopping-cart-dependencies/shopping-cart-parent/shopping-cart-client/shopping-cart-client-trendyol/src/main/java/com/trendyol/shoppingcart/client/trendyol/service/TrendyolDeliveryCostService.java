package com.trendyol.shoppingcart.client.trendyol.service;

import com.trendyol.shoppingcart.client.trendyol.config.TrendyolShoppingCartClientProperties;
import com.trendyol.shoppingcart.core.domain.ShoppingCart;
import com.trendyol.shoppingcart.core.domain.value.Amount;
import com.trendyol.shoppingcart.core.exception.InvalidValueException;
import com.trendyol.shoppingcart.core.service.DeliveryCostService;
import org.springframework.stereotype.Service;

@Service
public final class TrendyolDeliveryCostService implements DeliveryCostService {

    private static final Amount DEFAULT_FIXED_COST = Amount.valueOf(2.99D);
    private final TrendyolShoppingCartClientProperties trendyolShoppingCartProperties;

    public TrendyolDeliveryCostService() {
        this(null);
    }

    public TrendyolDeliveryCostService(TrendyolShoppingCartClientProperties trendyolShoppingCartProperties) {
        this.trendyolShoppingCartProperties = trendyolShoppingCartProperties;
    }

    @Override
    public Amount calculateFor(ShoppingCart cart) {

        if (cart == null) {
            throw new InvalidValueException("Shopping cart can not be null!");
        }

        Amount costPerDelivery = Amount.ofZero();
        Amount costPerProduct = Amount.ofZero();
        Amount fixedCost = DEFAULT_FIXED_COST;

        if (trendyolShoppingCartProperties != null) {
            costPerDelivery = Amount.valueOf(trendyolShoppingCartProperties.getDeliverCost().getCostPerDelivery());
            costPerProduct = Amount.valueOf(trendyolShoppingCartProperties.getDeliverCost().getCostPerProduct());
            fixedCost = Amount.valueOf(trendyolShoppingCartProperties.getDeliverCost().getFixedCost());

            if (costPerDelivery.isNegative()) {
                costPerDelivery = Amount.ofZero();
            }

            if (costPerProduct.isNegative()) {
                costPerProduct = Amount.ofZero();
            }

            if (fixedCost.isNegative() || fixedCost.isZero()) {
                fixedCost = DEFAULT_FIXED_COST;
            }
        }

        return costPerDelivery.multiply(cart.getNumberOfDistinctCategoryInCart().doubleValue())
                .add(costPerProduct.multiply(cart.getNumberOfDistinctProductInCart().doubleValue()))
                .add(fixedCost);
    }

    public TrendyolShoppingCartClientProperties getTrendyolShoppingCartProperties() {
        return trendyolShoppingCartProperties;
    }
}
