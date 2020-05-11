package com.trendyol.shoppingcart.client.mock;

import com.trendyol.shoppingcart.core.domain.CartItem;
import com.trendyol.shoppingcart.core.domain.Category;
import com.trendyol.shoppingcart.core.domain.Product;
import com.trendyol.shoppingcart.core.domain.ShoppingCart;
import com.trendyol.shoppingcart.core.domain.value.Amount;
import com.trendyol.shoppingcart.core.domain.value.Quantity;
import com.trendyol.shoppingcart.core.domain.value.Title;
import com.trendyol.shoppingcart.core.service.ProductService;
import com.trendyol.shoppingcart.core.service.ShoppingCartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class MockShoppingCartClientTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    private MockShoppingCartClient mockShoppingCartClient;
    private ShoppingCart shoppingCart;

    private static final Category food = food();
    private static final Product apple = apple();
    private static final Product pear = pear();
    private static final Product banana = banana();

    private static Category food() {
        return new Category(Title.valueOf("FOOD"));
    }

    private static Product apple() {
        return new Product(Title.valueOf("APPLE"), Amount.valueOf(10D), food);
    }

    private static Product pear() {
        return new Product(Title.valueOf("PEAR"), Amount.valueOf(15D), food);
    }

    private static Product banana() {
        return new Product(Title.valueOf("BANANA"), Amount.valueOf(20D), food);
    }

    @BeforeEach
    public void beforeEach() {
        ProductService productService = mock(ProductService.class);
        when(productService.get(Title.valueOf("APPLE"))).thenReturn(Optional.of(apple));
        when(productService.get(Title.valueOf("PEAR"))).thenReturn(Optional.of(pear));
        when(productService.get(Title.valueOf("BANANA"))).thenReturn(Optional.of(banana));

        this.shoppingCart = mock(ShoppingCart.class, withSettings().useConstructor().defaultAnswer(CALLS_REAL_METHODS));

        ShoppingCartService shoppingCartService = mock(ShoppingCartService.class);
        when(shoppingCartService.save(any(ShoppingCart.class))).thenReturn(shoppingCart);

        this.mockShoppingCartClient = new MockShoppingCartClient(shoppingCartService, productService);
    }

    @Test
    public void givenShoppingCartClient_whenClientCreated_thenAnyAppleExistsThenApplyAmountOf2DiscountShouldBeRegistered() {

    }

    @Test
    public void givenShoppingCartClient_whenClientCreated_thenAnyBananaExistsThenApplyAmountOf5DiscountShouldBeRegistered() {

    }

    @Test
    public void givenShoppingCartClient_whenClientCreated_ProductQuantityGreaterEquals5ThenApplyDiscountAmountOf10DiscountShouldBeRegistered() {

    }

    @Test
    public void givenShoppingCartClient_whenClientCreated_ProductQuantityGreaterEquals10ThenApplyDiscountAmountOf15DiscountShouldBeRegistered() {

    }

    @Test
    public void givenShoppingCartClient_whenCartHasOnly1AppleAndCartProductQuantityLesserThan5_thenApplyAmountOf2Discount() {
        //given
        shoppingCart.addProduct(apple, Quantity.valueOf(1));

        //when
        mockShoppingCartClient.submitCart(shoppingCart);
        //AnyAppleExistsThenApplyAmountOf2 discount should be applied to shopping cart

        //then
        assertThat(mockShoppingCartClient.getRuleDiscountProvider().getChildDiscountProviders()).isNotEmpty();
        assertThat(shoppingCart.getDiscountMap()).hasSize(1);
        assertThat(shoppingCart.getCartAmount()).isEqualTo(Amount.valueOf(8D));
        assertThat(shoppingCart.getCartAmountWithoutDiscount()).isEqualTo(Amount.valueOf(10D));
        assertThat(shoppingCart.getCartAmount()).isNotEqualTo(shoppingCart.getCartAmountWithoutDiscount());
        assertThat(shoppingCart.getTotalDiscount()).isEqualTo(Amount.valueOf(2D));
    }

    @Test
    public void givenShoppingCartClient_whenCartHasOnly1PearAndCartProductQuantityLesserThan5_thenDoNotApplyAnyDiscount() {
        //given
        shoppingCart.addProduct(pear, Quantity.valueOf(1));

        //when
        mockShoppingCartClient.submitCart(shoppingCart);
        //Any discount should not be applied to shopping cart

        //then
        assertThat(shoppingCart.getDiscountMap()).hasSize(0);
        assertThat(shoppingCart.getCartAmount()).isEqualTo(Amount.valueOf(15D));
        assertThat(shoppingCart.getCartAmountWithoutDiscount()).isEqualTo(Amount.valueOf(15D));
        assertThat(shoppingCart.getCartAmount()).isEqualTo(shoppingCart.getCartAmountWithoutDiscount());
        assertThat(shoppingCart.getTotalDiscount()).isEqualTo(Amount.valueOf(0D));
    }

    @Test
    public void givenShoppingCartClient_whenCartHasOnly1BananaAndCartProductQuantityLesserThan5_thenApplyAmountOf5Discount() {
        //given
        shoppingCart.addProduct(banana, Quantity.valueOf(1));

        //when
        mockShoppingCartClient.submitCart(shoppingCart);
        //AnyBananaExistsThenApplyAmountOf5 discount should be applied to shopping cart

        //then
        assertThat(shoppingCart.getDiscountMap()).hasSize(1);
        assertThat(shoppingCart.getCartAmount()).isEqualTo(Amount.valueOf(15D));
        assertThat(shoppingCart.getCartAmountWithoutDiscount()).isEqualTo(Amount.valueOf(20D));
        assertThat(shoppingCart.getCartAmount()).isNotEqualTo(shoppingCart.getCartAmountWithoutDiscount());
        assertThat(shoppingCart.getTotalDiscount()).isEqualTo(Amount.valueOf(5D));
    }

    @Test
    public void givenShoppingCartClient_whenCartHas1AppleAnd1BananaAndCartProductQuantityLesserThan5_thenApplyAmountOf7Discount() {
        //given
        shoppingCart.addProduct(apple, Quantity.valueOf(1));
        shoppingCart.addProduct(banana, Quantity.valueOf(1));

        //when
        mockShoppingCartClient.submitCart(shoppingCart);
        //AnyAppleExistsThenApplyAmountOf2 and AnyBananaExistsThenApplyAmountOf5 discounts should be applied to shopping cart

        //then
        assertThat(shoppingCart.getDiscountMap()).hasSize(2);
        assertThat(shoppingCart.getCartAmount()).isEqualTo(Amount.valueOf(23D));
        assertThat(shoppingCart.getCartAmountWithoutDiscount()).isEqualTo(Amount.valueOf(30D));
        assertThat(shoppingCart.getCartAmount()).isNotEqualTo(shoppingCart.getCartAmountWithoutDiscount());
        assertThat(shoppingCart.getTotalDiscount()).isEqualTo(Amount.valueOf(7D));
    }

    @Test
    public void givenShoppingCartClient_whenCartHasOnly7PearsAndCartProductQuantityGreaterOrEquals5_thenApplyAmountOf10Discount() {
        //given
        shoppingCart.addProduct(pear, Quantity.valueOf(6));

        //when
        mockShoppingCartClient.submitCart(shoppingCart);
        //ProductQuantityGreaterEquals5ThenApplyDiscountAmountOf10 discount should be applied to shopping cart

        //then
        assertThat(shoppingCart.getDiscountMap()).hasSize(1);
        assertThat(shoppingCart.getCartAmount()).isEqualTo(Amount.valueOf(80D));
        assertThat(shoppingCart.getCartAmountWithoutDiscount()).isEqualTo(Amount.valueOf(90D));
        assertThat(shoppingCart.getCartAmount()).isNotEqualTo(shoppingCart.getCartAmountWithoutDiscount());
        assertThat(shoppingCart.getTotalDiscount()).isEqualTo(Amount.valueOf(10D));
    }

    @Test
    public void givenShoppingCartClient_whenCartHasOnly6ApplesAndCartProductQuantityGreaterOrEquals5_thenApplyAmountOf12Discount() {
        //given
        shoppingCart.addProduct(apple, Quantity.valueOf(6));

        //when
        mockShoppingCartClient.submitCart(shoppingCart);
        //AnyAppleExistsThenApplyAmountOf2 and ProductQuantityGreaterEquals5ThenApplyDiscountAmountOf10 discounts should be applied to shopping cart

        //then
        assertThat(shoppingCart.getDiscountMap()).hasSize(2);
        assertThat(shoppingCart.getCartAmount()).isEqualTo(Amount.valueOf(48D));
        assertThat(shoppingCart.getCartAmountWithoutDiscount()).isEqualTo(Amount.valueOf(60D));
        assertThat(shoppingCart.getCartAmount()).isNotEqualTo(shoppingCart.getCartAmountWithoutDiscount());
        assertThat(shoppingCart.getTotalDiscount()).isEqualTo(Amount.valueOf(12D));
    }

    @Test
    public void givenShoppingCartClient_whenCartHasOnly6BananasAndCartProductQuantityGreaterOrEquals5_thenApplyAmountOf15Discount() {
        //given
        shoppingCart.addProduct(banana, Quantity.valueOf(6));

        //when
        mockShoppingCartClient.submitCart(shoppingCart);
        //AnyBananaExistsThenApplyAmountOf5 and ProductQuantityGreaterEquals5ThenApplyDiscountAmountOf10 discounts should be applied to shopping cart

        //then
        assertThat(shoppingCart.getDiscountMap()).hasSize(2);
        assertThat(shoppingCart.getCartAmount()).isEqualTo(Amount.valueOf(105D));
        assertThat(shoppingCart.getCartAmountWithoutDiscount()).isEqualTo(Amount.valueOf(120D));
        assertThat(shoppingCart.getCartAmount()).isNotEqualTo(shoppingCart.getCartAmountWithoutDiscount());
        assertThat(shoppingCart.getTotalDiscount()).isEqualTo(Amount.valueOf(15D));
    }

    @Test
    public void givenShoppingCartClient_whenCartHas4ApplesAnd2BananasAndCartProductQuantityGreaterOrEquals5_thenApplyAmountOf17Discount() {
        //given
        shoppingCart.addProduct(apple, Quantity.valueOf(4));
        shoppingCart.addProduct(banana, Quantity.valueOf(2));

        //when
        mockShoppingCartClient.submitCart(shoppingCart);
        //AnyAppleExistsThenApplyAmountOf2, AnyBananaExistsThenApplyAmountOf5 and ProductQuantityGreaterEquals5ThenApplyDiscountAmountOf10 discounts should be applied to shopping cart

        //then
        assertThat(shoppingCart.getDiscountMap()).hasSize(3);
        assertThat(shoppingCart.getCartAmount()).isEqualTo(Amount.valueOf(63D));
        assertThat(shoppingCart.getCartAmountWithoutDiscount()).isEqualTo(Amount.valueOf(80D));
        assertThat(shoppingCart.getCartAmount()).isNotEqualTo(shoppingCart.getCartAmountWithoutDiscount());
        assertThat(shoppingCart.getTotalDiscount()).isEqualTo(Amount.valueOf(17D));
    }

    @Test
    public void givenShoppingCartClient_whenCartHasOnly10PearsAndCartProductQuantityGreaterOrEquals10_thenApplyAmountOf15Discount() {
        //given
        shoppingCart.addProduct(pear, Quantity.valueOf(10));

        //when
        mockShoppingCartClient.submitCart(shoppingCart);
        //ProductQuantityGreaterEquals10ThenApplyDiscountAmountOf15 discount should be applied to shopping cart

        //then
        assertThat(shoppingCart.getDiscountMap()).hasSize(1);
        assertThat(shoppingCart.getCartAmount()).isEqualTo(Amount.valueOf(135D));
        assertThat(shoppingCart.getCartAmountWithoutDiscount()).isEqualTo(Amount.valueOf(150D));
        assertThat(shoppingCart.getCartAmount()).isNotEqualTo(shoppingCart.getCartAmountWithoutDiscount());
        assertThat(shoppingCart.getTotalDiscount()).isEqualTo(Amount.valueOf(15D));
    }

    @Test
    public void givenShoppingCartClient_whenCartHasOnly10ApplesAndCartProductQuantityGreaterOrEquals10_thenApplyAmountOf17Discount() {
        //given
        shoppingCart.addProduct(apple, Quantity.valueOf(10));

        //when
        mockShoppingCartClient.submitCart(shoppingCart);
        //AnyAppleExistsThenApplyAmountOf2 and ProductQuantityGreaterEquals10ThenApplyDiscountAmountOf15 discounts should be applied to shopping cart

        //then
        assertThat(shoppingCart.getDiscountMap()).hasSize(2);
        assertThat(shoppingCart.getCartAmount()).isEqualTo(Amount.valueOf(83D));
        assertThat(shoppingCart.getCartAmountWithoutDiscount()).isEqualTo(Amount.valueOf(100D));
        assertThat(shoppingCart.getCartAmount()).isNotEqualTo(shoppingCart.getCartAmountWithoutDiscount());
        assertThat(shoppingCart.getTotalDiscount()).isEqualTo(Amount.valueOf(17D));
    }

    @Test
    public void givenShoppingCartClient_whenCartHasOnly10BananasAndCartProductQuantityGreaterOrEquals10_thenApplyAmountOf20Discount() {
        //given
        shoppingCart.addProduct(banana, Quantity.valueOf(10));

        //when
        mockShoppingCartClient.submitCart(shoppingCart);
        //AnyBananaExistsThenApplyAmountOf5 and ProductQuantityGreaterEquals10ThenApplyDiscountAmountOf15 discounts should be applied to shopping cart

        //then
        assertThat(shoppingCart.getDiscountMap()).hasSize(2);
        assertThat(shoppingCart.getCartAmount()).isEqualTo(Amount.valueOf(180D));
        assertThat(shoppingCart.getCartAmountWithoutDiscount()).isEqualTo(Amount.valueOf(200D));
        assertThat(shoppingCart.getCartAmount()).isNotEqualTo(shoppingCart.getCartAmountWithoutDiscount());
        assertThat(shoppingCart.getTotalDiscount()).isEqualTo(Amount.valueOf(20D));
    }

    @Test
    public void givenShoppingCartClient_whenCartHas7Apples4BananasAndCartProductQuantityGreaterOrEquals10_thenApplyAmountOf22Discount() {
        //given
        shoppingCart.addProduct(apple, Quantity.valueOf(7));
        shoppingCart.addProduct(banana, Quantity.valueOf(4));

        //when
        mockShoppingCartClient.submitCart(shoppingCart);
        //AnyAppleExistsThenApplyAmountOf2, AnyBananaExistsThenApplyAmountOf5 and ProductQuantityGreaterEquals10ThenApplyDiscountAmountOf15 discounts should be applied to shopping cart

        //then
        assertThat(shoppingCart.getDiscountMap()).hasSize(3);
        assertThat(shoppingCart.getCartAmount()).isEqualTo(Amount.valueOf(128D));
        assertThat(shoppingCart.getCartAmountWithoutDiscount()).isEqualTo(Amount.valueOf(150D));
        assertThat(shoppingCart.getCartAmount()).isNotEqualTo(shoppingCart.getCartAmountWithoutDiscount());
        assertThat(shoppingCart.getTotalDiscount()).isEqualTo(Amount.valueOf(22D));
    }

    @Test
    public void givenShoppingCartClient_whenCartHas4Apples2Pears1BananasAndCartProductQuantityGreaterOrEquals5_thenApplyAmountOf17Discount() {
        //given
        shoppingCart.addProduct(apple, Quantity.valueOf(3));
        shoppingCart.addProduct(pear, Quantity.valueOf(2));
        shoppingCart.addProduct(banana, Quantity.valueOf(1));

        //when
        mockShoppingCartClient.submitCart(shoppingCart);
        //AnyAppleExistsThenApplyAmountOf2, AnyBananaExistsThenApplyAmountOf5 and ProductQuantityGreaterEquals5ThenApplyDiscountAmountOf10 discounts should be applied to shopping cart

        //then
        assertThat(shoppingCart.getDiscountMap()).hasSize(3);
        assertThat(shoppingCart.getCartAmount()).isEqualTo(Amount.valueOf(63D));
        assertThat(shoppingCart.getCartAmountWithoutDiscount()).isEqualTo(Amount.valueOf(80D));
        assertThat(shoppingCart.getCartAmount()).isNotEqualTo(shoppingCart.getCartAmountWithoutDiscount());
        assertThat(shoppingCart.getTotalDiscount()).isEqualTo(Amount.valueOf(17D));
    }

    @Test
    public void givenShoppingCartClient_whenCartHas4Apples2Pears1BananasAndCartProductQuantityGreaterOrEquals10_thenApplyAmountOf22Discount() {
        //given
        shoppingCart.addProduct(apple, Quantity.valueOf(4));
        shoppingCart.addProduct(pear, Quantity.valueOf(3));
        shoppingCart.addProduct(banana, Quantity.valueOf(3));

        //when
        mockShoppingCartClient.submitCart(shoppingCart);
        //AnyAppleExistsThenApplyAmountOf2, AnyBananaExistsThenApplyAmountOf5 and ProductQuantityGreaterEquals10ThenApplyDiscountAmountOf15 discounts should be applied to shopping cart

        //then
        assertThat(shoppingCart.getDiscountMap()).hasSize(3);
        assertThat(shoppingCart.getCartAmount()).isEqualTo(Amount.valueOf(123D));
        assertThat(shoppingCart.getCartAmountWithoutDiscount()).isEqualTo(Amount.valueOf(145D));
        assertThat(shoppingCart.getCartAmount()).isNotEqualTo(shoppingCart.getCartAmountWithoutDiscount());
        assertThat(shoppingCart.getTotalDiscount()).isEqualTo(Amount.valueOf(22D));
    }


    @Test
    public void givenShoppingCartClient_whenSubmitCart_thenDoNotApplyDeliveryCostIfDeliveryCostServiceNull() {
        //given
        shoppingCart.addProduct(apple, Quantity.valueOf(3));
        shoppingCart.addProduct(pear, Quantity.valueOf(2));
        shoppingCart.addProduct(banana, Quantity.valueOf(1));

        //when
        mockShoppingCartClient.submitCart(shoppingCart);
        //AnyAppleExistsThenApplyAmountOf2, AnyBananaExistsThenApplyAmountOf5 and ProductQuantityGreaterEquals5ThenApplyDiscountAmountOf10 discounts should be applied to shopping cart

        //then
        assertThat(shoppingCart.getDeliveryCost()).isEqualTo(Amount.ofZero());
        assertThat(shoppingCart.getTotalAmount()).isEqualTo(shoppingCart.getCartAmount());
    }

    @Test
    public void givenShoppingCartClient_whenSubmitCart_thenPrintShoppingCartInfo() {
        //given
        System.setOut(new PrintStream(outContent));

        shoppingCart.addProduct(apple, Quantity.valueOf(3));
        shoppingCart.addProduct(pear, Quantity.valueOf(2));
        shoppingCart.addProduct(banana, Quantity.valueOf(1));

        //when
        mockShoppingCartClient.submitCart(shoppingCart);
        //AnyAppleExistsThenApplyAmountOf2, AnyBananaExistsThenApplyAmountOf5 and ProductQuantityGreaterEquals5ThenApplyDiscountAmountOf10 discounts should be applied to shopping cart

        //then
        assertThat(shoppingCart.getDiscountMap()).hasSize(3);
        assertThat(shoppingCart.getCartAmount()).isEqualTo(Amount.valueOf(63D));
        assertThat(shoppingCart.getCartAmountWithoutDiscount()).isEqualTo(Amount.valueOf(80D));
        assertThat(shoppingCart.getCartAmount()).isNotEqualTo(shoppingCart.getCartAmountWithoutDiscount());
        assertThat(shoppingCart.getTotalDiscount()).isEqualTo(Amount.valueOf(17D));

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
        assertThat(outContent).asString().contains("Total Discount Amount: " + shoppingCart.getTotalDiscount());
        assertThat(outContent).asString().contains("Total Cart Amount After Discount(s): " + shoppingCart.getCartAmount());
        assertThat(outContent).asString().contains("Delivery Cost: " + shoppingCart.getDeliveryCost());
        assertThat(outContent).asString().contains("Total Amount(Cart Amount After Discount(s) + Delivery Cost): " + shoppingCart.getTotalAmount());


        System.setOut(originalOut);
    }

    @Test
    public void givenShoppingCartClient_whenSubmitCart_thenAlwaysProvideProductTypeBasedDiscountsFirst() {
        //given
        shoppingCart.addProduct(apple, Quantity.valueOf(3));
        shoppingCart.addProduct(pear, Quantity.valueOf(2));
        shoppingCart.addProduct(banana, Quantity.valueOf(1));

        //when
        mockShoppingCartClient.submitCart(shoppingCart);
        //AnyAppleExistsThenApplyAmountOf2, AnyBananaExistsThenApplyAmountOf5 and ProductQuantityGreaterEquals5ThenApplyDiscountAmountOf10 discounts should be applied to shopping cart

        //then

    }
}
