package com.trendyol.shoppingcart.core.domain;

import com.trendyol.shoppingcart.core.discountprovider.listener.DiscountProvidedEvent;
import com.trendyol.shoppingcart.core.domain.discount.ProductQuantityBasedMockDiscount;
import com.trendyol.shoppingcart.core.domain.discount.ProductTypeBasedMockDiscount;
import com.trendyol.shoppingcart.core.domain.value.Amount;
import com.trendyol.shoppingcart.core.domain.value.Quantity;
import com.trendyol.shoppingcart.core.domain.value.Title;
import com.trendyol.shoppingcart.core.exception.InvalidValueException;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class ShoppingCartTest {

    @Test
    public void givenShoppingCart_whenAddSingleProduct_thenAddProductToCartAndCalculateCartAmount() {
        //given
        ShoppingCart shoppingCart = new ShoppingCart();
        Category category = new Category(Title.valueOf("Category"));
        Product product = new Product(Title.valueOf("Product"), Amount.valueOf(10D), category);
        Quantity quantity = Quantity.valueOf(1);

        //when
        assertThat(product).isNotNull();
        assertThat(quantity).isNotNull();
        assertThat(quantity.doubleValue()).isPositive();
        shoppingCart.addProduct(product, quantity);

        //then
        assertThat(shoppingCart.containsProduct(product)).isTrue();
        assertThat(shoppingCart.getQuantityOfProductInCart(product)).isEqualTo(Quantity.valueOf(1));
        assertThat(shoppingCart.getTotalPriceOfProductInCart(product)).isEqualTo(Amount.valueOf(10D));
        assertThat(shoppingCart.getTotalQuantityOfProductsInCart()).isEqualTo(Quantity.valueOf(1));
        assertThat(shoppingCart.getNumberOfDistinctProductInCart()).isEqualTo(Quantity.valueOf(1));
        assertThat(shoppingCart.getNumberOfDistinctCategoryInCart()).isEqualTo(Quantity.valueOf(1));
        assertThat(shoppingCart.getCartAmount()).isEqualTo(Amount.valueOf(10D));
        assertThat(shoppingCart.getCartAmountWithoutDiscount()).isEqualTo(shoppingCart.getCartAmount()).isEqualTo(Amount.valueOf(10D));
        assertThat(shoppingCart.getTotalDiscount()).isEqualTo(Amount.ofZero());
        assertThat(shoppingCart.getDeliveryCost()).isEqualTo(Amount.ofZero());
    }

    @Test
    public void givenShoppingCart_whenAddSingleProductAndAddItAgainWithDifferentQuantity_thenAddProductToCartAndIncreaseProductQuantityAndCalculateCartAmount() {
        //given
        ShoppingCart shoppingCart = new ShoppingCart();
        Category category = new Category(Title.valueOf("Category"));
        Product product = new Product(Title.valueOf("Product"), Amount.valueOf(10D), category);

        //when
        shoppingCart.addProduct(product, Quantity.valueOf(1));
        shoppingCart.addProduct(product, Quantity.valueOf(2));

        //then
        assertThat(shoppingCart.containsProduct(product)).isTrue();
        assertThat(shoppingCart.getQuantityOfProductInCart(product)).isEqualTo(Quantity.valueOf(3));
        assertThat(shoppingCart.getTotalPriceOfProductInCart(product)).isEqualTo(Amount.valueOf(30D));
        assertThat(shoppingCart.getTotalQuantityOfProductsInCart()).isEqualTo(Quantity.valueOf(3));
        assertThat(shoppingCart.getNumberOfDistinctProductInCart()).isEqualTo(Quantity.valueOf(1));
        assertThat(shoppingCart.getNumberOfDistinctCategoryInCart()).isEqualTo(Quantity.valueOf(1));
        assertThat(shoppingCart.getCartAmount()).isEqualTo(Amount.valueOf(30D));
        assertThat(shoppingCart.getCartAmountWithoutDiscount()).isEqualTo(shoppingCart.getCartAmount()).isEqualTo(Amount.valueOf(30D));
        assertThat(shoppingCart.getTotalDiscount()).isEqualTo(Amount.ofZero());
        assertThat(shoppingCart.getDeliveryCost()).isEqualTo(Amount.ofZero());
    }

    @Test
    public void givenShoppingCart_whenAddMultipleProductsInSameCategory_thenAddProductsToCartAndCalculateCartAmount() {
        //given
        ShoppingCart shoppingCart = new ShoppingCart();
        Category category = new Category(Title.valueOf("Category"));
        Product product1 = new Product(Title.valueOf("Product1"), Amount.valueOf(10D), category);
        Product product2 = new Product(Title.valueOf("Product2"), Amount.valueOf(15D), category);

        //when
        shoppingCart.addProduct(product1, Quantity.valueOf(1));
        shoppingCart.addProduct(product2, Quantity.valueOf(2));

        //then
        assertThat(shoppingCart.containsProduct(product1)).isTrue();
        assertThat(shoppingCart.containsProduct(product2)).isTrue();
        assertThat(shoppingCart.getQuantityOfProductInCart(product1)).isEqualTo(Quantity.valueOf(1));
        assertThat(shoppingCart.getQuantityOfProductInCart(product2)).isEqualTo(Quantity.valueOf(2));
        assertThat(shoppingCart.getTotalPriceOfProductInCart(product1)).isEqualTo(Amount.valueOf(10D));
        assertThat(shoppingCart.getTotalPriceOfProductInCart(product2)).isEqualTo(Amount.valueOf(30D));
        assertThat(shoppingCart.getTotalQuantityOfProductsInCart()).isEqualTo(Quantity.valueOf(3));
        assertThat(shoppingCart.getNumberOfDistinctProductInCart()).isEqualTo(Quantity.valueOf(2));
        assertThat(shoppingCart.getNumberOfDistinctCategoryInCart()).isEqualTo(Quantity.valueOf(1));
        assertThat(shoppingCart.getCartAmount()).isEqualTo(Amount.valueOf(40D));
        assertThat(shoppingCart.getCartAmountWithoutDiscount()).isEqualTo(shoppingCart.getCartAmount()).isEqualTo(Amount.valueOf(40D));
        assertThat(shoppingCart.getTotalDiscount()).isEqualTo(Amount.ofZero());
        assertThat(shoppingCart.getDeliveryCost()).isEqualTo(Amount.ofZero());
    }

    @Test
    public void givenShoppingCart_whenAddMultipleProductsInDifferentCategories_thenAddProductsToCartAndCalculateCartAmount() {
        //given
        ShoppingCart shoppingCart = new ShoppingCart();
        Category category1 = new Category(Title.valueOf("Category1"));
        Category category2 = new Category(Title.valueOf("Category2"));
        Product product1 = new Product(Title.valueOf("Product1"), Amount.valueOf(10D), category1);
        Product product2 = new Product(Title.valueOf("Product2"), Amount.valueOf(15D), category1);
        Product product3 = new Product(Title.valueOf("Product2"), Amount.valueOf(20D), category2);

        //when
        shoppingCart.addProduct(product1, Quantity.valueOf(1));
        shoppingCart.addProduct(product2, Quantity.valueOf(2));
        shoppingCart.addProduct(product3, Quantity.valueOf(3));

        //then
        assertThat(shoppingCart.containsProduct(product1)).isTrue();
        assertThat(shoppingCart.containsProduct(product2)).isTrue();
        assertThat(shoppingCart.containsProduct(product3)).isTrue();
        assertThat(shoppingCart.getQuantityOfProductInCart(product1)).isEqualTo(Quantity.valueOf(1));
        assertThat(shoppingCart.getQuantityOfProductInCart(product2)).isEqualTo(Quantity.valueOf(2));
        assertThat(shoppingCart.getQuantityOfProductInCart(product3)).isEqualTo(Quantity.valueOf(3));
        assertThat(shoppingCart.getTotalPriceOfProductInCart(product1)).isEqualTo(Amount.valueOf(10D));
        assertThat(shoppingCart.getTotalPriceOfProductInCart(product2)).isEqualTo(Amount.valueOf(30D));
        assertThat(shoppingCart.getTotalPriceOfProductInCart(product3)).isEqualTo(Amount.valueOf(60D));
        assertThat(shoppingCart.getTotalQuantityOfProductsInCart()).isEqualTo(Quantity.valueOf(6));
        assertThat(shoppingCart.getNumberOfDistinctProductInCart()).isEqualTo(Quantity.valueOf(3));
        assertThat(shoppingCart.getNumberOfDistinctCategoryInCart()).isEqualTo(Quantity.valueOf(2));
        assertThat(shoppingCart.getCartAmount()).isEqualTo(Amount.valueOf(100D));
        assertThat(shoppingCart.getCartAmountWithoutDiscount()).isEqualTo(shoppingCart.getCartAmount()).isEqualTo(Amount.valueOf(100D));
        assertThat(shoppingCart.getTotalDiscount()).isEqualTo(Amount.ofZero());
        assertThat(shoppingCart.getDeliveryCost()).isEqualTo(Amount.ofZero());
    }

    @Test
    public void givenShoppingCart_whenAddNullProduct_thenThrowInvalidValueException() {
        //given
        ShoppingCart shoppingCart = new ShoppingCart();
        Product product = null;
        Quantity quantity = Quantity.valueOf(1);

        //when
        assertThat(product).isNull();
        Throwable throwable = catchThrowable(() -> shoppingCart.addProduct(product, quantity));

        //then
        assertThat(throwable)
                .isNotNull()
                .isInstanceOf(InvalidValueException.class)
                .hasMessage("Product can not be null!");
    }

    @Test
    public void givenShoppingCart_whenAddNullQuantity_thenThrowInvalidValueException() {
        //given
        ShoppingCart shoppingCart = new ShoppingCart();
        Category category = new Category(Title.valueOf("Category"));
        Product product = new Product(Title.valueOf("Product"), Amount.valueOf(10D), category);
        Quantity quantity = null;

        //when
        assertThat(quantity).isNull();
        Throwable throwable = catchThrowable(() -> shoppingCart.addProduct(product, quantity));

        //then
        assertThat(throwable)
                .isNotNull()
                .isInstanceOf(InvalidValueException.class)
                .hasMessage("Quantity can not be null!");
    }

    @Test
    public void givenShoppingCart_whenAddZeroQuantity_thenThrowInvalidValueException() {
        //given
        ShoppingCart shoppingCart = new ShoppingCart();
        Category category = new Category(Title.valueOf("Category"));
        Product product = new Product(Title.valueOf("Product"), Amount.valueOf(10D), category);
        Quantity quantity = Quantity.ofZero();

        //when
        assertThat(quantity).isNotNull();
        Throwable throwable = catchThrowable(() -> shoppingCart.addProduct(product, quantity));

        //then
        assertThat(throwable)
                .isNotNull()
                .isInstanceOf(InvalidValueException.class)
                .hasMessage("Quantity can not be zero!");
    }

    @Test
    public void givenShoppingCart_whenDiscountsAreNotProvided_thenNoDiscountsApplied() {
        //given
        ShoppingCart shoppingCart = new ShoppingCart();
        Category category = new Category(Title.valueOf("Category"));
        Product product = new Product(Title.valueOf("Product"), Amount.valueOf(10D), category);
        Quantity quantity = Quantity.valueOf(10);

        assertThat(product).isNotNull();
        assertThat(quantity).isNotNull();
        assertThat(quantity.doubleValue()).isPositive();

        shoppingCart.addProduct(product, quantity);

        DiscountProvidedEvent event = null;

        //when
        assertThat(event).isNull();
        shoppingCart.discountProvided(event);
        shoppingCart.applyDiscounts();

        //then
        assertThat(shoppingCart.getDiscountMap()).hasSize(0);
        assertThat(shoppingCart.getCartAmount()).isEqualTo(Amount.valueOf(100D));
        assertThat(shoppingCart.getCartAmountWithoutDiscount()).isEqualTo(shoppingCart.getCartAmount()).isEqualTo(Amount.valueOf(100D));
        assertThat(shoppingCart.getTotalDiscount()).isEqualTo(Amount.ofZero());
        assertThat(shoppingCart.getDeliveryCost()).isEqualTo(Amount.ofZero());
    }

    @Test
    public void givenShoppingCart_whenDiscountProvided_thenDiscountApplied() {
        //given
        ShoppingCart shoppingCart = new ShoppingCart();
        Category category = new Category(Title.valueOf("Category"));
        Product product = new Product(Title.valueOf("Product"), Amount.valueOf(10D), category);
        Quantity quantity = Quantity.valueOf(10);

        assertThat(product).isNotNull();
        assertThat(quantity).isNotNull();
        assertThat(quantity.doubleValue()).isPositive();

        shoppingCart.addProduct(product, quantity);

        //Mock discount: If more then x items in cart, then apply discount amount of y
        ProductQuantityBasedMockDiscount productQuantityBasedMockDiscount = new ProductQuantityBasedMockDiscount(Quantity.valueOf(5), Amount.valueOf(10D));
        DiscountProvidedEvent event = new DiscountProvidedEvent(productQuantityBasedMockDiscount);

        //when
        assertThat(event).isNotNull();
        shoppingCart.discountProvided(event);
        shoppingCart.applyDiscounts();

        //then
        assertThat(shoppingCart.getDiscountMap()).hasSize(1);
        assertThat(shoppingCart.getCartAmount()).isEqualTo(Amount.valueOf(90D));
        assertThat(shoppingCart.getCartAmountWithoutDiscount()).isEqualTo(Amount.valueOf(100D));
        assertThat(shoppingCart.getCartAmount()).isNotEqualTo(shoppingCart.getCartAmountWithoutDiscount());
        assertThat(shoppingCart.getTotalDiscount()).isEqualTo(Amount.valueOf(10D));
        assertThat(shoppingCart.getDeliveryCost()).isEqualTo(Amount.ofZero());
    }

    @Test
    public void givenShoppingCartAndValidDeliveryCost_whenDiscountProvided_thenDiscountApplied() {
        //given
        ShoppingCart shoppingCart = new ShoppingCart();
        Category category = new Category(Title.valueOf("Category"));
        Product product = new Product(Title.valueOf("Product"), Amount.valueOf(10D), category);
        Quantity quantity = Quantity.valueOf(10);
        Amount deliveryCost = Amount.valueOf(20D);

        assertThat(product).isNotNull();
        assertThat(quantity).isNotNull();
        assertThat(quantity.doubleValue()).isPositive();
        assertThat(deliveryCost).isNotNull();
        assertThat(deliveryCost.doubleValue()).isPositive();

        shoppingCart.addProduct(product, quantity);

        //Mock discount: If more then x items in cart, then apply discount amount of y
        ProductQuantityBasedMockDiscount productQuantityBasedMockDiscount = new ProductQuantityBasedMockDiscount(Quantity.valueOf(5), Amount.valueOf(10D));
        DiscountProvidedEvent event = new DiscountProvidedEvent(productQuantityBasedMockDiscount);

        //when
        assertThat(event).isNotNull();
        shoppingCart.discountProvided(event);
        shoppingCart.applyDiscounts();

        shoppingCart.setDeliveryCost(deliveryCost);
        shoppingCart.applyDeliveryCost();

        assertThat(shoppingCart.getDiscountMap()).hasSize(1);
        assertThat(shoppingCart.getCartAmount()).isEqualTo(Amount.valueOf(90D));
        assertThat(shoppingCart.getCartAmountWithoutDiscount()).isEqualTo(Amount.valueOf(100D));
        assertThat(shoppingCart.getCartAmount()).isNotEqualTo(shoppingCart.getCartAmountWithoutDiscount());
        assertThat(shoppingCart.getTotalAmount()).isEqualTo(Amount.valueOf(110D));
    }

    @Test
    public void givenShoppingCart_whenMultipleDiscountsProvided_thenApplyDistinctDiscounts() {
        //given
        ShoppingCart shoppingCart = new ShoppingCart();
        Category category = new Category(Title.valueOf("Category"));
        Product product1 = new Product(Title.valueOf("Product1"), Amount.valueOf(10D), category);
        Product product2 = new Product(Title.valueOf("Product2"), Amount.valueOf(20D), category);
        Quantity quantity1 = Quantity.valueOf(2);
        Quantity quantity2 = Quantity.valueOf(4);

        assertThat(product1).isNotNull();
        assertThat(product2).isNotNull();
        assertThat(quantity1).isNotNull();
        assertThat(quantity1.doubleValue()).isPositive();
        assertThat(quantity2).isNotNull();
        assertThat(quantity2.doubleValue()).isPositive();

        shoppingCart.addProduct(product1, quantity1);
        shoppingCart.addProduct(product2, quantity2);

        //Mock discount: If greater equals then x items in cart, then apply discount amount of y
        ProductQuantityBasedMockDiscount productQuantityBasedMockDiscount1 = new ProductQuantityBasedMockDiscount(Quantity.valueOf(5), Amount.valueOf(10D));
        ProductQuantityBasedMockDiscount productQuantityBasedMockDiscount2 = new ProductQuantityBasedMockDiscount(Quantity.valueOf(10), Amount.valueOf(15D));
        DiscountProvidedEvent event1 = new DiscountProvidedEvent(productQuantityBasedMockDiscount1);
        DiscountProvidedEvent event2 = new DiscountProvidedEvent(productQuantityBasedMockDiscount2);

        //Mock discount: If specific product x exists in cart, then apply discount amount of y
        ProductTypeBasedMockDiscount productTypeBasedMockDiscount = new ProductTypeBasedMockDiscount(product2, Amount.valueOf(10D));
        DiscountProvidedEvent event3 = new DiscountProvidedEvent(productTypeBasedMockDiscount);

        //when
        assertThat(event1).isNotNull();
        shoppingCart.discountProvided(event1);
        assertThat(event2).isNotNull();
        shoppingCart.discountProvided(event2);
        assertThat(event3).isNotNull();
        shoppingCart.discountProvided(event3);

        shoppingCart.applyDiscounts();

        //then
        assertThat(shoppingCart.getDiscountMap()).hasSize(2);
        assertThat(shoppingCart.getCartAmount()).isEqualTo(Amount.valueOf(80D));
        assertThat(shoppingCart.getCartAmountWithoutDiscount()).isEqualTo(Amount.valueOf(100D));
        assertThat(shoppingCart.getCartAmount()).isNotEqualTo(shoppingCart.getCartAmountWithoutDiscount());
        assertThat(shoppingCart.getTotalDiscount()).isEqualTo(Amount.valueOf(20D));
        assertThat(shoppingCart.getDeliveryCost()).isEqualTo(Amount.ofZero());
    }

    @Test
    public void givenShoppingCart_whenDiscountProvidedAndDiscountAmountIsGreaterThanCartAmount_thenAmpplyDiscountAndZeroCartAmount() {
        //given
        ShoppingCart shoppingCart = new ShoppingCart();
        Category category = new Category(Title.valueOf("Category"));
        Product product = new Product(Title.valueOf("Product"), Amount.valueOf(1D), category);
        Quantity quantity = Quantity.valueOf(5);

        assertThat(product).isNotNull();
        assertThat(quantity).isNotNull();
        assertThat(quantity.doubleValue()).isPositive();

        shoppingCart.addProduct(product, quantity);

        //Mock discount: If more then x items in cart, then apply discount amount of y
        ProductQuantityBasedMockDiscount productQuantityBasedMockDiscount = new ProductQuantityBasedMockDiscount(Quantity.valueOf(5), Amount.valueOf(10D));
        DiscountProvidedEvent event = new DiscountProvidedEvent(productQuantityBasedMockDiscount);

        //when
        assertThat(event).isNotNull();
        shoppingCart.discountProvided(event);
        shoppingCart.applyDiscounts();

        //then
        assertThat(shoppingCart.getDiscountMap()).hasSize(1);
        assertThat(shoppingCart.getCartAmount()).isEqualTo(Amount.ofZero());
        assertThat(shoppingCart.getCartAmountWithoutDiscount()).isEqualTo(Amount.valueOf(5D));
        assertThat(shoppingCart.getCartAmount()).isNotEqualTo(shoppingCart.getCartAmountWithoutDiscount());
        assertThat(shoppingCart.getTotalDiscount()).isEqualTo(Amount.valueOf(10D));
        assertThat(shoppingCart.getDeliveryCost()).isEqualTo(Amount.ofZero());
    }

    @Test
    public void givenShoppingCart_whenMultipleSameTypeDiscountProvided_thenApplyGreatestDiscount() {
        //given
        ShoppingCart shoppingCart = new ShoppingCart();
        Category category = new Category(Title.valueOf("Category"));
        Product product = new Product(Title.valueOf("Product"), Amount.valueOf(10D), category);
        Quantity quantity = Quantity.valueOf(10);

        assertThat(product).isNotNull();
        assertThat(quantity).isNotNull();
        assertThat(quantity.doubleValue()).isPositive();

        shoppingCart.addProduct(product, quantity);

        //Mock discount: If greater equals then x items in cart, then apply discount amount of y
        ProductQuantityBasedMockDiscount productQuantityBasedMockDiscount1 = new ProductQuantityBasedMockDiscount(Quantity.valueOf(5), Amount.valueOf(10D));
        ProductQuantityBasedMockDiscount productQuantityBasedMockDiscount2 = new ProductQuantityBasedMockDiscount(Quantity.valueOf(10), Amount.valueOf(15D));
        DiscountProvidedEvent event1 = new DiscountProvidedEvent(productQuantityBasedMockDiscount1);
        DiscountProvidedEvent event2 = new DiscountProvidedEvent(productQuantityBasedMockDiscount2);

        //when
        assertThat(event1).isNotNull();
        shoppingCart.discountProvided(event1);
        assertThat(event2).isNotNull();
        shoppingCart.discountProvided(event2);

        shoppingCart.applyDiscounts();

        //then
        assertThat(shoppingCart.getDiscountMap()).hasSize(1);
        assertThat(shoppingCart.getCartAmount()).isEqualTo(Amount.valueOf(85D));
        assertThat(shoppingCart.getCartAmountWithoutDiscount()).isEqualTo(Amount.valueOf(100D));
        assertThat(shoppingCart.getCartAmount()).isNotEqualTo(shoppingCart.getCartAmountWithoutDiscount());
        assertThat(shoppingCart.getTotalDiscount()).isEqualTo(Amount.valueOf(15D));
        assertThat(shoppingCart.getDeliveryCost()).isEqualTo(Amount.ofZero());
    }

    @Test
    public void givenShoppingCartAndValidDeliveryCost_whenShoppingCartPrinted_thenPrintShoppingCartInfoToConsole() {
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        final PrintStream originalOut = System.out;

        System.setOut(new PrintStream(outContent));

        //given
        ShoppingCart shoppingCart = new ShoppingCart();
        Category category = new Category(Title.valueOf("Category"));
        Product product = new Product(Title.valueOf("Product"), Amount.valueOf(10D), category);
        Quantity quantity = Quantity.valueOf(10);
        Amount deliveryCost = Amount.valueOf(20D);

        assertThat(product).isNotNull();
        assertThat(quantity).isNotNull();
        assertThat(quantity.doubleValue()).isPositive();
        assertThat(deliveryCost).isNotNull();
        assertThat(deliveryCost.doubleValue()).isPositive();

        shoppingCart.addProduct(product, quantity);

        //Mock discount: If more then x items in cart, then apply discount amount of y
        ProductQuantityBasedMockDiscount productQuantityBasedMockDiscount = new ProductQuantityBasedMockDiscount(Quantity.valueOf(5), Amount.valueOf(10D));
        DiscountProvidedEvent event = new DiscountProvidedEvent(productQuantityBasedMockDiscount);

        assertThat(event).isNotNull();
        shoppingCart.discountProvided(event);
        shoppingCart.applyDiscounts();

        shoppingCart.setDeliveryCost(deliveryCost);
        shoppingCart.applyDeliveryCost();

        assertThat(shoppingCart.getDiscountMap()).hasSize(1);
        assertThat(shoppingCart.getCartAmount()).isEqualTo(Amount.valueOf(90D));
        assertThat(shoppingCart.getCartAmountWithoutDiscount()).isEqualTo(Amount.valueOf(100D));
        assertThat(shoppingCart.getCartAmount()).isNotEqualTo(shoppingCart.getCartAmountWithoutDiscount());
        assertThat(shoppingCart.getTotalAmount()).isEqualTo(Amount.valueOf(110D));

        //when
        shoppingCart.print();

        //then
        assertThat(outContent).asString().contains("Shopping Cart Info:");
        shoppingCart.getCartItems()
                .stream()
                .collect(Collectors.groupingBy(CartItem::getCategory))
                .forEach((key, value) -> {
                    assertThat(outContent).asString().contains(key.toString());
                    value.forEach(cartItem -> assertThat(outContent).asString().contains(cartItem.toString()));
                });
        assertThat(outContent).asString().contains("Cart Amount: " + shoppingCart.getCartAmountWithoutDiscount());
        shoppingCart.getDiscountMap().forEach((key, value) -> assertThat(outContent).asString().contains(key + ": " + value.getDiscountAmount()));
        assertThat(outContent).asString().contains(productQuantityBasedMockDiscount.getDiscountName() + ": " + productQuantityBasedMockDiscount.getDiscountAmount());
        assertThat(outContent).asString().contains("Total Discount Amount: " + shoppingCart.getTotalDiscount());
        assertThat(outContent).asString().contains("Total Cart Amount After Discount(s): " + shoppingCart.getCartAmount());
        assertThat(outContent).asString().contains("Delivery Cost: " + shoppingCart.getDeliveryCost());
        assertThat(outContent).asString().contains("Total Amount(Cart Amount After Discount(s) + Delivery Cost): " + shoppingCart.getTotalAmount());


        System.setOut(originalOut);
    }
}
