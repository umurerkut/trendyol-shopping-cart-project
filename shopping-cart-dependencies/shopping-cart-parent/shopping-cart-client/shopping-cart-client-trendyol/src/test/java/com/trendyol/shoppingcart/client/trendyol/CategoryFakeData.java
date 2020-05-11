package com.trendyol.shoppingcart.client.trendyol;

import com.trendyol.shoppingcart.core.domain.Category;
import com.trendyol.shoppingcart.core.domain.value.Title;

public final class CategoryFakeData {

    public static final Category food = food();
    public static final Category beverage = beverage();
    public static final Category alcoholicBeverage = alcoholicBeverage();
    public static final Category nonalcoholicBeverage = nonalcoholicBeverage();

    private static Category food() {
        return new Category(Title.valueOf("FOOD"));
    }

    private static Category beverage() {
        return new Category(Title.valueOf("BEVERAGE"));
    }

    private static Category alcoholicBeverage() {
        return new Category(beverage, Title.valueOf("ALCOHOLIC_BEVERAGE"));
    }

    private static Category nonalcoholicBeverage() {
        return new Category(beverage, Title.valueOf("NONALCOHOLIC_BEVERAGE"));
    }
}
