package com.trendyol.shoppingcart.core.discountprovider;

import com.trendyol.shoppingcart.core.discountprovider.listener.DiscountProvidedEvent;
import com.trendyol.shoppingcart.core.domain.ShoppingCart;
import com.trendyol.shoppingcart.core.domain.discount.ProductQuantityBasedMockDiscount;
import com.trendyol.shoppingcart.core.exception.InvalidValueException;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import java.time.Duration;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.*;

public class MockDiscountProviderTest {

    @Test
    public void givenDiscount_whenDiscountIsNull_thenThrowInvalidValueException() {
        //given
        ProductQuantityBasedMockDiscount discount = null;

        //when
        assertThat(discount).isNull();
        Throwable throwable = catchThrowable(() -> {
            MockDiscountProvider mockDiscountProvider = new MockDiscountProvider(discount);
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
        MockDiscountProvider mockDiscountProvider = new MockDiscountProvider(discount);

        //then
        assertThat(mockDiscountProvider).isNotNull();
        assertThat(mockDiscountProvider.getDiscount()).isEqualTo(discount);
        assertThat(mockDiscountProvider.getExpireTime()).isNull();
    }

    @Test
    public void givenValidDiscountAndExpireTime_whenCreateMockDiscountProvider_thenReturnMockDiscountProvider() {
        //given
        ProductQuantityBasedMockDiscount discount = mock(ProductQuantityBasedMockDiscount.class);
        assertThat(discount).isNotNull();
        Instant expireTime = Instant.now().plus(Duration.ofDays(10));
        assertThat(expireTime).isNotNull();

        //when
        MockDiscountProvider mockDiscountProvider = new MockDiscountProvider(discount, expireTime);

        //then
        assertThat(mockDiscountProvider).isNotNull();
        assertThat(mockDiscountProvider.getDiscount()).isEqualTo(discount);
        assertThat(mockDiscountProvider.getExpireTime()).isNotNull().isEqualTo(expireTime);
    }

    @Test
    public void givenMockDiscountProvider_whenExpireTimeNull_thenIsValid() {
        //given
        ProductQuantityBasedMockDiscount discount = mock(ProductQuantityBasedMockDiscount.class);
        assertThat(discount).isNotNull();
        MockDiscountProvider mockDiscountProvider = new MockDiscountProvider(discount);
        assertThat(mockDiscountProvider.getExpireTime()).isNull();

        //when
        Boolean result = mockDiscountProvider.isValid();

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
        MockDiscountProvider mockDiscountProvider = new MockDiscountProvider(discount, expireTime);
        assertThat(mockDiscountProvider.getExpireTime()).isNotNull();

        //when
        Boolean result = mockDiscountProvider.isValid();

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
        MockDiscountProvider mockDiscountProvider = new MockDiscountProvider(discount, expireTime);
        assertThat(mockDiscountProvider.getExpireTime()).isNotNull();

        //when
        Boolean result = mockDiscountProvider.isValid();

        //then
        assertThat(result).isFalse();
    }

    @Test
    public void givenMockDiscountProvider_whenRegisterShoppingCartAsListener_thenAddShoppingCartToListenerList() {
        //given
        ProductQuantityBasedMockDiscount discount = mock(ProductQuantityBasedMockDiscount.class);
        assertThat(discount).isNotNull();
        MockDiscountProvider mockDiscountProvider = new MockDiscountProvider(discount);
        assertThat(mockDiscountProvider.getExpireTime()).isNull();

        //when
        ShoppingCart shoppingCart = mock(ShoppingCart.class);
        mockDiscountProvider.registerDiscountProviderEventListener(shoppingCart);

        //then
        assertThat(mockDiscountProvider.getDiscountProviderEventListenerList()).hasSize(1);
        assertThat(mockDiscountProvider.getDiscountProviderEventListenerList()).contains(shoppingCart);
    }

    @Test
    public void givenMockDiscountProvider_whenUnregisterShoppingCartAsListener_thenRemoveShoppingCartToListenerList() {
        //given
        ProductQuantityBasedMockDiscount discount = mock(ProductQuantityBasedMockDiscount.class);
        assertThat(discount).isNotNull();
        MockDiscountProvider mockDiscountProvider = new MockDiscountProvider(discount);
        assertThat(mockDiscountProvider.getExpireTime()).isNull();

        ShoppingCart shoppingCart = mock(ShoppingCart.class);
        mockDiscountProvider.registerDiscountProviderEventListener(shoppingCart);

        //when
        mockDiscountProvider.unregisterDiscountProviderEventListener(shoppingCart);

        //then
        assertThat(mockDiscountProvider.getDiscountProviderEventListenerList()).hasSize(0);
        assertThat(mockDiscountProvider.getDiscountProviderEventListenerList()).doesNotContain(shoppingCart);
    }

    @Test
    public void givenMockDiscountProvider_whenNotifyListeners_thenNotifyShoppingCartAboutDiscountProvided() {
        //given
        ProductQuantityBasedMockDiscount discount = mock(ProductQuantityBasedMockDiscount.class);
        assertThat(discount).isNotNull();
        MockDiscountProvider mockDiscountProvider = new MockDiscountProvider(discount);
        assertThat(mockDiscountProvider.getExpireTime()).isNull();

        ShoppingCart shoppingCart = mock(ShoppingCart.class);
        mockDiscountProvider.registerDiscountProviderEventListener(shoppingCart);

        //when
        DiscountProvidedEvent event = mock(DiscountProvidedEvent.class);
        mockDiscountProvider.notifyDiscountProviderEventListeners(event);

        //then
        assertThat(mockDiscountProvider.getDiscountProviderEventListenerList()).hasSize(1);
        assertThat(mockDiscountProvider.getDiscountProviderEventListenerList()).contains(shoppingCart);
        verify(shoppingCart, times(1)).discountProvided(ArgumentMatchers.eq(event));
    }

    @Test
    public void givenMockDiscountProvider_whenIsValid_thenProvideDiscount() {
        //given
        ProductQuantityBasedMockDiscount discount = mock(ProductQuantityBasedMockDiscount.class);
        assertThat(discount).isNotNull();
        MockDiscountProvider mockDiscountProvider = new MockDiscountProvider(discount);
        assertThat(mockDiscountProvider.getExpireTime()).isNull();

        ShoppingCart shoppingCart = mock(ShoppingCart.class);
        mockDiscountProvider.registerDiscountProviderEventListener(shoppingCart);

        //when
        assertThat(mockDiscountProvider.isValid()).isTrue();
        mockDiscountProvider.provideDiscount();

        //then
        assertThat(mockDiscountProvider.getDiscountProviderEventListenerList()).hasSize(1);
        assertThat(mockDiscountProvider.getDiscountProviderEventListenerList()).contains(shoppingCart);
        verify(shoppingCart, times(1)).discountProvided(ArgumentMatchers.any(DiscountProvidedEvent.class));
    }

    @Test
    public void givenMockDiscountProvider_whenIsNotValid_thenDoNotProvideDiscount() {
        //given
        ProductQuantityBasedMockDiscount discount = mock(ProductQuantityBasedMockDiscount.class);
        assertThat(discount).isNotNull();
        Instant expireTime = Instant.now().minus(Duration.ofDays(10));
        assertThat(expireTime).isNotNull();
        MockDiscountProvider mockDiscountProvider = new MockDiscountProvider(discount, expireTime);
        assertThat(mockDiscountProvider.getExpireTime()).isNotNull();

        ShoppingCart shoppingCart = mock(ShoppingCart.class);
        mockDiscountProvider.registerDiscountProviderEventListener(shoppingCart);

        //when
        assertThat(mockDiscountProvider.isValid()).isFalse();
        mockDiscountProvider.provideDiscount();

        //then
        assertThat(mockDiscountProvider.getDiscountProviderEventListenerList()).hasSize(1);
        assertThat(mockDiscountProvider.getDiscountProviderEventListenerList()).contains(shoppingCart);
        verify(shoppingCart, never()).discountProvided(ArgumentMatchers.any(DiscountProvidedEvent.class));
    }

}
