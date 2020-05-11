package com.trendyol.shoppingcart.client.trendyol.discountprovider.coupon;


import com.trendyol.shoppingcart.client.trendyol.domain.value.CouponCode;
import com.trendyol.shoppingcart.client.trendyol.domain.value.Rate;
import com.trendyol.shoppingcart.core.discountprovider.listener.DiscountProvidedEvent;
import com.trendyol.shoppingcart.core.domain.ShoppingCart;
import com.trendyol.shoppingcart.core.domain.value.Amount;
import com.trendyol.shoppingcart.core.exception.InvalidValueException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import java.time.Duration;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.*;

public class MinimumCartAmountBasedDiscountCouponTest {

    @Test
    public void givenDiscount_whenCouponCodeIsNull_thenThrowInvalidValueException() {
        //given
        CouponCode couponCode = null;
        assertThat(couponCode).isNull();
        Amount minimumCartAmount = Amount.valueOf(10D);
        assertThat(minimumCartAmount).isNotNull();
        Rate discountRate = Rate.valueOf(10D);
        assertThat(discountRate).isNotNull();

        //when
        Throwable throwable = catchThrowable(() -> {
            MinimumCartAmountBasedDiscountCoupon coupon = new MinimumCartAmountBasedDiscountCoupon(couponCode, minimumCartAmount, discountRate);
        });

        //then
        assertThat(throwable)
                .isNotNull()
                .isInstanceOf(InvalidValueException.class)
                .hasMessage("Coupon code can not be null!");
    }

    @Test
    public void givenDiscountMount_whenCreateMinimumCartAmountBasedDiscountCoupon_thenReturnMinimumCartAmountBasedAmountDiscountCoupon() {
        //given
        CouponCode couponCode = CouponCode.valueOf("COUPON");
        assertThat(couponCode).isNotNull();
        Amount minimumCartAmount = Amount.valueOf(10D);
        assertThat(minimumCartAmount).isNotNull();
        Amount discountAmount = Amount.valueOf(10D);
        assertThat(discountAmount).isNotNull();

        //when
        MinimumCartAmountBasedDiscountCoupon coupon = new MinimumCartAmountBasedDiscountCoupon(couponCode, minimumCartAmount, discountAmount);

        //then
        Assertions.assertThat(coupon).isNotNull();
        Assertions.assertThat(coupon.getDiscount()).isNotNull();
        Assertions.assertThat(coupon.getExpireTime()).isNull();
    }

    @Test
    public void givenDiscountRate_whenCreateMinimumCartAmountBasedDiscountCoupon_thenReturnMinimumCartAmountBasedRateDiscountCoupon() {
        //given
        CouponCode couponCode = CouponCode.valueOf("COUPON");
        assertThat(couponCode).isNotNull();
        Amount minimumCartAmount = Amount.valueOf(10D);
        assertThat(minimumCartAmount).isNotNull();
        Rate discountRate = Rate.valueOf(10D);
        assertThat(discountRate).isNotNull();

        //when
        MinimumCartAmountBasedDiscountCoupon coupon = new MinimumCartAmountBasedDiscountCoupon(couponCode, minimumCartAmount, discountRate);

        //then
        Assertions.assertThat(coupon).isNotNull();
        Assertions.assertThat(coupon.getDiscount()).isNotNull();
        Assertions.assertThat(coupon.getExpireTime()).isNull();
    }

    @Test
    public void givenValidDiscountAndExpireTime_whenCreateMinimumCartAmountBasedDiscountCoupon_thenReturnMinimumCartAmountBasedDiscountCoupon() {
        //given
        CouponCode couponCode = CouponCode.valueOf("COUPON");
        assertThat(couponCode).isNotNull();
        Amount minimumCartAmount = Amount.valueOf(10D);
        assertThat(minimumCartAmount).isNotNull();
        Amount discountAmount = Amount.valueOf(10D);
        assertThat(discountAmount).isNotNull();
        Instant expireTime = Instant.now();

        //when
        MinimumCartAmountBasedDiscountCoupon coupon = new MinimumCartAmountBasedDiscountCoupon(couponCode, minimumCartAmount, discountAmount, expireTime);

        //then
        Assertions.assertThat(coupon).isNotNull();
        Assertions.assertThat(coupon.getDiscount()).isNotNull();
        Assertions.assertThat(coupon.getExpireTime()).isNotNull().isEqualTo(expireTime);
    }

