package com.trendyol.shoppingcart.core.domain.value;

import com.trendyol.shoppingcart.core.exception.InvalidValueException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RateTest {

    @Test
    public void givenValue_whenValueIsLesserThan0_thenThrowInvalidValueException() {
        //given
        double value = -10D;

        //when
        InvalidValueException exception = assertThrows(InvalidValueException.class, () -> Rate.valueOf(value));

        //then
        assertNotNull(exception);
        assertEquals(exception.getMessage(), "Rate value can not be less than " + 0D + '!');
    }


    @Test
    public void givenValue_whenValueIsGreaterThan100_thenThrowInvalidValueException() {
        //given
        double value = 110D;

        //when
        InvalidValueException exception = assertThrows(InvalidValueException.class, () -> Rate.valueOf(value));

        //then
        assertNotNull(exception);
        assertEquals(exception.getMessage(), "Rate value can not be greater than " + 100D + '!');
    }

    @Test
    public void givenValue_whenValueIsNull_thenReturnZeroRate() {
        //given
        Double value = null;

        //when
        assertNull(value);

        //then
        Rate rate = Rate.valueOf(value);
        assertNotNull(rate);
        assertTrue(rate.isZero());
    }

    @Test
    public void givenValue_whenValueIsNotNull_thenReturnRateOfValue() {
        //given
        Double value = 10D;

        //when
        assertNotNull(value);
        assertNotEquals(value, "");

        //then
        Rate rate = Rate.valueOf(value);
        assertNotNull(rate);
        assertEquals(rate.doubleValue(), value);
    }

    @Test
    public void givenTwoValues_WhenValuesAreSame_thenRateEqualsReturnTrue() {
        //given
        Double value1 = 10D;
        Double value2 = 10D;

        //when
        assertEquals(value1, value2);

        //then
        Rate rate1 = Rate.valueOf(value1);
        Rate rate2 = Rate.valueOf(value2);
        assertEquals(rate1, rate2);
    }

    @Test
    public void givenTwoValues_WhenValuesAreDifferent_thenEqualsReturnFalse() {
        //given
        Double value1 = 10D;
        Double value2 = 20D;

        //when
        assertNotEquals(value1, value2);

        //then
        Rate rate1 = Rate.valueOf(value1);
        Rate rate2 = Rate.valueOf(value2);
        assertNotEquals(rate1, rate2);
    }

    @Test
    public void givenTwoValues_WhenValueOfRate1LesserThanValueOfRate2_thenRate1LesserThanRate2() {
        //given
        Double value1 = 10D;
        Double value2 = 20D;

        //when
        assertEquals(-1, value1.compareTo(value2));

        //then
        Rate rate1 = Rate.valueOf(value1);
        Rate rate2 = Rate.valueOf(value2);
        assertTrue(rate1.isLessThan(rate2));
    }

    @Test
    public void givenTwoValues_WhenValueOfRate1GreaterThanValueOfRate2_thenRate1GreaterThanRate2() {
        //given
        Double value1 = 20D;
        Double value2 = 10D;

        //when
        assertEquals(1, value1.compareTo(value2));

        //then
        Rate rate1 = Rate.valueOf(value1);
        Rate rate2 = Rate.valueOf(value2);
        assertTrue(rate1.isGreaterThan(rate2));
    }

    @Test
    public void givenTwoRates_addRates_thenReturnResultAsNewRate() {
        //given
        Rate rate1 = Rate.valueOf(20D);
        Rate rate2 = Rate.valueOf(10D);

        //when
        Rate result = rate1.add(rate2);

        //then
        assertEquals(result, Rate.valueOf(30D));
    }

    @Test
    public void givenTwoRates_subtractRates_thenReturnResultAsNewRate() {
        //given
        Rate rate1 = Rate.valueOf(20D);
        Rate rate2 = Rate.valueOf(10D);

        //when
        Rate result = rate1.subtract(rate2);

        //then
        assertEquals(result, Rate.valueOf(10D));
    }

    @Test
    public void givenRateAndDoubleValue_multiplyRateWithDouble_thenReturnResultAsNewRate() {
        //given
        Rate rate = Rate.valueOf(20D);
        Double multiplier = 2D;

        //when
        Rate result = rate.multiply(multiplier);

        //then
        assertEquals(result, Rate.valueOf(40D));
    }

    @Test
    public void givenRateAndDoubleValue_divideRateWithDouble_thenThrowInvalidValueException() {
        //given
        Rate rate = Rate.valueOf(20D);
        Double division = 2D;

        //when
        Rate result = rate.divide(division);

        //then
        assertEquals(result, Rate.valueOf(10D));
    }

    @Test
    public void givenRateAndZeroValue_divideRateWithZero_thenReturnResultAsNewRate() {
        //given
        Rate rate = Rate.valueOf(20D);
        Double division = 0D;

        //when
        InvalidValueException exception = assertThrows(InvalidValueException.class, () -> rate.divide(division));

        //then
        assertNotNull(exception);
        assertEquals(exception.getMessage(), "Divisor value can not be negative!");
    }
}
