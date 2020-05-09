package com.trendyol.shoppingcart.core.domain.value;

import com.trendyol.shoppingcart.core.exception.InvalidValueException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class RateTest {

    @Test
    public void givenValue_whenValueIsLessThan0_thenThrowInvalidValueException() {
        //given
        double value = -10D;

        //when
        Throwable throwable = catchThrowable(() -> {
            Rate rate = Rate.valueOf(value);
        });

        //then
        assertThat(throwable)
                .isNotNull()
                .isInstanceOf(InvalidValueException.class)
                .hasMessage("Rate value can not be less than " + 0D + '!');
    }


    @Test
    public void givenValue_whenValueIsGreaterThan100_thenThrowInvalidValueException() {
        //given
        double value = 110D;

        //when
        Throwable throwable = catchThrowable(() -> {
            Rate rate = Rate.valueOf(value);
        });

        //then
        assertThat(throwable)
                .isNotNull()
                .isInstanceOf(InvalidValueException.class)
                .hasMessage("Rate value can not be greater than " + 100D + '!');
    }

    @Test
    public void givenValue_whenValueIsNull_thenReturnZeroRate() {
        //given
        Double value = null;

        //when
        assertThat(value).isNull();

        //then
        Rate rate = Rate.valueOf(value);
        assertThat(rate).isNotNull();
        assertThat(rate.isZero()).isTrue();
    }

    @Test
    public void givenValue_whenValueIsNotNull_thenReturnRateOfValue() {
        //given
        Double value = 10D;

        //when
        assertThat(value).isNotNull();

        //then
        Rate rate = Rate.valueOf(value);
        assertThat(rate).isNotNull();
        assertThat(rate.doubleValue()).isEqualTo(value);
    }

    @Test
    public void givenTwoValues_whenValuesAreSame_thenRateEqualsReturnTrue() {
        //given
        Double value1 = 10D;
        Double value2 = 10D;

        //when
        assertThat(value1).isEqualTo(value2);

        //then
        Rate rate1 = Rate.valueOf(value1);
        Rate rate2 = Rate.valueOf(value2);
        assertThat(rate1).isEqualTo(rate2);
    }

    @Test
    public void givenTwoValues_whenValuesAreDifferent_thenEqualsReturnFalse() {
        //given
        Double value1 = 10D;
        Double value2 = 20D;

        //when
        assertThat(value1).isNotEqualTo(value2);

        //then
        Rate rate1 = Rate.valueOf(value1);
        Rate rate2 = Rate.valueOf(value2);
        assertThat(rate1).isNotEqualTo(rate2);
    }

    @Test
    public void givenTwoValues_whenValueOfRate1LessThanValueOfRate2_thenRate1LessThanRate2() {
        //given
        Double value1 = 10D;
        Double value2 = 20D;

        //when
        assertThat(value1).isLessThan(value2);

        //then
        Rate rate1 = Rate.valueOf(value1);
        Rate rate2 = Rate.valueOf(value2);
        assertThat(rate1).isLessThan(rate2);
    }

    @Test
    public void givenTwoValues_whenValueOfRate1GreaterThanValueOfRate2_thenRate1GreaterThanRate2() {
        //given
        Double value1 = 20D;
        Double value2 = 10D;

        //when
        assertThat(value1).isGreaterThan(value2);

        //then
        Rate rate1 = Rate.valueOf(value1);
        Rate rate2 = Rate.valueOf(value2);
        assertThat(rate1).isGreaterThan(rate2);
    }

    @Test
    public void givenTwoRates_addRates_thenReturnResultAsNewRate() {
        //given
        Rate rate1 = Rate.valueOf(20D);
        Rate rate2 = Rate.valueOf(10D);

        //when
        Rate result = rate1.add(rate2);

        //then
        assertThat(result).isEqualTo(Rate.valueOf(30D));
    }

    @Test
    public void givenTwoRates_subtractRates_thenReturnResultAsNewRate() {
        //given
        Rate rate1 = Rate.valueOf(20D);
        Rate rate2 = Rate.valueOf(10D);

        //when
        Rate result = rate1.subtract(rate2);

        //then
        assertThat(result).isEqualTo(Rate.valueOf(10D));
    }

    @Test
    public void givenRateAndDoubleValue_multiplyRateWithDouble_thenReturnResultAsNewRate() {
        //given
        Rate rate = Rate.valueOf(20D);
        Double multiplier = 2D;

        //when
        Rate result = rate.multiply(multiplier);

        //then
        assertThat(result).isEqualTo(Rate.valueOf(40D));
    }

    @Test
    public void givenRateAndDoubleValue_divideRateWithDouble_thenThrowInvalidValueException() {
        //given
        Rate rate = Rate.valueOf(20D);
        Double division = 2D;

        //when
        Rate result = rate.divide(division);

        //then
        assertThat(result).isEqualTo(Rate.valueOf(10D));
    }

    @Test
    public void givenRateAndZeroValue_divideRateWithZero_thenReturnResultAsNewRate() {
        //given
        Rate rate = Rate.valueOf(20D);
        Double division = 0D;

        //when
        Throwable throwable = catchThrowable(() -> rate.divide(division));

        //then
        assertThat(throwable)
                .isNotNull()
                .isInstanceOf(InvalidValueException.class)
                .hasMessage("Divisor value can not be negative!");
    }
}
