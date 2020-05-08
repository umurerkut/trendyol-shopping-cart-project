package com.trendyol.shoppingcart.core.domain.value;

import com.trendyol.shoppingcart.core.exception.InvalidValueException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class QuantityTest {

    @Test
    public void givenValue_whenValueIsNegative_thenThrowInvalidValueException() {
        //given
        int value = -10;

        //when
        InvalidValueException exception = assertThrows(InvalidValueException.class, () -> Quantity.valueOf(value));

        //then
        assertNotNull(exception);
        assertEquals(exception.getMessage(), "Quantity value can not be negative!");
    }

    @Test
    public void givenValue_whenValueIsNull_thenReturnZeroQuantity() {
        //given
        Integer value = null;

        //when
        assertNull(value);

        //then
        Quantity quantity = Quantity.valueOf(value);
        assertNotNull(quantity);
        assertTrue(quantity.isZero());
    }

    @Test
    public void givenValue_whenValueIsNotNull_thenReturnQuantityOfValue() {
        //given
        Integer value = 10;

        //when
        assertNotNull(value);
        assertNotEquals(value, "");

        //then
        Quantity quantity = Quantity.valueOf(value);
        assertNotNull(quantity);
        assertEquals(quantity.intValue(), value);
    }

    @Test
    public void givenTwoValues_WhenValuesAreSame_thenQuantityEqualsReturnTrue() {
        //given
        Integer value1 = 10;
        Integer value2 = 10;

        //when
        assertEquals(value1, value2);

        //then
        Quantity quantity1 = Quantity.valueOf(value1);
        Quantity quantity2 = Quantity.valueOf(value2);
        assertEquals(quantity1, quantity2);
    }

    @Test
    public void givenTwoValues_WhenValuesAreDifferent_thenEqualsReturnFalse() {
        //given
        Integer value1 = 10;
        Integer value2 = 20;

        //when
        assertNotEquals(value1, value2);

        //then
        Quantity quantity1 = Quantity.valueOf(value1);
        Quantity quantity2 = Quantity.valueOf(value2);
        assertNotEquals(quantity1, quantity2);
    }

    @Test
    public void givenTwoValues_WhenValueOfQuantity1LesserThanValueOfQuantity2_thenQuantity1LesserThanQuantity2() {
        //given
        Integer value1 = 10;
        Integer value2 = 20;

        //when
        assertEquals(-1, value1.compareTo(value2));

        //then
        Quantity quantity1 = Quantity.valueOf(value1);
        Quantity quantity2 = Quantity.valueOf(value2);
        assertTrue(quantity1.isLessThan(quantity2));
    }

    @Test
    public void givenTwoValues_WhenValueOfQuantity1GreaterThanValueOfQuantity2_thenQuantity1GreaterThanQuantity2() {
        //given
        Integer value1 = 20;
        Integer value2 = 10;

        //when
        assertEquals(1, value1.compareTo(value2));

        //then
        Quantity quantity1 = Quantity.valueOf(value1);
        Quantity quantity2 = Quantity.valueOf(value2);
        assertTrue(quantity1.isGreaterThan(quantity2));
    }

    @Test
    public void givenTwoQuantitys_addQuantitys_thenReturnResultAsNewQuantity() {
        //given
        Quantity quantity1 = Quantity.valueOf(20);
        Quantity quantity2 = Quantity.valueOf(10);

        //when
        Quantity result = quantity1.add(quantity2);

        //then
        assertEquals(result, Quantity.valueOf(30));
    }

    @Test
    public void givenTwoQuantities_subtractQuantities_thenReturnResultAsNewQuantity() {
        //given
        Quantity quantity1 = Quantity.valueOf(20);
        Quantity quantity2 = Quantity.valueOf(10);

        //when
        Quantity result = quantity1.subtract(quantity2);

        //then
        assertEquals(result, Quantity.valueOf(10));
    }

    @Test
    public void givenQuantityAndIntegerValue_multiplyQuantityWithDouble_thenReturnResultAsNewQuantity() {
        //given
        Quantity quantity = Quantity.valueOf(20);
        Integer multiplier = 2;

        //when
        Quantity result = quantity.multiply(multiplier);

        //then
        assertEquals(result, Quantity.valueOf(40));
    }

    @Test
    public void givenQuantityAndIntegerValue_divideQuantityWithDouble_thenReturnResultAsNewQuantity() {
        //given
        Quantity quantity = Quantity.valueOf(20);
        Integer division = 2;

        //when
        Quantity result = quantity.divide(division);

        //then
        assertEquals(result, Quantity.valueOf(10));
    }

    @Test
    public void givenQuantityAndZeroValue_divideQuantityWithZero_thenReturnResultAsNewQuantity() {
        //given
        Quantity quantity = Quantity.valueOf(20);
        Integer division = 0;

        //when
        InvalidValueException exception = assertThrows(InvalidValueException.class, () -> quantity.divide(division));

        //then
        assertNotNull(exception);
        assertEquals(exception.getMessage(), "Divisor value can not be negative!");
    }
}
