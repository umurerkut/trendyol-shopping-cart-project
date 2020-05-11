package com.trendyol.shoppingcart.client.mock;

import com.trendyol.shoppingcart.client.mock.discount.ProductQuantityBasedMockDiscount;
import com.trendyol.shoppingcart.client.mock.discount.ProductTypeBasedMockDiscount;
import com.trendyol.shoppingcart.client.mock.discountprovider.MockDiscountProviderItem;
import com.trendyol.shoppingcart.core.client.ShoppingCartClient;
import com.trendyol.shoppingcart.core.discountprovider.DiscountProvider;
import com.trendyol.shoppingcart.core.discountprovider.DiscountProviderGroup;
import com.trendyol.shoppingcart.core.discountprovider.DiscountProviderItem;
import com.trendyol.shoppingcart.core.domain.Product;
import com.trendyol.shoppingcart.core.domain.discount.Discount;
import com.trendyol.shoppingcart.core.domain.value.Amount;
import com.trendyol.shoppingcart.core.domain.value.Quantity;
import com.trendyol.shoppingcart.core.domain.value.Title;
import com.trendyol.shoppingcart.core.service.ProductService;
import com.trendyol.shoppingcart.core.service.ShoppingCartService;

import java.util.NoSuchElementException;

public class MockShoppingCartClient extends ShoppingCartClient {

    private final ProductService productService;

    public MockShoppingCartClient(ShoppingCartService shoppingCartService, ProductService productService) {
        super(shoppingCartService);
        this.productService = productService;
        createClientRuleDiscountProviderGroup();
    }

    //always provide product type based discounts first then provide product quantity based discounts
    private void createClientRuleDiscountProviderGroup() {
        ruleDiscountProvider.addDiscountProvider(createProductTypeBasedDiscountProviderGroup());
        ruleDiscountProvider.addDiscountProvider(createProductQuantityBasedDiscountProviderGroup());
    }

    private DiscountProvider createProductQuantityBasedDiscountProviderGroup() {
        DiscountProviderGroup discountProviderGroup = new DiscountProviderGroup();
        discountProviderGroup.addDiscountProvider(createDiscountProviderItem(createDiscountIfProductQuantityGreaterEquals5ThenApplyDiscountAmountOf10()));
        discountProviderGroup.addDiscountProvider(createDiscountProviderItem(createDiscountIfProductQuantityGreaterEquals10ThenApplyDiscountAmountOf15()));
        return discountProviderGroup;
    }

    private DiscountProvider createProductTypeBasedDiscountProviderGroup() {
        DiscountProviderGroup discountProviderGroup = new DiscountProviderGroup();
        discountProviderGroup.addDiscountProvider(createDiscountProviderItem(createDiscountIfAnyAppleExistsThenApplyAmountOf2()));
        discountProviderGroup.addDiscountProvider(createDiscountProviderItem(createDiscountIfAnyBananaExistsThenApplyAmountOf5()));
        return discountProviderGroup;
    }

    private DiscountProviderItem createDiscountProviderItem(Discount discount) {
        return new MockDiscountProviderItem(discount);
    }

    private Discount createProductQuantityBasedDiscount(Quantity minimumQuantityOfProducts, Amount discountAmount) {
        return new ProductQuantityBasedMockDiscount(minimumQuantityOfProducts, discountAmount);
    }

    private Discount createProductTypeBasedDiscount(Product product, Amount discountAmount) {
        return new ProductTypeBasedMockDiscount(product, discountAmount);
    }

    private Discount createDiscountIfProductQuantityGreaterEquals5ThenApplyDiscountAmountOf10() {
        return createProductQuantityBasedDiscount(Quantity.valueOf(5), Amount.valueOf(10D));
    }

    private Discount createDiscountIfProductQuantityGreaterEquals10ThenApplyDiscountAmountOf15() {
        return createProductQuantityBasedDiscount(Quantity.valueOf(10), Amount.valueOf(15D));
    }

    private Discount createDiscountIfAnyAppleExistsThenApplyAmountOf2() {
        Product apple = productService.get(Title.valueOf("APPLE")).orElseThrow(NoSuchElementException::new);
        return createProductTypeBasedDiscount(apple, Amount.valueOf(2D));
    }

    private Discount createDiscountIfAnyBananaExistsThenApplyAmountOf5() {
        Product apple = productService.get(Title.valueOf("BANANA")).orElseThrow(NoSuchElementException::new);
        return createProductTypeBasedDiscount(apple, Amount.valueOf(5D));
    }
}
