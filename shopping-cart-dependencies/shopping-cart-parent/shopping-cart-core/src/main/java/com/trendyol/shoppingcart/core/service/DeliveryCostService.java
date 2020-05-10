package com.trendyol.shoppingcart.core.service;

import com.trendyol.shoppingcart.core.domain.ShoppingCart;
import com.trendyol.shoppingcart.core.domain.value.Amount;

public interface DeliveryCostService {

    Amount calculateFor(ShoppingCart cart);
}