    @Test
    public void givenMinimumCartAmountBasedDiscountCoupon_whenExpireTimeNull_thenIsValid() {
        //given
        CouponCode couponCode = CouponCode.valueOf("COUPON");
        assertThat(couponCode).isNotNull();
        Amount minimumCartAmount = Amount.valueOf(10D);
        assertThat(minimumCartAmount).isNotNull();
        Amount discountAmount = Amount.valueOf(10D);
        assertThat(discountAmount).isNotNull();

        MinimumCartAmountBasedDiscountCoupon coupon = new MinimumCartAmountBasedDiscountCoupon(couponCode, minimumCartAmount, discountAmount);
        Assertions.assertThat(coupon.getExpireTime()).isNull();

        //when
        boolean result = coupon.isValid();

        //then
        assertThat(result).isTrue();
    }

    @Test
    public void givenMinimumCartAmountBasedDiscountCoupon_whenExpireTimeAfterNow_thenIsValid() {
        //given
        CouponCode couponCode = CouponCode.valueOf("COUPON");
        assertThat(couponCode).isNotNull();
        Amount minimumCartAmount = Amount.valueOf(10D);
        assertThat(minimumCartAmount).isNotNull();
        Amount discountAmount = Amount.valueOf(10D);
        assertThat(discountAmount).isNotNull();
        Instant expireTime = Instant.now().plus(Duration.ofDays(10));
        assertThat(expireTime).isNotNull();

        MinimumCartAmountBasedDiscountCoupon coupon = new MinimumCartAmountBasedDiscountCoupon(couponCode, minimumCartAmount, discountAmount, expireTime);
        Assertions.assertThat(coupon.getExpireTime()).isNotNull();

        //when
        boolean result = coupon.isValid();

        //then
        assertThat(result).isTrue();
    }

    @Test
    public void givenMinimumCartAmountBasedDiscountCoupon_whenExpireTimeBeforeNow_thenIsNotValid() {
        //given
        CouponCode couponCode = CouponCode.valueOf("COUPON");
        assertThat(couponCode).isNotNull();
        Amount minimumCartAmount = Amount.valueOf(10D);
        assertThat(minimumCartAmount).isNotNull();
        Amount discountAmount = Amount.valueOf(10D);
        assertThat(discountAmount).isNotNull();
        Instant expireTime = Instant.now().minus(Duration.ofDays(10));
        assertThat(expireTime).isNotNull();

        MinimumCartAmountBasedDiscountCoupon coupon = new MinimumCartAmountBasedDiscountCoupon(couponCode, minimumCartAmount, discountAmount, expireTime);
        Assertions.assertThat(coupon.getExpireTime()).isNotNull();

        //when
        boolean result = coupon.isValid();

        //then
        assertThat(result).isFalse();
    }

    @Test
    public void givenMinimumCartAmountBasedDiscountCoupon_whenRegisterShoppingCartAsListener_thenAddShoppingCartToListenerList() {
        //given
        CouponCode couponCode = CouponCode.valueOf("COUPON");
        assertThat(couponCode).isNotNull();
        Amount minimumCartAmount = Amount.valueOf(10D);
        assertThat(minimumCartAmount).isNotNull();
        Amount discountAmount = Amount.valueOf(10D);
        assertThat(discountAmount).isNotNull();

        MinimumCartAmountBasedDiscountCoupon coupon = new MinimumCartAmountBasedDiscountCoupon(couponCode, minimumCartAmount, discountAmount);
        Assertions.assertThat(coupon.getExpireTime()).isNull();

        //when
        ShoppingCart shoppingCart = mock(ShoppingCart.class);
        coupon.registerDiscountProviderEventListener(shoppingCart);

        //then
        Assertions.assertThat(coupon.getDiscountProviderEventListenerList()).hasSize(1);
        Assertions.assertThat(coupon.getDiscountProviderEventListenerList()).contains(shoppingCart);
    }

    @Test
    public void givenMinimumCartAmountBasedDiscountCoupon_whenUnregisterShoppingCartAsListener_thenRemoveShoppingCartToListenerList() {
        //given
        CouponCode couponCode = CouponCode.valueOf("COUPON");
        assertThat(couponCode).isNotNull();
        Amount minimumCartAmount = Amount.valueOf(10D);
        assertThat(minimumCartAmount).isNotNull();
        Amount discountAmount = Amount.valueOf(10D);
        assertThat(discountAmount).isNotNull();

        MinimumCartAmountBasedDiscountCoupon coupon = new MinimumCartAmountBasedDiscountCoupon(couponCode, minimumCartAmount, discountAmount);
        Assertions.assertThat(coupon.getExpireTime()).isNull();

        ShoppingCart shoppingCart = mock(ShoppingCart.class);
        coupon.registerDiscountProviderEventListener(shoppingCart);

        //when
        coupon.unregisterDiscountProviderEventListener(shoppingCart);

        //then
        Assertions.assertThat(coupon.getDiscountProviderEventListenerList()).hasSize(0);
        Assertions.assertThat(coupon.getDiscountProviderEventListenerList()).doesNotContain(shoppingCart);
    }

