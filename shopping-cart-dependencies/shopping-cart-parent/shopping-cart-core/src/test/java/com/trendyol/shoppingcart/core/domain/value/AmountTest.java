package com.trendyol.shoppingcart.core.domain.value;

import com.trendyol.shoppingcart.core.exception.InvalidValueException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class AmountTest {

    @Test
    public void givenValue_whenValueIsNull_thenReturnZeroAmount() {
        //given
        Double value = null;

        //when
        assertThat(value).isNull();

        //then
        Amount amount = Amount.valueOf(value);
        assertThat(amount).isNotNull();
        assertThat(amount.isZero()).isTrue();
    }

    @Test
    public void givenValue_whenValueIsNotNull_thenReturnAmountOfValue() {
        //given
        Double value = 10D;

        //when
        assertThat(value).isNotNull();

        //then
        Amount amount = Amount.valueOf(value);
        assertThat(amount).isNotNull();
        assertThat(amount.doubleValue()).isEqualTo(value);
    }

    @Test
    public void givenValue_whenValueIsNotNullAndPositive_thenReturnPositiveAmountOfValue() {
        //given
        Double value = 10D;

        //when
        assertThat(value).isNotNull().isPositive();

        //then
        Amount amount = Amount.valueOf(value);
        assertThat(amount).isNotNull();
        assertThat(amount.isPositive()).isTrue();
        assertThat(amount.doubleValue()).isEqualTo(value);
    }

    @Test
    public void givenValue_whenValueIsNotNullAndPositive_thenReturnNegativeAmountOfValue() {
        //given
        Double value = -10D;

        //when
        assertThat(value).isNotNull().isNegative();

        //then
        Amount amount = Amount.valueOf(value);
        assertThat(amount).isNotNull();
        assertThat(amount.isNegative()).isTrue();
        assertThat(amount.doubleValue()).isEqualTo(value);
    }

    @Test
    public void givenTwoValues_whenValuesAreSame_thenAmountEqualsReturnTrue() {
        //given
        Double value1 = 10D;
        Double value2 = 10D;

        //when
        assertThat(value1).isEqualTo(value2);

        //then
        Amount amount1 = Amount.valueOf(value1);
        Amount amount2 = Amount.valueOf(value2);
        assertThat(amount1).isEqualTo(amount2);
    }

    @Test
    public void givenTwoValues_whenValuesAreDifferent_thenEqualsReturnFalse() {
        //given
        Double value1 = 10D;
        Double value2 = 20D;

        //when
        assertThat(value1).isNotEqualTo(value2);

        //then
        Amount amount1 = Amount.valueOf(value1);
        Amount amount2 = Amount.valueOf(value2);
        assertThat(amount1).isNotEqualTo(amount2);
    }

    @Test
    public void givenTwoValues_whenValueOfAmount1LessThanValueOfAmount2_thenAmount1LessThanAmount2() {
        //given
        Double value1 = 10D;
        Double value2 = 20D;

        //when
        assertThat(value1).isLessThan(value2);

        //then
        Amount amount1 = Amount.valueOf(value1);
        Amount amount2 = Amount.valueOf(value2);
        assertThat(amount1).isLessThan(amount2);
    }

    @Test
    public void givenTwoValues_whenValueOfAmount1GreaterThanValueOfAmount2_thenAmount1GreaterThanAmount2() {
        //given
        Double value1 = 20D;
        Double value2 = 10D;

        //when
        assertThat(value1).isGreaterThan(value2);

        //then
        Amount amount1 = Amount.valueOf(value1);
        Amount amount2 = Amount.valueOf(value2);
        assertThat(amount1).isGreaterThan(amount2);
    }

    @Test
    public void givenTwoAmounts_addAmounts_thenReturnResultAsNewAmount() {
        //given
        Amount amount1 = Amount.valueOf(20D);
        Amount amount2 = Amount.valueOf(10D);

        //when
        Amount result = amount1.add(amount2);

        //then
        assertThat(result).isEqualTo(Amount.valueOf(30D));
    }

    @Test
    public void givenTwoAmounts_subtractAmounts_thenReturnResultAsNewAmount() {
        //given
        Amount amount1 = Amount.valueOf(20D);
        Amount amount2 = Amount.valueOf(10D);

        //when
        Amount result = amount1.subtract(amount2);

        //then
        assertThat(result).isEqualTo(Amount.valueOf(10D));
    }

    @Test
    public void givenAmountAndDoubleValue_multiplyAmountWithDouble_thenReturnResultAsNewAmount() {
        //given
        Amount amount = Amount.valueOf(20D);
        Double multiplier = 2D;

        //when
        Amount result = amount.multiply(multiplier);

        //then
        assertThat(result).isEqualTo(Amount.valueOf(40D));
    }

    @Test
    public void givenAmountAndDoubleValue_divideAmountWithDouble_thenReturnResultAsNewAmount() {
        //given
        Amount amount = Amount.valueOf(20D);
        Double division = 2D;

        //when
        Amount result = amount.divide(division);

        //then
        assertThat(result).isEqualTo(Amount.valueOf(10D));
    }

    @Test
    public void givenAmountAndZeroValue_divideAmountWithZero_thenReturnResultAsNewAmount() {
        //given
        Amount amount = Amount.valueOf(20D);
        Double division = 0D;

        //when
        Throwable throwable = catchThrowable(() -> amount.divide(division));

        //then
        assertThat(throwable)
                .isNotNull()
                .isInstanceOf(InvalidValueException.class)
                .hasMessage("Divisor value can not be negative!");
    }
}
