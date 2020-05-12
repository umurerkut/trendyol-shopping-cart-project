package com.trendyol.shoppingcart.client.trendyol;

import com.trendyol.shoppingcart.core.domain.Category;
import com.trendyol.shoppingcart.core.domain.value.Title;

public final class CategoryFakeData {

    public static final Category root = root();
    public static final Category food = food();
    public static final Category beverage = beverage();
    public static final Category alcoholicBeverage = alcoholicBeverage();
    public static final Category nonalcoholicBeverage = nonalcoholicBeverage();

    private static Category root() {
        return new Category(Title.valueOf("ROOT"));
    }

    private static Category food() {
        return root.addChild(Title.valueOf("FOOD"));
    }

    private static Category beverage() {
        return root.addChild((Title.valueOf("BEVERAGE")));
    }

    private static Category alcoholicBeverage() {
        return beverage.addChild(Title.valueOf("ALCOHOLIC_BEVERAGE"));

    }

    private static Category nonalcoholicBeverage() {
        return beverage.addChild(Title.valueOf("NONALCOHOLIC_BEVERAGE"));
    }
}
