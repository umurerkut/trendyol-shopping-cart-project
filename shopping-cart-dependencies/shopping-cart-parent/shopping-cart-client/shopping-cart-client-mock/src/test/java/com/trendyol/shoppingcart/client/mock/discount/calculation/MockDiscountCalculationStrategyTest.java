package com.trendyol.shoppingcart.client.mock.discount.calculation;

import com.trendyol.shoppingcart.core.domain.ShoppingCart;
import com.trendyol.shoppingcart.core.domain.value.Amount;
import com.trendyol.shoppingcart.core.exception.InvalidValueException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class MockDiscountCalculationStrategyTest {

    @Test
    public void givenDiscountAmount_whenDiscountAmountIsNull_thenThrowInvalidValueException() {
        //given
        Amount discountAmount = null;
        assertThat(discountAmount).isNull();

        //when
        Throwable throwable = catchThrowable(() -> {
            MockDiscountCalculationStrategy calculationStrategy = new MockDiscountCalculationStrategy(discountAmount);
        });

        //then
        assertThat(throwable)
                .isNotNull()
                .isInstanceOf(InvalidValueException.class)
                .hasMessage("Amount of discount to be applied can not be null!");
    }

    @Test
    public void givenDiscountAmount_whenDiscountAmountIsZero_thenThrowInvalidValueException() {
        //given
        Amount discountAmount = Amount.ofZero();
        assertThat(discountAmount).isNotNull();

        //when
        Throwable throwable = catchThrowable(() -> {
            MockDiscountCalculationStrategy calculationStrategy = new MockDiscountCalculationStrategy(discountAmount);
        });

        //then
        assertThat(throwable)
                .isNotNull()
                .isInstanceOf(InvalidValueException.class)
                .hasMessage("Amount of discount to be applied can not be zero!");
    }

    @Test
    public void givenValidDiscountAmount_whenCreateMockDiscountCalculationStrategy_thenReturnMockDiscountCalculationStrategy() {
        //given
        Amount discountAmount = Amount.valueOf(10D);
        assertThat(discountAmount).isNotNull();

        //when
        MockDiscountCalculationStrategy calculationStrategy = new MockDiscountCalculationStrategy(discountAmount);

        //then
        assertThat(calculationStrategy).isNotNull();
        assertThat(calculationStrategy.getDiscountAmount()).isEqualTo(discountAmount);
    }

    @Test
    public void givenDiscountAmount_whenDiscountAmountIsNegative_thenThrowInvalidValueException() {
        //given
        Amount discountAmount = Amount.valueOf(-10D);
        assertThat(discountAmount).isNotNull();

        //when
        Throwable throwable = catchThrowable(() -> {
            MockDiscountCalculationStrategy calculationStrategy = new MockDiscountCalculationStrategy(discountAmount);
        });

        //then
        assertThat(throwable)
                .isNotNull()
                .isInstanceOf(InvalidValueException.class)
                .hasMessage("Amount of discount to be applied can not be negative!");
    }

    @Test
    public void givenMockDiscountCalculationStrategy_whenCalculateDiscountAmount_thenReturnCalculatedDiscountAmount() {
        //given
        Amount discountAmount = Amount.valueOf(10D);
        assertThat(discountAmount).isNotNull();
        MockDiscountCalculationStrategy calculationStrategy = new MockDiscountCalculationStrategy(discountAmount);

        //when
        ShoppingCart shoppingCart = mock(ShoppingCart.class);
        Amount calculatedDiscountAmount = calculationStrategy.calculateDiscountAmount(shoppingCart);

        verifyNoMoreInteractions(shoppingCart);

        //then
        assertThat(calculatedDiscountAmount).isEqualTo(discountAmount).isEqualTo(Amount.valueOf(10D));
    }
}
