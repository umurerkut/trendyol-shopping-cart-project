package com.trendyol.shoppingcart.core.domain.value;

import com.trendyol.shoppingcart.core.exception.InvalidValueException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DiscountNameTest {

    @Test
    public void givenValue_whenValueIsNull_thenThrowInvalidValueException() {
        //given
        String value = null;

        //when
        assertNull(value);

        //then
        InvalidValueException exception = assertThrows(InvalidValueException.class, () -> DiscountName.valueOf(value));
        assertNotNull(exception);
        assertEquals(exception.getMessage(), "Discount name value can not be null.");
    }

    @Test
    public void givenValue_whenValueIsBlank_thenThrowInvalidValueException() {
        //given
        String value = "";

        //when
        assertEquals(value, "");

        //then
        InvalidValueException exception = assertThrows(InvalidValueException.class, () -> DiscountName.valueOf(value));
        assertNotNull(exception);
        assertEquals(exception.getMessage(), "Discount name value can not be blank.");
    }

    @Test
    public void givenValue_whenValueIsNotNullAndNotBlank_thenReturnDiscountNameOfValue() {
        //given
        String value = "Test DiscountName";

        //when
        assertNotNull(value);
        assertNotEquals(value, "");

        //then
        DiscountName discountName = DiscountName.valueOf(value);
        assertNotNull(discountName);
        assertEquals(discountName.getValue(), value);
    }

    @Test
    public void givenTwoValues_whenValuesAreSame_thenDiscountNameEqualsReturnTrue() {
        //given
        String value1 = "Test DiscountName";
        String value2 = "Test DiscountName";

        //when
        assertEquals(value1, value2);

        //then
        DiscountName discountName1 = DiscountName.valueOf(value1);
        DiscountName discountName2 = DiscountName.valueOf(value2);
        assertEquals(discountName1, discountName2);
    }

    @Test
    public void givenTwoValues_whenValuesAreDifferent_thenEqualsReturnFalse() {
        //given
        String value1 = "Test DiscountName 1";
        String value2 = "Test DiscountName 2";

        //when
        assertNotEquals(value1, value2);

        //then
        DiscountName discountName1 = DiscountName.valueOf(value1);
        DiscountName discountName2 = DiscountName.valueOf(value2);
        assertNotEquals(discountName1, discountName2);
    }

    @Test
    public void givenTwoValues_whenValueOfDiscountName1LesserThanValueOfDiscountName2_thenDiscountName1LesserThanDiscountName2() {
        //given
        String value1 = "Test DiscountName 1";
        String value2 = "Test DiscountName 2";

        //when
        assertEquals(-1, value1.compareTo(value2));

        //then
        DiscountName discountName1 = DiscountName.valueOf(value1);
        DiscountName discountName2 = DiscountName.valueOf(value2);
        assertEquals(-1, discountName1.compareTo(discountName2));
    }

    @Test
    public void givenTwoValues_whenValueOfDiscountName1GreaterThanValueOfDiscountName2_thenDiscountName1GreaterThanDiscountName2() {
        //given
        String value1 = "Test DiscountName 2";
        String value2 = "Test DiscountName 1";

        //when
        assertEquals(1, value1.compareTo(value2));

        //then
        DiscountName discountName1 = DiscountName.valueOf(value1);
        DiscountName discountName2 = DiscountName.valueOf(value2);
        assertEquals(1, discountName1.compareTo(discountName2));
    }
}
