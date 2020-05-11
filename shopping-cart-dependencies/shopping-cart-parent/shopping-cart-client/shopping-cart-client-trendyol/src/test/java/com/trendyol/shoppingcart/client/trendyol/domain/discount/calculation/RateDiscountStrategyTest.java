package com.trendyol.shoppingcart.client.trendyol.domain.discount.calculation;

import com.trendyol.shoppingcart.client.trendyol.domain.value.Rate;
import com.trendyol.shoppingcart.core.domain.ShoppingCart;
import com.trendyol.shoppingcart.core.domain.value.Amount;
import com.trendyol.shoppingcart.core.exception.InvalidValueException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RateDiscountStrategyTest {

    @Test
    public void givenDiscountRate_whenDiscountRateIsNull_thenThrowInvalidValueException() {
        //given
        Rate discountRate = null;
        assertThat(discountRate).isNull();

        //when
        Throwable throwable = catchThrowable(() -> {
            RateDiscountStrategy calculationStrategy = new RateDiscountStrategy(discountRate);
        });

        //then
        assertThat(throwable)
                .isNotNull()
                .isInstanceOf(InvalidValueException.class)
                .hasMessage("Rate of discount to be applied can not be null!");
    }

    @Test
    public void givenValidDiscountRate_whenCreateRateDiscountStrategy_thenReturnRateDiscountStrategy() {
        //given
        Rate discountRate = Rate.valueOf(10D);
        assertThat(discountRate).isNotNull();

        //when
        RateDiscountStrategy calculationStrategy = new RateDiscountStrategy(discountRate);

        //then
        assertThat(calculationStrategy).isNotNull();
        assertThat(calculationStrategy.getDiscountRate()).isEqualTo(discountRate);
    }

    @Test
    public void givenRateDiscountStrategy_whenCalculateDiscountRate_thenReturnCalculatedDiscountRate() {
        //given
        Rate discountRate = Rate.valueOf(10D);
        assertThat(discountRate).isNotNull();
        RateDiscountStrategy calculationStrategy = new RateDiscountStrategy(discountRate);

        //when
        ShoppingCart shoppingCart = mock(ShoppingCart.class);
        when(shoppingCart.getCartAmount()).thenReturn(Amount.valueOf(100D));
        Amount calculatedDiscountAmount = calculationStrategy.calculateDiscountAmount(shoppingCart);

        //then
        assertThat(calculatedDiscountAmount)
                .isEqualTo(shoppingCart.getCartAmount().multiply(discountRate.doubleValue()).divide(100D))
                .isEqualTo(Amount.valueOf(10D));
    }
}
