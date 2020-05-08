package com.trendyol.shoppingcart.core.domain.value;

import com.trendyol.shoppingcart.core.exception.InvalidValueException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TitleTest {

    @Test
    @DisplayName("Should Return Title When Valid Value Is Given")
    public void shouldReturnTitle_validValueGiven() {
        //given
        String value = "Test Title";

        //when
        Title title = Title.ofValue(value);

        //then
        assertNotNull(title);
        assertEquals(title.getValue(), value);
    }

    @Test
    @DisplayName("Should Throw Invalid Value Exception When Null Value Is Given")
    public void shouldThrowInvalidValueException_nullValueGiven() {
        //given
        String value = null;

        //when
        InvalidValueException exception = assertThrows(InvalidValueException.class, () -> Title.ofValue(value));

        //then
        assertNotNull(exception);
        assertEquals(exception.getMessage(), "Title value can not be null.");
    }

    @Test
    @DisplayName("Should Throw Invalid Value Exception When Blank Value Is Given")
    public void shouldThrowInvalidValueException_blankValueGiven() {
        //given
        String value = "";

        //when
        InvalidValueException exception = assertThrows(InvalidValueException.class, () -> Title.ofValue(value));

        //then
        assertNotNull(exception);
        assertEquals(exception.getMessage(), "Title value can not be blank.");
    }

    @Test
    @DisplayName("Should Return Same Titles When Same Values Are Given")
    public void shouldReturnSameTitles_sameValuesGiven() {
        //given
        String value = "Test Title";

        //when
        Title title1 = Title.ofValue(value);
        Title title2 = Title.ofValue(value);

        //then
        assertNotNull(title1);
        assertEquals(title1.getValue(), value);
        assertNotNull(title2);
        assertEquals(title2.getValue(), value);
        assertEquals(title1, title2);
        assertEquals(title1.getValue(), title2.getValue());
    }

    @Test
    @DisplayName("Should Return Different Titles When Different Values Are Given")
    public void shouldReturnDifferentTitles_differentValueGiven() {
        //given
        String value1 = "Test Title 1";
        String value2 = "Test Title 2";

        //when
        Title title1 = Title.ofValue(value1);
        Title title2 = Title.ofValue(value2);

        //then
        assertNotNull(title1);
        assertEquals(title1.getValue(), value1);
        assertNotNull(title2);
        assertEquals(title2.getValue(), value2);
        assertNotEquals(title1, title2);
        assertNotEquals(title1.getValue(), title2.getValue());
    }

    @Test
    @DisplayName("Should Compare Different Titles When Different Values Are Given")
    public void shouldCompareDifferentTitles_differentValuesGiven() {
        //given
        String value1 = "A Test Title";
        String value2 = "B Test Title";

        //when
        Title title1 = Title.ofValue(value1);
        Title title2 = Title.ofValue(value2);
        int result1 = title1.compareTo(title2);
        int result2 = title2.compareTo(title1);

        //then
        assertNotNull(title1);
        assertEquals(title1.getValue(), value1);
        assertNotNull(title2);
        assertEquals(title2.getValue(), value2);
        assertNotEquals(title1, title2);
        assertNotEquals(title1.getValue(), title2.getValue());
        assertEquals(result1, value1.compareTo(value2));
        assertTrue(result1 < 0);
        assertEquals(result2, value2.compareTo(value1));
        assertTrue(result2 > 0);
    }
}
