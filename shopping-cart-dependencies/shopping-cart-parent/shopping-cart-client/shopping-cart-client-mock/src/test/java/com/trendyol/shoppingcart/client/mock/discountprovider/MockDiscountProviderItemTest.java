package com.trendyol.shoppingcart.client.mock.discountprovider;

import com.trendyol.shoppingcart.client.mock.discount.ProductQuantityBasedMockDiscount;
import com.trendyol.shoppingcart.core.discountprovider.listener.DiscountProvidedEvent;
import com.trendyol.shoppingcart.core.domain.ShoppingCart;
import com.trendyol.shoppingcart.core.exception.InvalidValueException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import java.time.Duration;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.*;

public class MockDiscountProviderItemTest {

    @Test
    public void givenDiscount_whenDiscountIsNull_thenThrowInvalidValueException() {
        //given
        ProductQuantityBasedMockDiscount discount = null;

        //when
        assertThat(discount).isNull();
        Throwable throwable = catchThrowable(() -> {
            MockDiscountProviderItem mockDiscountProviderItem = new MockDiscountProviderItem(discount);
        });

        //then
        assertThat(throwable)
                .isNotNull()
                .isInstanceOf(InvalidValueException.class)
                .hasMessage("Discount can not be null!");
    }

    @Test
    public void givenValidDiscount_whenCreateMockDiscountProvider_thenReturnMockDiscountProvider() {
        //given
        ProductQuantityBasedMockDiscount discount = mock(ProductQuantityBasedMockDiscount.class);
        assertThat(discount).isNotNull();

        //when
        MockDiscountProviderItem mockDiscountProviderItem = new MockDiscountProviderItem(discount);

        //then
        Assertions.assertThat(mockDiscountProviderItem).isNotNull();
        Assertions.assertThat(mockDiscountProviderItem.getDiscount()).isEqualTo(discount);
        Assertions.assertThat(mockDiscountProviderItem.getExpireTime()).isNull();
    }

    @Test
    public void givenValidDiscountAndExpireTime_whenCreateMockDiscountProvider_thenReturnMockDiscountProvider() {
        //given
        ProductQuantityBasedMockDiscount discount = mock(ProductQuantityBasedMockDiscount.class);
        assertThat(discount).isNotNull();
        Instant expireTime = Instant.now().plus(Duration.ofDays(10));
        assertThat(expireTime).isNotNull();

        //when
        MockDiscountProviderItem mockDiscountProviderItem = new MockDiscountProviderItem(discount, expireTime);

        //then
        Assertions.assertThat(mockDiscountProviderItem).isNotNull();
        Assertions.assertThat(mockDiscountProviderItem.getDiscount()).isEqualTo(discount);
        Assertions.assertThat(mockDiscountProviderItem.getExpireTime()).isNotNull().isEqualTo(expireTime);
    }

    @Test
    public void givenMockDiscountProvider_whenExpireTimeNull_thenIsValid() {
        //given
        ProductQuantityBasedMockDiscount discount = mock(ProductQuantityBasedMockDiscount.class);
        assertThat(discount).isNotNull();
        MockDiscountProviderItem mockDiscountProviderItem = new MockDiscountProviderItem(discount);
        Assertions.assertThat(mockDiscountProviderItem.getExpireTime()).isNull();

        //when
        boolean result = mockDiscountProviderItem.isValid();

        //then
        assertThat(result).isTrue();
    }

    @Test
    public void givenMockDiscountProvider_whenExpireTimeAfterNow_thenIsValid() {
        //given
        ProductQuantityBasedMockDiscount discount = mock(ProductQuantityBasedMockDiscount.class);
        assertThat(discount).isNotNull();
        Instant expireTime = Instant.now().plus(Duration.ofDays(10));
        assertThat(expireTime).isNotNull();
        MockDiscountProviderItem mockDiscountProviderItem = new MockDiscountProviderItem(discount, expireTime);
        Assertions.assertThat(mockDiscountProviderItem.getExpireTime()).isNotNull();

        //when
        boolean result = mockDiscountProviderItem.isValid();

        //then
        assertThat(result).isTrue();
    }

    @Test
    public void givenMockDiscountProvider_whenExpireTimeBeforeNow_thenIsNotValid() {
        //given
        ProductQuantityBasedMockDiscount discount = mock(ProductQuantityBasedMockDiscount.class);
        assertThat(discount).isNotNull();
        Instant expireTime = Instant.now().minus(Duration.ofDays(10));
        assertThat(expireTime).isNotNull();
        MockDiscountProviderItem mockDiscountProviderItem = new MockDiscountProviderItem(discount, expireTime);
        Assertions.assertThat(mockDiscountProviderItem.getExpireTime()).isNotNull();

        //when
        boolean result = mockDiscountProviderItem.isValid();

        //then
        assertThat(result).isFalse();
    }

