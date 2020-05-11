package com.trendyol.shoppingcart.core.domain.value;

import com.trendyol.shoppingcart.core.exception.InvalidValueException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class TitleTest {

    @Test
    public void givenValue_whenValueIsNull_thenThrowInvalidValueException() {
        //given
        String value = null;

        //when
        assertThat(value).isNull();
        Throwable throwable = catchThrowable(() -> {
            Title title = Title.valueOf(value);
        });

        //then
        assertThat(throwable)
                .isNotNull()
                .isInstanceOf(InvalidValueException.class)
                .hasMessage("Title value can not be null!");
    }

    @Test
    public void givenValue_whenValueIsBlank_thenThrowInvalidValueException() {
        //given
        String value = "";

        //when
        assertThat(value).isBlank();
        Throwable throwable = catchThrowable(() -> {
            Title title = Title.valueOf(value);
        });

        //then
        assertThat(throwable)
                .isNotNull()
                .isInstanceOf(InvalidValueException.class)
                .hasMessage("Title value can not be blank!");
    }

    @Test
    public void givenValue_whenValueIsNotNullAndNotBlank_thenReturnTitleOfValue() {
        //given
        String value = "Test Title";

        //when
        assertThat(value).isNotNull().isNotBlank();

        //then
        Title title = Title.valueOf(value);
        assertThat(title).isNotNull();
        assertThat(title.getValue()).isEqualTo(value);
    }

    @Test
    public void givenTwoValues_whenValuesAreSame_thenTitlesAreEqual() {
        //given
        String value1 = "Test Title";
        String value2 = "Test Title";

        //when
        assertThat(value1).isEqualTo(value2);

        //then
        Title title1 = Title.valueOf(value1);
        Title title2 = Title.valueOf(value2);
        assertThat(title1).isEqualTo(title2);
    }

    @Test
    public void givenTwoValues_whenValuesAreDifferent_thenTitlesAreNotEqual() {
        //given
        String value1 = "Test Title 1";
        String value2 = "Test Title 2";

        //when
        assertThat(value1).isNotEqualTo(value2);

        //then
        Title title1 = Title.valueOf(value1);
        Title title2 = Title.valueOf(value2);
        assertThat(title1).isNotEqualTo(title2);
    }

    @Test
    public void givenTwoValues_whenValueOfTitle1LessThanValueOfTitle2_thenTitle1LessThanTitle2() {
        //given
        String value1 = "Test Title 1";
        String value2 = "Test Title 2";

        //when
        assertThat(value1).isLessThan(value2);

        //then
        Title title1 = Title.valueOf(value1);
        Title title2 = Title.valueOf(value2);
        assertThat(title1).isLessThan(title2);
    }

    @Test
    public void givenTwoValues_whenValueOfTitle1GreaterThanValueOfTitle2_thenTitle1GreaterThanTitle2() {
        //given
        String value1 = "Test Title 2";
        String value2 = "Test Title 1";

        //when
        assertThat(value1).isGreaterThan(value2);

        //then
        Title title1 = Title.valueOf(value1);
        Title title2 = Title.valueOf(value2);
        assertThat(title1).isGreaterThan(title2);
    }
}