    @Test
    public void givenMinimumCartAmountBasedDiscountCoupon_whenNotifyListeners_thenNotifyShoppingCartAboutDiscountProvided() {
        //given
        CouponCode couponCode = CouponCode.valueOf("COUPON");
        assertThat(couponCode).isNotNull();
        Amount minimumCartAmount = Amount.valueOf(10D);
        assertThat(minimumCartAmount).isNotNull();
        Amount discountAmount = Amount.valueOf(10D);
        assertThat(discountAmount).isNotNull();

        MinimumCartAmountBasedDiscountCoupon coupon = new MinimumCartAmountBasedDiscountCoupon(couponCode, minimumCartAmount, discountAmount);
        Assertions.assertThat(coupon.getExpireTime()).isNull();

        ShoppingCart shoppingCart = mock(ShoppingCart.class);
        coupon.registerDiscountProviderEventListener(shoppingCart);

        //when
        DiscountProvidedEvent event = mock(DiscountProvidedEvent.class);
        coupon.notifyDiscountProviderEventListeners(event);

        //then
        Assertions.assertThat(coupon.getDiscountProviderEventListenerList()).hasSize(1);
        Assertions.assertThat(coupon.getDiscountProviderEventListenerList()).contains(shoppingCart);
        verify(shoppingCart, times(1)).discountProvided(ArgumentMatchers.eq(event));
    }

    @Test
    public void givenMinimumCartAmountBasedDiscountCoupon_whenIsValid_thenProvideDiscount() {
        //given
        CouponCode couponCode = CouponCode.valueOf("COUPON");
        assertThat(couponCode).isNotNull();
        Amount minimumCartAmount = Amount.valueOf(10D);
        assertThat(minimumCartAmount).isNotNull();
        Amount discountAmount = Amount.valueOf(10D);
        assertThat(discountAmount).isNotNull();

        MinimumCartAmountBasedDiscountCoupon coupon = new MinimumCartAmountBasedDiscountCoupon(couponCode, minimumCartAmount, discountAmount);
        Assertions.assertThat(coupon.getExpireTime()).isNull();

        ShoppingCart shoppingCart = mock(ShoppingCart.class);
        coupon.registerDiscountProviderEventListener(shoppingCart);

        //when
        Assertions.assertThat(coupon.isValid()).isTrue();
        coupon.provideDiscount();

        //then
        Assertions.assertThat(coupon.getDiscountProviderEventListenerList()).hasSize(1);
        Assertions.assertThat(coupon.getDiscountProviderEventListenerList()).contains(shoppingCart);
        verify(shoppingCart, times(1)).discountProvided(ArgumentMatchers.any(DiscountProvidedEvent.class));
    }

    @Test
    public void givenMinimumCartAmountBasedDiscountCoupon_whenIsNotValid_thenDoNotProvideDiscount() {
        //given
        CouponCode couponCode = CouponCode.valueOf("COUPON");
        assertThat(couponCode).isNotNull();
        Amount minimumCartAmount = Amount.valueOf(10D);
        assertThat(minimumCartAmount).isNotNull();
        Amount discountAmount = Amount.valueOf(10D);
        assertThat(discountAmount).isNotNull();

        Instant expireTime = Instant.now().minus(Duration.ofDays(10));
        assertThat(expireTime).isNotNull();
        MinimumCartAmountBasedDiscountCoupon coupon = new MinimumCartAmountBasedDiscountCoupon(couponCode, minimumCartAmount, discountAmount, expireTime);
        Assertions.assertThat(coupon.getExpireTime()).isNotNull();

        ShoppingCart shoppingCart = mock(ShoppingCart.class);
        coupon.registerDiscountProviderEventListener(shoppingCart);

        //when
        Assertions.assertThat(coupon.isValid()).isFalse();
        coupon.provideDiscount();

        //then
        Assertions.assertThat(coupon.getDiscountProviderEventListenerList()).hasSize(1);
        Assertions.assertThat(coupon.getDiscountProviderEventListenerList()).contains(shoppingCart);
        verify(shoppingCart, never()).discountProvided(ArgumentMatchers.any(DiscountProvidedEvent.class));
    }

}
