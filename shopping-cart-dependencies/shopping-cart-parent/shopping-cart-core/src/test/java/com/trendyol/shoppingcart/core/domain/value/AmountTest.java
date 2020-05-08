package com.trendyol.shoppingcart.core.domain.value;

import com.trendyol.shoppingcart.core.exception.InvalidValueException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AmountTest {

    @Test
    public void givenValue_whenValueIsNull_thenReturnZeroAmount() {
        //given
        Double value = null;

        //when
        assertNull(value);

        //then
        Amount amount = Amount.valueOf(value);
        assertNotNull(amount);
        assertTrue(amount.isZero());
    }

    @Test
    public void givenValue_whenValueIsNotNull_thenReturnAmountOfValue() {
        //given
        Double value = 10D;

        //when
        assertNotNull(value);
        assertNotEquals(value, "");

        //then
        Amount amount = Amount.valueOf(value);
        assertNotNull(amount);
        assertEquals(amount.doubleValue(), value);
    }

    @Test
    public void givenValue_whenValueIsNotNullAndPositive_thenReturnPositiveAmountOfValue() {
        //given
        Double value = 10D;

        //when
        assertNotNull(value);
        assertNotEquals(value, "");

        //then
        Amount amount = Amount.valueOf(value);
        assertNotNull(amount);
        assertTrue(amount.isPositive());
        assertEquals(amount.doubleValue(), value);
    }

    @Test
    public void givenValue_whenValueIsNotNullAndPositive_thenReturnNegativeAmountOfValue() {
        //given
        Double value = -10D;

        //when
        assertNotNull(value);
        assertNotEquals(value, "");

        //then
        Amount amount = Amount.valueOf(value);
        assertNotNull(amount);
        assertTrue(amount.isNegative());
        assertEquals(amount.doubleValue(), value);
    }

    @Test
    public void givenTwoValues_whenValuesAreSame_thenAmountEqualsReturnTrue() {
        //given
        Double value1 = 10D;
        Double value2 = 10D;

        //when
        assertEquals(value1, value2);

        //then
        Amount amount1 = Amount.valueOf(value1);
        Amount amount2 = Amount.valueOf(value2);
        assertEquals(amount1, amount2);
    }

    @Test
    public void givenTwoValues_whenValuesAreDifferent_thenEqualsReturnFalse() {
        //given
        Double value1 = 10D;
        Double value2 = 20D;

        //when
        assertNotEquals(value1, value2);

        //then
        Amount amount1 = Amount.valueOf(value1);
        Amount amount2 = Amount.valueOf(value2);
        assertNotEquals(amount1, amount2);
    }

    @Test
    public void givenTwoValues_whenValueOfAmount1LesserThanValueOfAmount2_thenAmount1LesserThanAmount2() {
        //given
        Double value1 = 10D;
        Double value2 = 20D;

        //when
        assertEquals(-1, value1.compareTo(value2));

        //then
        Amount amount1 = Amount.valueOf(value1);
        Amount amount2 = Amount.valueOf(value2);
        assertTrue(amount1.isLessThan(amount2));
    }

    @Test
    public void givenTwoValues_whenValueOfAmount1GreaterThanValueOfAmount2_thenAmount1GreaterThanAmount2() {
        //given
        Double value1 = 20D;
        Double value2 = 10D;

        //when
        assertEquals(1, value1.compareTo(value2));

        //then
        Amount amount1 = Amount.valueOf(value1);
        Amount amount2 = Amount.valueOf(value2);
        assertTrue(amount1.isGreaterThan(amount2));
    }

    @Test
    public void givenTwoAmounts_addAmounts_thenReturnResultAsNewAmount() {
        //given
        Amount amount1 = Amount.valueOf(20D);
        Amount amount2 = Amount.valueOf(10D);

        //when
        Amount result = amount1.add(amount2);

        //then
        assertEquals(result, Amount.valueOf(30D));
    }

    @Test
    public void givenTwoAmounts_subtractAmounts_thenReturnResultAsNewAmount() {
        //given
        Amount amount1 = Amount.valueOf(20D);
        Amount amount2 = Amount.valueOf(10D);

        //when
        Amount result = amount1.subtract(amount2);

        //then
        assertEquals(result, Amount.valueOf(10D));
    }

    @Test
    public void givenAmountAndDoubleValue_multiplyAmountWithDouble_thenReturnResultAsNewAmount() {
        //given
        Amount amount = Amount.valueOf(20D);
        Double multiplier = 2D;

        //when
        Amount result = amount.multiply(multiplier);

        //then
        assertEquals(result, Amount.valueOf(40D));
    }

    @Test
    public void givenAmountAndDoubleValue_divideAmountWithDouble_thenReturnResultAsNewAmount() {
        //given
        Amount amount = Amount.valueOf(20D);
        Double division = 2D;

        //when
        Amount result = amount.divide(division);

        //then
        assertEquals(result, Amount.valueOf(10D));
    }

    @Test
    public void givenAmountAndZeroValue_divideAmountWithZero_thenReturnResultAsNewAmount() {
        //given
        Amount amount = Amount.valueOf(20D);
        Double division = 0D;

        //when
        InvalidValueException exception = assertThrows(InvalidValueException.class, () -> amount.divide(division));

        //then
        assertNotNull(exception);
        assertEquals(exception.getMessage(), "Divisor value can not be negative!");
    }
}
