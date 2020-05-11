package com.trendyol.shoppingcart.core.domain.discount;

import com.trendyol.shoppingcart.core.domain.discount.calculation.DiscountCalculationStrategy;
import com.trendyol.shoppingcart.core.domain.discount.validation.DiscountValidationStrategy;
import com.trendyol.shoppingcart.core.domain.value.DiscountName;
import com.trendyol.shoppingcart.core.exception.InvalidValueException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.withSettings;

public class DiscountTest {

    @Test
    public void givenConstructorParameters_whenAllConstructorParameters_thenReturnInstance() {
        //given
        DiscountName discountName = DiscountName.valueOf("NAME");
        DiscountValidationStrategy discountValidationStrategy = mock(DiscountValidationStrategy.class);
        DiscountCalculationStrategy discountCalculationStrategy = mock(DiscountCalculationStrategy.class);

        //when
        Discount discount = mock(Discount.class, withSettings()
                .useConstructor(discountName, discountValidationStrategy, discountCalculationStrategy)
                .defaultAnswer(Mockito.CALLS_REAL_METHODS));

        //then
        assertThat(discount).isNotNull();
    }

    @Test
    public void givenConstructorParameters_whenDiscountNameNull_throwInvalidValueException() {
        //given
        DiscountName discountName = null;
        DiscountValidationStrategy discountValidationStrategy = mock(DiscountValidationStrategy.class);
        DiscountCalculationStrategy discountCalculationStrategy = mock(DiscountCalculationStrategy.class);

        //when
        Throwable throwable = catchThrowable(() -> {
            Discount discount = mock(Discount.class, withSettings()
                    .useConstructor(discountName, discountValidationStrategy, discountCalculationStrategy)
                    .defaultAnswer(Mockito.CALLS_REAL_METHODS));
        });

        //then
        Throwable root = ExceptionUtils.getRootCause(throwable);
        assertThat(root)
                .isNotNull()
                .isInstanceOf(InvalidValueException.class)
                .hasMessage("Discount name an not be null!");
    }

    @Test
    public void givenConstructorParameters_whenValidationStrategyNull_throwInvalidValueException() {
        //given
        DiscountName discountName = DiscountName.valueOf("DISCOUNT");
        DiscountValidationStrategy discountValidationStrategy = null;
        DiscountCalculationStrategy discountCalculationStrategy = mock(DiscountCalculationStrategy.class);

        //when
        Throwable throwable = catchThrowable(() -> {
            Discount discount = mock(Discount.class, withSettings()
                    .useConstructor(discountName, discountValidationStrategy, discountCalculationStrategy)
                    .defaultAnswer(Mockito.CALLS_REAL_METHODS));
        });

        //then
        Throwable root = ExceptionUtils.getRootCause(throwable);
        assertThat(root)
                .isNotNull()
                .isInstanceOf(InvalidValueException.class)
                .hasMessage("Validation strategy can not be null!");
    }

    @Test
    public void givenConstructorParameters_whenCalculationStrategyNull_throwInvalidValueException() {
        //given
        DiscountName discountName = DiscountName.valueOf("DISCOUNT");
        DiscountValidationStrategy discountValidationStrategy = mock(DiscountValidationStrategy.class);
        DiscountCalculationStrategy discountCalculationStrategy = null;

        //when
        Throwable throwable = catchThrowable(() -> {
            Discount discount = mock(Discount.class, withSettings()
                    .useConstructor(discountName, discountValidationStrategy, discountCalculationStrategy)
                    .defaultAnswer(Mockito.CALLS_REAL_METHODS));
        });

        //then
        Throwable root = ExceptionUtils.getRootCause(throwable);
        assertThat(root)
                .isNotNull()
                .isInstanceOf(InvalidValueException.class)
                .hasMessage("Calculation strategy can not be null!");
    }
}
