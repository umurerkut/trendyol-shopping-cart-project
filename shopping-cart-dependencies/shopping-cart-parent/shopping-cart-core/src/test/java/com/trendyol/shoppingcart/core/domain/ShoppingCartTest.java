package com.trendyol.shoppingcart.core.domain;

import com.trendyol.shoppingcart.core.discountprovider.listener.DiscountProvidedEvent;
import com.trendyol.shoppingcart.core.domain.discount.Discount;
import com.trendyol.shoppingcart.core.domain.discount.calculation.DiscountCalculationStrategy;
import com.trendyol.shoppingcart.core.domain.discount.validation.DiscountValidationStrategy;
import com.trendyol.shoppingcart.core.domain.value.Amount;
import com.trendyol.shoppingcart.core.domain.value.DiscountName;
import com.trendyol.shoppingcart.core.domain.value.Quantity;
import com.trendyol.shoppingcart.core.domain.value.Title;
import com.trendyol.shoppingcart.core.exception.InvalidValueException;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.*;

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

        //Mock discount: If >= 5 items in cart, then apply discount amount of 10
        DiscountName discountName = DiscountName.valueOf("MOCK");
        assertThat(discountName).isNotNull();

        DiscountCalculationStrategy discountCalculationStrategy = mock(DiscountCalculationStrategy.class);
        assertThat(discountCalculationStrategy).isNotNull();
        when(discountCalculationStrategy.calculateDiscountAmount(shoppingCart)).thenReturn(Amount.valueOf(10D));

        DiscountValidationStrategy discountValidationStrategy = mock(DiscountValidationStrategy.class);
        assertThat(discountValidationStrategy).isNotNull();
        assertThat(shoppingCart.getTotalQuantityOfProductsInCart()).isGreaterThanOrEqualTo(Quantity.valueOf(5));
        when(discountValidationStrategy.isValid(shoppingCart)).thenReturn(true);

        Discount discount = mock(Discount.class, withSettings()
                .useConstructor(discountName, discountValidationStrategy, discountCalculationStrategy)
                .defaultAnswer(CALLS_REAL_METHODS));
        assertThat(discount).isNotNull();
        when(discount.isGreaterThan(null)).thenReturn(true);

        DiscountProvidedEvent event = mock(DiscountProvidedEvent.class);
        assertThat(event).isNotNull();
        when(event.getDiscount()).thenReturn(discount);

        //when
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

        //Mock discount: If >= 5 items in cart, then apply discount amount of 10
        DiscountName discountName = DiscountName.valueOf("MOCK");
        assertThat(discountName).isNotNull();

        DiscountCalculationStrategy discountCalculationStrategy = mock(DiscountCalculationStrategy.class);
        assertThat(discountCalculationStrategy).isNotNull();
        when(discountCalculationStrategy.calculateDiscountAmount(shoppingCart)).thenReturn(Amount.valueOf(10D));

        DiscountValidationStrategy discountValidationStrategy = mock(DiscountValidationStrategy.class);
        assertThat(discountValidationStrategy).isNotNull();
        assertThat(shoppingCart.getTotalQuantityOfProductsInCart()).isGreaterThanOrEqualTo(Quantity.valueOf(5));
        when(discountValidationStrategy.isValid(shoppingCart)).thenReturn(true);

        Discount discount = mock(Discount.class, withSettings()
                .useConstructor(discountName, discountValidationStrategy, discountCalculationStrategy)
                .defaultAnswer(CALLS_REAL_METHODS));
        assertThat(discount).isNotNull();
        when(discount.isGreaterThan(null)).thenReturn(true);

        DiscountProvidedEvent event = mock(DiscountProvidedEvent.class);
        assertThat(event).isNotNull();
        when(event.getDiscount()).thenReturn(discount);

        //when
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

        //Mock discount 1: If >= 5 items in cart, then apply discount amount of 10
        //Mock discount 2: If >= 10 items in cart, then apply discount amount of 15
        DiscountName quantityBasedDiscount = DiscountName.valueOf("QuantityBasedDiscount");
        assertThat(quantityBasedDiscount).isNotNull();

        DiscountCalculationStrategy discountCalculationStrategy1 = mock(DiscountCalculationStrategy.class);
        assertThat(discountCalculationStrategy1).isNotNull();
        when(discountCalculationStrategy1.calculateDiscountAmount(shoppingCart)).thenReturn(Amount.valueOf(10D));

        DiscountCalculationStrategy discountCalculationStrategy2 = mock(DiscountCalculationStrategy.class);
        assertThat(discountCalculationStrategy2).isNotNull();
        when(discountCalculationStrategy2.calculateDiscountAmount(shoppingCart)).thenReturn(Amount.valueOf(15D));

        DiscountValidationStrategy discountValidationStrategy1 = mock(DiscountValidationStrategy.class);
        assertThat(discountValidationStrategy1).isNotNull();
        doAnswer(invocationOnMock -> shoppingCart.getTotalQuantityOfProductsInCart().isGreaterThan(Quantity.valueOf(5))
                || shoppingCart.getTotalQuantityOfProductsInCart().equals(Quantity.valueOf(5)))
                .when(discountValidationStrategy1).isValid(shoppingCart);

        DiscountValidationStrategy discountValidationStrategy2 = mock(DiscountValidationStrategy.class);
        assertThat(discountValidationStrategy2).isNotNull();
        doAnswer(invocationOnMock -> shoppingCart.getTotalQuantityOfProductsInCart().isGreaterThan(Quantity.valueOf(10))
                || shoppingCart.getTotalQuantityOfProductsInCart().equals(Quantity.valueOf(10)))
                .when(discountValidationStrategy2).isValid(shoppingCart);

        Discount discount1 = mock(Discount.class, withSettings()
                .useConstructor(quantityBasedDiscount, discountValidationStrategy1, discountCalculationStrategy1)
                .defaultAnswer(CALLS_REAL_METHODS));
        assertThat(discount1).isNotNull();
        Discount discount2 = mock(Discount.class, withSettings()
                .useConstructor(quantityBasedDiscount, discountValidationStrategy2, discountCalculationStrategy2)
                .defaultAnswer(CALLS_REAL_METHODS));
        assertThat(discount2).isNotNull();

        when(discount1.isGreaterThan(null)).thenReturn(true);
        when(discount2.isGreaterThan(null)).thenReturn(true);
        when(discount1.isGreaterThan(discount2)).thenReturn(false);
        when(discount2.isGreaterThan(discount1)).thenReturn(true);

        DiscountProvidedEvent event1 = mock(DiscountProvidedEvent.class);
        assertThat(event1).isNotNull();
        when(event1.getDiscount()).thenReturn(discount1);

        DiscountProvidedEvent event2 = mock(DiscountProvidedEvent.class);
        assertThat(event2).isNotNull();
        when(event2.getDiscount()).thenReturn(discount2);

        //Mock discount3: If Product1 exists in cart, then apply discount amount of 10
        DiscountName specificProductBasedDiscount = DiscountName.valueOf("Product1ExistenceBasedDiscount");
        assertThat(specificProductBasedDiscount).isNotNull();

        DiscountCalculationStrategy discountCalculationStrategy3 = mock(DiscountCalculationStrategy.class);
        assertThat(discountCalculationStrategy3).isNotNull();
        when(discountCalculationStrategy3.calculateDiscountAmount(shoppingCart)).thenReturn(Amount.valueOf(10D));

        DiscountValidationStrategy discountValidationStrategy3 = mock(DiscountValidationStrategy.class);
        assertThat(discountValidationStrategy3).isNotNull();
        doAnswer(invocationOnMock -> shoppingCart.getQuantityOfProductInCart(product1).isGreaterThan(Quantity.valueOf(2))
                || shoppingCart.getQuantityOfProductInCart(product1).equals(Quantity.valueOf(2)))
                .when(discountValidationStrategy3).isValid(shoppingCart);

        Discount discount3 = mock(Discount.class, withSettings()
                .useConstructor(specificProductBasedDiscount, discountValidationStrategy3, discountCalculationStrategy3)
                .defaultAnswer(CALLS_REAL_METHODS));
        assertThat(discount3).isNotNull();
        when(discount3.isGreaterThan(null)).thenReturn(true);

        DiscountProvidedEvent event3 = mock(DiscountProvidedEvent.class);
        assertThat(event3).isNotNull();
        when(event3.getDiscount()).thenReturn(discount3);

        //when
        shoppingCart.discountProvided(event1);
        shoppingCart.discountProvided(event2);
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
    public void givenShoppingCart_whenDiscountProvidedAndDiscountAmountIsGreaterThanCartAmount_thenApplyDiscountAndZeroCartAmount() {
        //given
        ShoppingCart shoppingCart = new ShoppingCart();
        Category category = new Category(Title.valueOf("Category"));
        Product product = new Product(Title.valueOf("Product"), Amount.valueOf(1D), category);
        Quantity quantity = Quantity.valueOf(5);

        assertThat(product).isNotNull();
        assertThat(quantity).isNotNull();
        assertThat(quantity.doubleValue()).isPositive();

        shoppingCart.addProduct(product, quantity);

        //Mock discount: If >= 5 items in cart, then apply discount amount of 10
        DiscountName discountName = DiscountName.valueOf("MOCK");
        assertThat(discountName).isNotNull();

        DiscountCalculationStrategy discountCalculationStrategy = mock(DiscountCalculationStrategy.class);
        assertThat(discountCalculationStrategy).isNotNull();
        when(discountCalculationStrategy.calculateDiscountAmount(shoppingCart)).thenReturn(Amount.valueOf(10D));

        DiscountValidationStrategy discountValidationStrategy = mock(DiscountValidationStrategy.class);
        assertThat(discountValidationStrategy).isNotNull();
        assertThat(shoppingCart.getTotalQuantityOfProductsInCart()).isGreaterThanOrEqualTo(Quantity.valueOf(5));
        when(discountValidationStrategy.isValid(shoppingCart)).thenReturn(true);

        Discount discount = mock(Discount.class, withSettings()
                .useConstructor(discountName, discountValidationStrategy, discountCalculationStrategy)
                .defaultAnswer(CALLS_REAL_METHODS));
        assertThat(discount).isNotNull();
        when(discount.isGreaterThan(null)).thenReturn(true);

        DiscountProvidedEvent event = mock(DiscountProvidedEvent.class);
        assertThat(event).isNotNull();
        when(event.getDiscount()).thenReturn(discount);

        //when

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

        //Mock discount 1: If >= 5 items in cart, then apply discount amount of 10
        //Mock discount 2: If >= 10 items in cart, then apply discount amount of 15
        DiscountName quantityBasedDiscount = DiscountName.valueOf("QuantityBasedDiscount");
        assertThat(quantityBasedDiscount).isNotNull();

        DiscountCalculationStrategy discountCalculationStrategy1 = mock(DiscountCalculationStrategy.class);
        assertThat(discountCalculationStrategy1).isNotNull();
        when(discountCalculationStrategy1.calculateDiscountAmount(shoppingCart)).thenReturn(Amount.valueOf(10D));

        DiscountCalculationStrategy discountCalculationStrategy2 = mock(DiscountCalculationStrategy.class);
        assertThat(discountCalculationStrategy2).isNotNull();
        when(discountCalculationStrategy2.calculateDiscountAmount(shoppingCart)).thenReturn(Amount.valueOf(15D));

        DiscountValidationStrategy discountValidationStrategy1 = mock(DiscountValidationStrategy.class);
        assertThat(discountValidationStrategy1).isNotNull();
        doAnswer(invocationOnMock -> shoppingCart.getTotalQuantityOfProductsInCart().isGreaterThan(Quantity.valueOf(5))
                || shoppingCart.getTotalQuantityOfProductsInCart().equals(Quantity.valueOf(5)))
                .when(discountValidationStrategy1).isValid(shoppingCart);

        DiscountValidationStrategy discountValidationStrategy2 = mock(DiscountValidationStrategy.class);
        assertThat(discountValidationStrategy2).isNotNull();
        doAnswer(invocationOnMock -> shoppingCart.getTotalQuantityOfProductsInCart().isGreaterThan(Quantity.valueOf(10))
                || shoppingCart.getTotalQuantityOfProductsInCart().equals(Quantity.valueOf(10)))
                .when(discountValidationStrategy2).isValid(shoppingCart);

        Discount discount1 = mock(Discount.class, withSettings()
                .useConstructor(quantityBasedDiscount, discountValidationStrategy1, discountCalculationStrategy1)
                .defaultAnswer(CALLS_REAL_METHODS));
        assertThat(discount1).isNotNull();
        Discount discount2 = mock(Discount.class, withSettings()
                .useConstructor(quantityBasedDiscount, discountValidationStrategy2, discountCalculationStrategy2)
                .defaultAnswer(CALLS_REAL_METHODS));
        assertThat(discount2).isNotNull();

        when(discount1.isGreaterThan(null)).thenReturn(true);
        when(discount2.isGreaterThan(null)).thenReturn(true);
        when(discount1.isGreaterThan(discount2)).thenReturn(false);
        when(discount2.isGreaterThan(discount1)).thenReturn(true);

        DiscountProvidedEvent event1 = mock(DiscountProvidedEvent.class);
        assertThat(event1).isNotNull();
        when(event1.getDiscount()).thenReturn(discount1);

        DiscountProvidedEvent event2 = mock(DiscountProvidedEvent.class);
        assertThat(event2).isNotNull();
        when(event2.getDiscount()).thenReturn(discount2);

        //when
        shoppingCart.discountProvided(event1);
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

        //Mock discount: If >= 5 items in cart, then apply discount amount of 10
        DiscountName discountName = DiscountName.valueOf("MOCK");
        assertThat(discountName).isNotNull();

        DiscountCalculationStrategy discountCalculationStrategy = mock(DiscountCalculationStrategy.class);
        assertThat(discountCalculationStrategy).isNotNull();
        when(discountCalculationStrategy.calculateDiscountAmount(shoppingCart)).thenReturn(Amount.valueOf(10D));

        DiscountValidationStrategy discountValidationStrategy = mock(DiscountValidationStrategy.class);
        assertThat(discountValidationStrategy).isNotNull();
        assertThat(shoppingCart.getTotalQuantityOfProductsInCart()).isGreaterThanOrEqualTo(Quantity.valueOf(5));
        when(discountValidationStrategy.isValid(shoppingCart)).thenReturn(true);

        Discount discount = mock(Discount.class, withSettings()
                .useConstructor(discountName, discountValidationStrategy, discountCalculationStrategy)
                .defaultAnswer(CALLS_REAL_METHODS));
        assertThat(discount).isNotNull();
        when(discount.isGreaterThan(null)).thenReturn(true);

        DiscountProvidedEvent event = mock(DiscountProvidedEvent.class);
        assertThat(event).isNotNull();
        when(event.getDiscount()).thenReturn(discount);

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
        assertThat(outContent).asString().contains(discount.getDiscountName() + ": " + discount.getDiscountAmount());
        assertThat(outContent).asString().contains("Total Discount Amount: " + shoppingCart.getTotalDiscount());
        assertThat(outContent).asString().contains("Total Cart Amount After Discount(s): " + shoppingCart.getCartAmount());
        assertThat(outContent).asString().contains("Delivery Cost: " + shoppingCart.getDeliveryCost());
        assertThat(outContent).asString().contains("Total Amount(Cart Amount After Discount(s) + Delivery Cost): " + shoppingCart.getTotalAmount());


        System.setOut(originalOut);
    }
}
