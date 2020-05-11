package com.trendyol.shoppingcart.client.trendyol.domain.value;

import com.trendyol.shoppingcart.core.exception.InvalidValueException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class CouponCodeTest {

    @Test
    public void givenValue_whenValueIsNull_thenThrowInvalidValueException() {
        //given
        String value = null;

        //when
        assertThat(value).isNull();
        Throwable throwable = catchThrowable(() -> {
            CouponCode couponCode = CouponCode.valueOf(value);
        });

        //then
        assertThat(throwable)
                .isNotNull()
                .isInstanceOf(InvalidValueException.class)
                .hasMessage("Coupon code value can not be null!");
    }

    @Test
    public void givenValue_whenValueIsBlank_thenThrowInvalidValueException() {
        //given
        String value = "";

        //when
        assertThat(value).isBlank();
        Throwable throwable = catchThrowable(() -> {
            CouponCode couponCode = CouponCode.valueOf(value);
        });

        //then
        assertThat(throwable)
                .isNotNull()
                .isInstanceOf(InvalidValueException.class)
                .hasMessage("Coupon code value can not be blank!");
    }

    @Test
    public void givenValue_whenValueIsNotNullAndNotBlank_thenReturnCouponCodeOfValue() {
        //given
        String value = "Test CouponCode";

        //when
        assertThat(value).isNotNull().isNotBlank();

        //then
        CouponCode couponCode = CouponCode.valueOf(value);
        assertThat(couponCode).isNotNull();
        assertThat(couponCode.getValue()).isEqualTo(value);
    }

    @Test
    public void givenTwoValues_whenValuesAreSame_thenCouponCodeEqualsReturnTrue() {
        //given
        String value1 = "Test CouponCode";
        String value2 = "Test CouponCode";

        //when
        assertThat(value1).isEqualTo(value2);

        //then
        CouponCode couponCode1 = CouponCode.valueOf(value1);
        CouponCode couponCode2 = CouponCode.valueOf(value2);
        assertThat(couponCode1).isEqualTo(couponCode2);
    }

    @Test
    public void givenTwoValues_whenValuesAreDifferent_thenEqualsReturnFalse() {
        //given
        String value1 = "Test CouponCode 1";
        String value2 = "Test CouponCode 2";

        //when
        assertThat(value1).isNotEqualTo(value2);

        //then
        CouponCode couponCode1 = CouponCode.valueOf(value1);
        CouponCode couponCode2 = CouponCode.valueOf(value2);
        assertThat(couponCode1).isNotEqualTo(couponCode2);
    }

    @Test
    public void givenTwoValues_whenValueOfCouponCode1LessThanValueOfCouponCode2_thenCouponCode1LessThanCouponCode2() {
        //given
        String value1 = "Test CouponCode 1";
        String value2 = "Test CouponCode 2";

        //when
        assertThat(value1).isLessThan(value2);

        //then
        CouponCode couponCode1 = CouponCode.valueOf(value1);
        CouponCode couponCode2 = CouponCode.valueOf(value2);
        assertThat(couponCode1).isLessThan(couponCode2);
    }

    @Test
    public void givenTwoValues_whenValueOfCouponCode1GreaterThanValueOfCouponCode2_thenCouponCode1GreaterThanCouponCode2() {
        //given
        String value1 = "Test CouponCode 2";
        String value2 = "Test CouponCode 1";

        //when
        assertThat(value1).isGreaterThan(value2);

        //then
        CouponCode couponCode1 = CouponCode.valueOf(value1);
        CouponCode couponCode2 = CouponCode.valueOf(value2);
        assertThat(couponCode1).isGreaterThan(couponCode2);
    }
}
