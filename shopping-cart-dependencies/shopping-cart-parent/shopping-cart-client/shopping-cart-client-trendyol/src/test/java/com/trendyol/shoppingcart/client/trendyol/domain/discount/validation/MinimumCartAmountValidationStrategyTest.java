package com.trendyol.shoppingcart.client.trendyol.domain.discount.validation;

import com.trendyol.shoppingcart.core.domain.ShoppingCart;
import com.trendyol.shoppingcart.core.domain.value.Amount;
import com.trendyol.shoppingcart.core.exception.InvalidValueException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MinimumCartAmountValidationStrategyTest {

    @Test
    public void givenMinimumAmountOfProducts_whenMinimumCartAmountIsNull_thenThrowInvalidValueException() {
        //given
        Amount minimumCartAmount = null;
        assertThat(minimumCartAmount).isNull();

        //when
        Throwable throwable = catchThrowable(() -> {
            MinimumCartAmountValidationStrategy validationStrategy = new MinimumCartAmountValidationStrategy(minimumCartAmount);
        });

        //then
        assertThat(throwable)
                .isNotNull()
                .isInstanceOf(InvalidValueException.class)
                .hasMessage("Minimum cart amount can not be null!");
    }

    @Test
    public void givenMinimumAmountOfProducts_whenMinimumCartAmountIsNegative_thenThrowInvalidValueException() {
        //given
        Amount minimumCartAmount = Amount.valueOf(-10D);
        assertThat(minimumCartAmount).isNotNull();

        //when
        Throwable throwable = catchThrowable(() -> {
            MinimumCartAmountValidationStrategy validationStrategy = new MinimumCartAmountValidationStrategy(minimumCartAmount);
        });

        //then
        assertThat(throwable)
                .isNotNull()
                .isInstanceOf(InvalidValueException.class)
                .hasMessage("Minimum cart amount can not be negative!");
    }

    @Test
    public void givenValidMinimumAmountOfProducts_whenCreateMinimumCartAmountValidationStrategy_thenReturnMinimumCartAmountValidationStrategy() {
        //given
        Amount minimumCartAmount = Amount.valueOf(10D);
        assertThat(minimumCartAmount).isNotNull();

        //when
        MinimumCartAmountValidationStrategy validationStrategy = new MinimumCartAmountValidationStrategy(minimumCartAmount);

        //then
        assertThat(validationStrategy).isNotNull();
        assertThat(validationStrategy.getMinimumCartAmount()).isEqualTo(minimumCartAmount);
    }

    @Test
    public void givenMinimumCartAmountValidationStrategy_whenIsValid_thenReturnTrue() {
        //given
        Amount minimumCartAmount = Amount.valueOf(10D);
        assertThat(minimumCartAmount).isNotNull();
        MinimumCartAmountValidationStrategy validationStrategy = new MinimumCartAmountValidationStrategy(minimumCartAmount);

        //when
        ShoppingCart shoppingCart = mock(ShoppingCart.class);
        when(shoppingCart.getCartAmount()).thenReturn(Amount.valueOf(20D));
        boolean result = validationStrategy.isValid(shoppingCart);

        //then
        assertThat(result).isTrue();
    }

    @Test
    public void givenMinimumCartAmountValidationStrategy_whenIsNotValid_thenReturnFalse() {
        //given
        Amount minimumCartAmount = Amount.valueOf(10D);
        assertThat(minimumCartAmount).isNotNull();
        MinimumCartAmountValidationStrategy validationStrategy = new MinimumCartAmountValidationStrategy(minimumCartAmount);

        //when
        ShoppingCart shoppingCart = mock(ShoppingCart.class);
        when(shoppingCart.getCartAmount()).thenReturn(Amount.valueOf(5D));
        boolean result = validationStrategy.isValid(shoppingCart);

        //then
        assertThat(result).isFalse();
    }

}
