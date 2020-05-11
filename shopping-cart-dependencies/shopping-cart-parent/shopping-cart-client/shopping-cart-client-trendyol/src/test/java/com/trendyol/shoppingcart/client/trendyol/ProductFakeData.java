package com.trendyol.shoppingcart.client.trendyol;

import com.trendyol.shoppingcart.core.domain.Product;
import com.trendyol.shoppingcart.core.domain.value.Amount;
import com.trendyol.shoppingcart.core.domain.value.Title;

import static com.trendyol.shoppingcart.client.trendyol.CategoryFakeData.*;

public final class ProductFakeData {

    public static final Product apple = apple();
    public static final Product pear = pear();
    public static final Product banana = banana();
    public static final Product water = water();
    public static final Product beer = beer();

    private static Product apple() {
        return new Product(Title.valueOf("APPLE"), Amount.valueOf(10D), food);
    }

    private static Product pear() {
        return new Product(Title.valueOf("PEAR"), Amount.valueOf(15D), food);
    }

    private static Product banana() {
        return new Product(Title.valueOf("BANANA"), Amount.valueOf(20D), food);
    }

    private static Product water() {
        return new Product(Title.valueOf("WATER"), Amount.valueOf(2D), nonalcoholicBeverage);
    }

    private static Product beer() {
        return new Product(Title.valueOf("BEER"), Amount.valueOf(20D), alcoholicBeverage);
    }
}