    @Test
    public void givenMockDiscountProvider_whenRegisterShoppingCartAsListener_thenAddShoppingCartToListenerList() {
        //given
        ProductQuantityBasedMockDiscount discount = mock(ProductQuantityBasedMockDiscount.class);
        assertThat(discount).isNotNull();
        MockDiscountProviderItem mockDiscountProviderItem = new MockDiscountProviderItem(discount);
        Assertions.assertThat(mockDiscountProviderItem.getExpireTime()).isNull();

        //when
        ShoppingCart shoppingCart = mock(ShoppingCart.class);
        mockDiscountProviderItem.registerDiscountProviderEventListener(shoppingCart);

        //then
        Assertions.assertThat(mockDiscountProviderItem.getDiscountProviderEventListenerList()).hasSize(1);
        Assertions.assertThat(mockDiscountProviderItem.getDiscountProviderEventListenerList()).contains(shoppingCart);
    }

    @Test
    public void givenMockDiscountProvider_whenUnregisterShoppingCartAsListener_thenRemoveShoppingCartToListenerList() {
        //given
        ProductQuantityBasedMockDiscount discount = mock(ProductQuantityBasedMockDiscount.class);
        assertThat(discount).isNotNull();
        MockDiscountProviderItem mockDiscountProviderItem = new MockDiscountProviderItem(discount);
        Assertions.assertThat(mockDiscountProviderItem.getExpireTime()).isNull();

        ShoppingCart shoppingCart = mock(ShoppingCart.class);
        mockDiscountProviderItem.registerDiscountProviderEventListener(shoppingCart);

        //when
        mockDiscountProviderItem.unregisterDiscountProviderEventListener(shoppingCart);

        //then
        Assertions.assertThat(mockDiscountProviderItem.getDiscountProviderEventListenerList()).hasSize(0);
        Assertions.assertThat(mockDiscountProviderItem.getDiscountProviderEventListenerList()).doesNotContain(shoppingCart);
    }

    @Test
    public void givenMockDiscountProvider_whenNotifyListeners_thenNotifyShoppingCartAboutDiscountProvided() {
        //given
        ProductQuantityBasedMockDiscount discount = mock(ProductQuantityBasedMockDiscount.class);
        assertThat(discount).isNotNull();
        MockDiscountProviderItem mockDiscountProviderItem = new MockDiscountProviderItem(discount);
        Assertions.assertThat(mockDiscountProviderItem.getExpireTime()).isNull();

        ShoppingCart shoppingCart = mock(ShoppingCart.class);
        mockDiscountProviderItem.registerDiscountProviderEventListener(shoppingCart);

        //when
        DiscountProvidedEvent event = mock(DiscountProvidedEvent.class);
        mockDiscountProviderItem.notifyDiscountProviderEventListeners(event);

        //then
        Assertions.assertThat(mockDiscountProviderItem.getDiscountProviderEventListenerList()).hasSize(1);
        Assertions.assertThat(mockDiscountProviderItem.getDiscountProviderEventListenerList()).contains(shoppingCart);
        verify(shoppingCart, times(1)).discountProvided(ArgumentMatchers.eq(event));
    }

    @Test
    public void givenMockDiscountProvider_whenIsValid_thenProvideDiscount() {
        //given
        ProductQuantityBasedMockDiscount discount = mock(ProductQuantityBasedMockDiscount.class);
        assertThat(discount).isNotNull();
        MockDiscountProviderItem mockDiscountProviderItem = new MockDiscountProviderItem(discount);
        Assertions.assertThat(mockDiscountProviderItem.getExpireTime()).isNull();

        ShoppingCart shoppingCart = mock(ShoppingCart.class);
        mockDiscountProviderItem.registerDiscountProviderEventListener(shoppingCart);

        //when
        Assertions.assertThat(mockDiscountProviderItem.isValid()).isTrue();
        mockDiscountProviderItem.provideDiscount();

        //then
        Assertions.assertThat(mockDiscountProviderItem.getDiscountProviderEventListenerList()).hasSize(1);
        Assertions.assertThat(mockDiscountProviderItem.getDiscountProviderEventListenerList()).contains(shoppingCart);
        verify(shoppingCart, times(1)).discountProvided(ArgumentMatchers.any(DiscountProvidedEvent.class));
    }

    @Test
    public void givenMockDiscountProvider_whenIsNotValid_thenDoNotProvideDiscount() {
        //given
        ProductQuantityBasedMockDiscount discount = mock(ProductQuantityBasedMockDiscount.class);
        assertThat(discount).isNotNull();
        Instant expireTime = Instant.now().minus(Duration.ofDays(10));
        assertThat(expireTime).isNotNull();
        MockDiscountProviderItem mockDiscountProviderItem = new MockDiscountProviderItem(discount, expireTime);
        Assertions.assertThat(mockDiscountProviderItem.getExpireTime()).isNotNull();

        ShoppingCart shoppingCart = mock(ShoppingCart.class);
        mockDiscountProviderItem.registerDiscountProviderEventListener(shoppingCart);

        //when
        Assertions.assertThat(mockDiscountProviderItem.isValid()).isFalse();
        mockDiscountProviderItem.provideDiscount();

        //then
        Assertions.assertThat(mockDiscountProviderItem.getDiscountProviderEventListenerList()).hasSize(1);
        Assertions.assertThat(mockDiscountProviderItem.getDiscountProviderEventListenerList()).contains(shoppingCart);
        verify(shoppingCart, never()).discountProvided(ArgumentMatchers.any(DiscountProvidedEvent.class));
    }

}
