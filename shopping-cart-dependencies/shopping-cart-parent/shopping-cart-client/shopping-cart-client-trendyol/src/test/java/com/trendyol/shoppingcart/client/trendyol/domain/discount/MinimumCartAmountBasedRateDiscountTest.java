package com.trendyol.shoppingcart.client.trendyol.domain.discount;

import com.trendyol.shoppingcart.client.trendyol.domain.value.Rate;
import com.trendyol.shoppingcart.core.discountprovider.listener.DiscountProvidedEvent;
import com.trendyol.shoppingcart.core.domain.ShoppingCart;
import com.trendyol.shoppingcart.core.domain.value.Amount;
import com.trendyol.shoppingcart.core.domain.value.DiscountName;
import com.trendyol.shoppingcart.core.exception.InvalidValueException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.*;

//Discount: If amount of cart greater than minimumCartAmount, then apply discount rate of discountRate
public class MinimumCartAmountBasedRateDiscountTest {

    @Test
    public void givenMinimumCartAmount_whenMinimumCartAmountIsNull_thenThrowInvalidValueException() {
        //given
        Amount minimumCartAmount = null;
        Rate discountRate = Rate.valueOf(10D);
        assertThat(minimumCartAmount).isNull();
        assertThat(discountRate).isNotNull();

        //when
        Throwable throwable = catchThrowable(() -> {
            MinimumCartAmountBasedRateDiscount discount = new MinimumCartAmountBasedRateDiscount(minimumCartAmount, discountRate);
        });

        //then
        assertThat(throwable)
                .isNotNull()
                .isInstanceOf(InvalidValueException.class)
                .hasMessage("Minimum cart amount can not be null!");
    }

    @Test
    public void givenMinimumCartAmount_whenMinimumCartAmountIsNegative_thenThrowInvalidValueException() {
        //given
        Amount minimumCartAmount = Amount.valueOf(-10D);
        Rate discountRate = Rate.valueOf(10D);
        assertThat(minimumCartAmount).isNotNull();
        assertThat(discountRate).isNotNull();

        //when
        Throwable throwable = catchThrowable(() -> {
            MinimumCartAmountBasedRateDiscount discount = new MinimumCartAmountBasedRateDiscount(minimumCartAmount, discountRate);
        });

        //then
        assertThat(throwable)
                .isNotNull()
                .isInstanceOf(InvalidValueException.class)
                .hasMessage("Minimum cart amount can not be negative!");
    }

    @Test
    public void givenDiscountRate_whenDiscountRateIsNull_thenThrowInvalidValueException() {
        //given
        Amount minimumCartAmount = Amount.valueOf(10D);
        Rate discountRate = null;
        assertThat(discountRate).isNull();

        //when
        Throwable throwable = catchThrowable(() -> {
            MinimumCartAmountBasedRateDiscount discount = new MinimumCartAmountBasedRateDiscount(minimumCartAmount, discountRate);
        });

        //then
        assertThat(throwable)
                .isNotNull()
                .isInstanceOf(InvalidValueException.class)
                .hasMessage("Rate of discount to be applied can not be null!");
    }

    @Test
    public void givenValidDiscountRateAndMinimumCartAmount_whenCreateMinimumCartAmountBasedRateDiscount_thenReturnMinimumCartAmountBasedRateDiscount() {
        //given
        Amount minimumCartAmount = Amount.valueOf(10D);
        Rate discountRate = Rate.valueOf(10D);
        assertThat(minimumCartAmount).isNotNull();
        assertThat(discountRate).isNotNull();

        //when
        MinimumCartAmountBasedRateDiscount discount = new MinimumCartAmountBasedRateDiscount(minimumCartAmount, discountRate);

        //then
        assertThat(discount).isNotNull();
        assertThat(discount.getDiscountName()).isEqualTo(DiscountName.valueOf("MINIMUM_CART_AMOUNT_BASED_DISCOUNT"));
        assertThat(discount.getCalculationStrategy().getDiscountRate()).isEqualTo(discountRate);
        assertThat(discount.getValidationStrategy().getMinimumCartAmount()).isEqualTo(minimumCartAmount);
    }

    @Test
    public void givenTwoMinimumCartAmountBasedRateDiscounts_whenMinimumCartAmountGreater_thenTheDiscountAlsoGreater() {
        //given
        Rate discountRate = Rate.valueOf(10D);
        Amount minimumCartAmount1 = Amount.valueOf(15D);
        Amount minimumCartAmount2 = Amount.valueOf(10D);
        assertThat(discountRate).isNotNull();
        assertThat(minimumCartAmount1).isNotNull();
        assertThat(minimumCartAmount2).isNotNull();

        MinimumCartAmountBasedRateDiscount discount1 = new MinimumCartAmountBasedRateDiscount(minimumCartAmount1, discountRate);
        MinimumCartAmountBasedRateDiscount discount2 = new MinimumCartAmountBasedRateDiscount(minimumCartAmount2, discountRate);

        //when
        assertThat(minimumCartAmount1.isGreaterThan(minimumCartAmount2)).isTrue();

        //then
        assertThat(discount1.isGreaterThan(discount2)).isTrue();
    }

