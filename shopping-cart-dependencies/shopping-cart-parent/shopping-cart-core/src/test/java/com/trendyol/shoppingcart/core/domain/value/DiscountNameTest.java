package com.trendyol.shoppingcart.core.domain.value;

import com.trendyol.shoppingcart.core.exception.InvalidValueException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class DiscountNameTest {

    @Test
    public void givenValue_whenValueIsNull_thenThrowInvalidValueException() {
        //given
        String value = null;

        //when
        assertThat(value).isNull();
        Throwable throwable = catchThrowable(() -> {
            DiscountName discountName = DiscountName.valueOf(value);
        });

        //then
        assertThat(throwable)
                .isNotNull()
                .isInstanceOf(InvalidValueException.class)
                .hasMessage("Discount name value can not be null.");
    }

    @Test
    public void givenValue_whenValueIsBlank_thenThrowInvalidValueException() {
        //given
        String value = "";

        //when
        assertThat(value).isBlank();
        Throwable throwable = catchThrowable(() -> {
            DiscountName discountName = DiscountName.valueOf(value);
        });

        //then
        assertThat(throwable)
                .isNotNull()
                .isInstanceOf(InvalidValueException.class)
                .hasMessage("Discount name value can not be blank.");
    }

    @Test
    public void givenValue_whenValueIsNotNullAndNotBlank_thenReturnDiscountNameOfValue() {
        //given
        String value = "Test DiscountName";

        //when
        assertThat(value).isNotNull().isNotBlank();

        //then
        DiscountName discountName = DiscountName.valueOf(value);
        assertThat(discountName).isNotNull();
        assertThat(discountName.getValue()).isEqualTo(value);
    }

    @Test
    public void givenTwoValues_whenValuesAreSame_thenDiscountNameEqualsReturnTrue() {
        //given
        String value1 = "Test DiscountName";
        String value2 = "Test DiscountName";

        //when
        assertThat(value1).isEqualTo(value2);

        //then
        DiscountName discountName1 = DiscountName.valueOf(value1);
        DiscountName discountName2 = DiscountName.valueOf(value2);
        assertThat(discountName1).isEqualTo(discountName2);
    }

    @Test
    public void givenTwoValues_whenValuesAreDifferent_thenEqualsReturnFalse() {
        //given
        String value1 = "Test DiscountName 1";
        String value2 = "Test DiscountName 2";

        //when
        assertThat(value1).isNotEqualTo(value2);

        //then
        DiscountName discountName1 = DiscountName.valueOf(value1);
        DiscountName discountName2 = DiscountName.valueOf(value2);
        assertThat(discountName1).isNotEqualTo(discountName2);
    }

    @Test
    public void givenTwoValues_whenValueOfDiscountName1LessThanValueOfDiscountName2_thenDiscountName1LessThanDiscountName2() {
        //given
        String value1 = "Test DiscountName 1";
        String value2 = "Test DiscountName 2";

        //when
        assertThat(value1).isLessThan(value2);

        //then
        DiscountName discountName1 = DiscountName.valueOf(value1);
        DiscountName discountName2 = DiscountName.valueOf(value2);
        assertThat(discountName1).isLessThan(discountName2);
    }

    @Test
    public void givenTwoValues_whenValueOfDiscountName1GreaterThanValueOfDiscountName2_thenDiscountName1GreaterThanDiscountName2() {
        //given
        String value1 = "Test DiscountName 2";
        String value2 = "Test DiscountName 1";

        //when
        assertThat(value1).isGreaterThan(value2);

        //then
        DiscountName discountName1 = DiscountName.valueOf(value1);
        DiscountName discountName2 = DiscountName.valueOf(value2);
        assertThat(discountName1).isGreaterThan(discountName2);
    }
}
