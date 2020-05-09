package com.trendyol.shoppingcart.core.domain.value;

import com.trendyol.shoppingcart.core.exception.InvalidValueException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class QuantityTest {

    @Test
    public void givenValue_whenValueIsNegative_thenThrowInvalidValueException() {
        //given
        int value = -10;

        //when
        assertThat(value).isNotNull().isNegative();
        Throwable throwable = catchThrowable(() -> {
            Quantity quantity = Quantity.valueOf(value);
        });

        //then
        assertThat(throwable)
                .isNotNull()
                .isInstanceOf(InvalidValueException.class)
                .hasMessage("Quantity value can not be negative!");
    }

    @Test
    public void givenValue_whenValueIsNull_thenReturnZeroQuantity() {
        //given
        Integer value = null;

        //when
        assertThat(value).isNull();

        //then
        Quantity quantity = Quantity.valueOf(value);
        assertThat(quantity).isNotNull();
        assertThat(quantity.isZero()).isTrue();
    }

    @Test
    public void givenValue_whenValueIsNotNull_thenReturnQuantityOfValue() {
        //given
        Integer value = 10;

        //when
        assertThat(value).isNotNull().isPositive();

        //then
        Quantity quantity = Quantity.valueOf(value);
        assertThat(quantity).isNotNull();
        assertThat(quantity.intValue()).isEqualTo(value);
    }

    @Test
    public void givenTwoValues_whenValuesAreSame_thenQuantityEqualsReturnTrue() {
        //given
        Integer value1 = 10;
        Integer value2 = 10;

        //when
        assertThat(value1).isEqualTo(value2);

        //then
        Quantity quantity1 = Quantity.valueOf(value1);
        Quantity quantity2 = Quantity.valueOf(value2);
        assertThat(quantity1).isEqualTo(quantity2);
    }

    @Test
    public void givenTwoValues_whenValuesAreDifferent_thenEqualsReturnFalse() {
        //given
        Integer value1 = 10;
        Integer value2 = 20;

        //when
        assertThat(value1).isNotEqualTo(value2);

        //then
        Quantity quantity1 = Quantity.valueOf(value1);
        Quantity quantity2 = Quantity.valueOf(value2);
        assertThat(quantity1).isNotEqualTo(quantity2);
    }

    @Test
    public void givenTwoValues_whenValueOfQuantity1LessThanValueOfQuantity2_thenQuantity1LessThanQuantity2() {
        //given
        Integer value1 = 10;
        Integer value2 = 20;

        //when
        assertThat(value1).isLessThan(value2);

        //then
        Quantity quantity1 = Quantity.valueOf(value1);
        Quantity quantity2 = Quantity.valueOf(value2);
        assertThat(quantity1).isLessThan(quantity2);
    }

    @Test
    public void givenTwoValues_whenValueOfQuantity1GreaterThanValueOfQuantity2_thenQuantity1GreaterThanQuantity2() {
        //given
        Integer value1 = 20;
        Integer value2 = 10;

        //when
        assertThat(value1).isGreaterThan(value2);

        //then
        Quantity quantity1 = Quantity.valueOf(value1);
        Quantity quantity2 = Quantity.valueOf(value2);
        assertThat(quantity1).isGreaterThan(quantity2);
    }

    @Test
    public void givenTwoQuantitys_addQuantitys_thenReturnResultAsNewQuantity() {
        //given
        Quantity quantity1 = Quantity.valueOf(20);
        Quantity quantity2 = Quantity.valueOf(10);

        //when
        Quantity result = quantity1.add(quantity2);

        //then
        assertThat(result).isEqualTo(Quantity.valueOf(30));
    }

    @Test
    public void givenTwoQuantities_subtractQuantities_thenReturnResultAsNewQuantity() {
        //given
        Quantity quantity1 = Quantity.valueOf(20);
        Quantity quantity2 = Quantity.valueOf(10);

        //when
        Quantity result = quantity1.subtract(quantity2);

        //then
        assertThat(result).isEqualTo(Quantity.valueOf(10));
    }

    @Test
    public void givenQuantityAndIntegerValue_multiplyQuantityWithDouble_thenReturnResultAsNewQuantity() {
        //given
        Quantity quantity = Quantity.valueOf(20);
        Integer multiplier = 2;

        //when
        Quantity result = quantity.multiply(multiplier);

        //then
        assertThat(result).isEqualTo(Quantity.valueOf(40));
    }

    @Test
    public void givenQuantityAndIntegerValue_divideQuantityWithDouble_thenReturnResultAsNewQuantity() {
        //given
        Quantity quantity = Quantity.valueOf(20);
        Integer division = 2;

        //when
        Quantity result = quantity.divide(division);

        //then
        assertThat(result).isEqualTo(Quantity.valueOf(10));
    }

    @Test
    public void givenQuantityAndZeroValue_divideQuantityWithZero_thenReturnResultAsNewQuantity() {
        //given
        Quantity quantity = Quantity.valueOf(20);
        Integer division = 0;

        //when
        Throwable throwable = catchThrowable(() -> quantity.divide(division));

        //then
        assertThat(throwable)
                .isNotNull()
                .isInstanceOf(InvalidValueException.class)
                .hasMessage("Divisor value can not be negative!");
    }
}