    @Test
    public void givenTwoMinimumCartAmountBasedRateDiscounts_whenMinimumCartAmountSameButDiscountRateGreater_thenTheDiscountAlsoGreater() {
        //given
        Rate discountRate1 = Rate.valueOf(20D);
        Rate discountRate2 = Rate.valueOf(10D);
        Amount minimumCartAmount = Amount.valueOf(10D);
        MinimumCartAmountBasedRateDiscount discount1 = new MinimumCartAmountBasedRateDiscount(minimumCartAmount, discountRate1);
        MinimumCartAmountBasedRateDiscount discount2 = new MinimumCartAmountBasedRateDiscount(minimumCartAmount, discountRate2);

        //when
        assertThat(discountRate1).isNotNull();
        assertThat(discountRate2).isNotNull();
        assertThat(minimumCartAmount).isNotNull();
        assertThat(discountRate1.isGreaterThan(discountRate2)).isTrue();

        //then
        assertThat(discount1.isGreaterThan(discount2)).isTrue();
    }

    @Test
    public void givenShoppingCartAmount5_whenMinimumCartAmount10_thenDoNotApplyDiscount() {
        //given
        ShoppingCart shoppingCart = mock(ShoppingCart.class, withSettings().useConstructor().defaultAnswer(CALLS_REAL_METHODS));
        when(shoppingCart.getCartAmount()).thenReturn(Amount.valueOf(5D));

        MinimumCartAmountBasedRateDiscount discount = new MinimumCartAmountBasedRateDiscount(Amount.valueOf(10D), Rate.valueOf(5D));
        DiscountProvidedEvent event = mock(DiscountProvidedEvent.class);
        when(event.getDiscount()).thenReturn(discount);

        //when
        shoppingCart.discountProvided(event);
        shoppingCart.applyDiscounts();

        //then
        assertThat(shoppingCart.getDiscountMap()).hasSize(0);
        assertThat(shoppingCart.getTotalDiscount()).isEqualTo(Amount.ofZero());
    }

    @Test
    public void givenShoppingCartAmount10_whenMinimumCartAmount10_thenApplyDiscountRateOf5() {
        //given
        ShoppingCart shoppingCart = mock(ShoppingCart.class, withSettings().useConstructor().defaultAnswer(CALLS_REAL_METHODS));
        when(shoppingCart.getCartAmount()).thenReturn(Amount.valueOf(10D));

        MinimumCartAmountBasedRateDiscount discount = new MinimumCartAmountBasedRateDiscount(Amount.valueOf(10D), Rate.valueOf(5D));
        DiscountProvidedEvent event = mock(DiscountProvidedEvent.class);
        when(event.getDiscount()).thenReturn(discount);

        //when
        shoppingCart.discountProvided(event);
        shoppingCart.applyDiscounts();

        //then
        assertThat(shoppingCart.getDiscountMap()).hasSize(1);
        assertThat(shoppingCart.getTotalDiscount()).isEqualTo(Amount.valueOf(0.5D));
    }

    @Test
    public void givenShoppingCartAmount20_whenMultipleMinimumCartAmountDiscounts_thenApplyTheGreatestDiscount() {
        //given
        ShoppingCart shoppingCart = mock(ShoppingCart.class, withSettings().useConstructor().defaultAnswer(CALLS_REAL_METHODS));
        when(shoppingCart.getCartAmount()).thenReturn(Amount.valueOf(20D));

        MinimumCartAmountBasedRateDiscount discount1 = new MinimumCartAmountBasedRateDiscount(Amount.valueOf(10D), Rate.valueOf(5D));
        MinimumCartAmountBasedRateDiscount discount2 = new MinimumCartAmountBasedRateDiscount(Amount.valueOf(20D), Rate.valueOf(10D));

        DiscountProvidedEvent event1 = mock(DiscountProvidedEvent.class);
        when(event1.getDiscount()).thenReturn(discount1);

        DiscountProvidedEvent event2 = mock(DiscountProvidedEvent.class);
        when(event2.getDiscount()).thenReturn(discount2);

        //when
        shoppingCart.discountProvided(event1);
        shoppingCart.discountProvided(event2);
        shoppingCart.applyDiscounts();

        //then
        assertThat(shoppingCart.getDiscountMap()).hasSize(1);
        assertThat(shoppingCart.getTotalDiscount()).isEqualTo(Amount.valueOf(2D));
    }
}
