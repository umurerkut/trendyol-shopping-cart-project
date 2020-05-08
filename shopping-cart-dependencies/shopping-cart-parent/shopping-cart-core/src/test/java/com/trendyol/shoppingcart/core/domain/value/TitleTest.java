package com.trendyol.shoppingcart.core.domain.value;

import com.trendyol.shoppingcart.core.exception.InvalidValueException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TitleTest {

    @Test
    public void givenValue_whenValueIsNull_thenThrowInvalidValueException() {
        //given
        String value = null;

        //when
        assertNull(value);

        //then
        InvalidValueException exception = assertThrows(InvalidValueException.class, () -> Title.valueOf(value));
        assertNotNull(exception);
        assertEquals(exception.getMessage(), "Title value can not be null.");
    }

    @Test
    public void givenValue_whenValueIsBlank_thenThrowInvalidValueException() {
        //given
        String value = "";

        //when
        assertEquals(value, "");

        //then
        InvalidValueException exception = assertThrows(InvalidValueException.class, () -> Title.valueOf(value));
        assertNotNull(exception);
        assertEquals(exception.getMessage(), "Title value can not be blank.");
    }

    @Test
    public void givenValue_whenValueIsNotNullAndNotBlank_thenReturnTitleOfValue() {
        //given
        String value = "Test Title";

        //when
        assertNotNull(value);
        assertNotEquals(value, "");

        //then
        Title title = Title.valueOf(value);
        assertNotNull(title);
        assertEquals(title.getValue(), value);
    }

    @Test
    public void givenTwoValues_whenValuesAreSame_thenTitleEqualsReturnTrue() {
        //given
        String value1 = "Test Title";
        String value2 = "Test Title";

        //when
        assertEquals(value1, value2);

        //then
        Title title1 = Title.valueOf(value1);
        Title title2 = Title.valueOf(value2);
        assertEquals(title1, title2);
    }

    @Test
    public void givenTwoValues_whenValuesAreDifferent_thenEqualsReturnFalse() {
        //given
        String value1 = "Test Title 1";
        String value2 = "Test Title 2";

        //when
        assertNotEquals(value1, value2);

        //then
        Title title1 = Title.valueOf(value1);
        Title title2 = Title.valueOf(value2);
        assertNotEquals(title1, title2);
    }

    @Test
    public void givenTwoValues_whenValueOfTitle1LesserThanValueOfTitle2_thenTitle1LesserThanTitle2() {
        //given
        String value1 = "Test Title 1";
        String value2 = "Test Title 2";

        //when
        assertEquals(-1, value1.compareTo(value2));

        //then
        Title title1 = Title.valueOf(value1);
        Title title2 = Title.valueOf(value2);
        assertEquals(-1, title1.compareTo(title2));
    }

    @Test
    public void givenTwoValues_whenValueOfTitle1GreaterThanValueOfTitle2_thenTitle1GreaterThanTitle2() {
        //given
        String value1 = "Test Title 2";
        String value2 = "Test Title 1";

        //when
        assertEquals(1, value1.compareTo(value2));

        //then
        Title title1 = Title.valueOf(value1);
        Title title2 = Title.valueOf(value2);
        assertEquals(1, title1.compareTo(title2));
    }
}
