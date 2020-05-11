package com.trendyol.shoppingcart.client.trendyol.domain.discount;

import com.trendyol.shoppingcart.core.discountprovider.listener.DiscountProvidedEvent;
import com.trendyol.shoppingcart.core.domain.ShoppingCart;
import com.trendyol.shoppingcart.core.domain.value.Amount;
import com.trendyol.shoppingcart.core.domain.value.DiscountName;
import com.trendyol.shoppingcart.core.exception.InvalidValueException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.*;

//Discount: If amount of cart greater than minimumCartAmount, then apply discount amount of dismountAmount
public class MinimumCartAmountBasedAmountDiscountTest {

    @Test
    public void givenMinimumCartAmount_whenMinimumCartAmountIsNull_thenThrowInvalidValueException() {
        //given
        Amount minimumCartAmount = null;
        Amount discountAmount = Amount.valueOf(10D);
        assertThat(minimumCartAmount).isNull();
        assertThat(discountAmount).isNotNull();

        //when
        Throwable throwable = catchThrowable(() -> {
            MinimumCartAmountBasedAmountDiscount discount = new MinimumCartAmountBasedAmountDiscount(minimumCartAmount, discountAmount);
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
        Amount discountAmount = Amount.valueOf(10D);
        assertThat(minimumCartAmount).isNotNull();
        assertThat(discountAmount).isNotNull();

        //when
        Throwable throwable = catchThrowable(() -> {
            MinimumCartAmountBasedAmountDiscount discount = new MinimumCartAmountBasedAmountDiscount(minimumCartAmount, discountAmount);
        });

        //then
        assertThat(throwable)
                .isNotNull()
                .isInstanceOf(InvalidValueException.class)
                .hasMessage("Minimum cart amount can not be negative!");
    }

    @Test
    public void givenDiscountAmount_whenDiscountAmountIsNull_thenThrowInvalidValueException() {
        //given
        Amount minimumCartAmount = Amount.valueOf(10D);
        Amount discountAmount = null;
        assertThat(discountAmount).isNull();

        //when
        Throwable throwable = catchThrowable(() -> {
            MinimumCartAmountBasedAmountDiscount discount = new MinimumCartAmountBasedAmountDiscount(minimumCartAmount, discountAmount);
        });

        //then
        assertThat(throwable)
                .isNotNull()
                .isInstanceOf(InvalidValueException.class)
                .hasMessage("Amount of discount to be applied can not be null!");
    }

    @Test
    public void givenDiscountAmount_whenDiscountAmountIsZero_thenThrowInvalidValueException() {
        //given
        Amount minimumCartAmount = Amount.valueOf(10D);
        Amount discountAmount = Amount.ofZero();
        assertThat(discountAmount).isNotNull();

        //when
        Throwable throwable = catchThrowable(() -> {
            MinimumCartAmountBasedAmountDiscount discount = new MinimumCartAmountBasedAmountDiscount(minimumCartAmount, discountAmount);
        });

        //then
        assertThat(throwable)
                .isNotNull()
                .isInstanceOf(InvalidValueException.class)
                .hasMessage("Amount of discount to be applied can not be zero!");
    }

    @Test
    public void givenValidDiscountAmountAndMinimumCartAmount_whenCreateMinimumCartAmountBasedAmountDiscount_thenReturnMinimumCartAmountBasedAmountDiscount() {
        //given
        Amount minimumCartAmount = Amount.valueOf(10D);
        Amount discountAmount = Amount.valueOf(10D);
        assertThat(minimumCartAmount).isNotNull();
        assertThat(discountAmount).isNotNull();

        //when
        MinimumCartAmountBasedAmountDiscount discount = new MinimumCartAmountBasedAmountDiscount(minimumCartAmount, discountAmount);

        //then
        assertThat(discount).isNotNull();
        assertThat(discount.getDiscountName()).isEqualTo(DiscountName.valueOf("MINIMUM_CART_AMOUNT_BASED_DISCOUNT"));
        assertThat(discount.getCalculationStrategy().getDiscountAmount()).isEqualTo(discountAmount);
        assertThat(discount.getValidationStrategy().getMinimumCartAmount()).isEqualTo(minimumCartAmount);
    }

    @Test
    public void givenTwoMinimumCartAmountBasedAmountDiscounts_whenMinimumCartAmountGreater_thenTheDiscountAlsoGreater() {
        //given
        Amount discountAmount = Amount.valueOf(10D);
        Amount minimumCartAmount1 = Amount.valueOf(15D);
        Amount minimumCartAmount2 = Amount.valueOf(10D);
        assertThat(discountAmount).isNotNull();
        assertThat(minimumCartAmount1).isNotNull();
        assertThat(minimumCartAmount2).isNotNull();

        MinimumCartAmountBasedAmountDiscount discount1 = new MinimumCartAmountBasedAmountDiscount(minimumCartAmount1, discountAmount);
        MinimumCartAmountBasedAmountDiscount discount2 = new MinimumCartAmountBasedAmountDiscount(minimumCartAmount2, discountAmount);

        //when
        assertThat(minimumCartAmount1.isGreaterThan(minimumCartAmount2)).isTrue();

        //then
        assertThat(discount1.isGreaterThan(discount2)).isTrue();
    }

    @Test
    public void givenTwoMinimumCartAmountBasedAmountDiscounts_whenMinimumCartAmountSameButDiscountAmountGreater_thenTheDiscountAlsoGreater() {
        //given
        Amount discountAmount1 = Amount.valueOf(20D);
        Amount discountAmount2 = Amount.valueOf(10D);
        Amount minimumCartAmount = Amount.valueOf(10D);
        MinimumCartAmountBasedAmountDiscount discount1 = new MinimumCartAmountBasedAmountDiscount(minimumCartAmount, discountAmount1);
        MinimumCartAmountBasedAmountDiscount discount2 = new MinimumCartAmountBasedAmountDiscount(minimumCartAmount, discountAmount2);

        //when
        assertThat(discountAmount1).isNotNull();
        assertThat(discountAmount2).isNotNull();
        assertThat(minimumCartAmount).isNotNull();
        assertThat(discountAmount1.isGreaterThan(discountAmount2)).isTrue();

        //then
        assertThat(discount1.isGreaterThan(discount2)).isTrue();
    }

    @Test
    public void givenShoppingCartAmount5_whenMinimumCartAmount10_thenDoNotApplyDiscount() {
        //given
        ShoppingCart shoppingCart = mock(ShoppingCart.class, withSettings().useConstructor().defaultAnswer(CALLS_REAL_METHODS));
        when(shoppingCart.getCartAmount()).thenReturn(Amount.valueOf(5D));

        MinimumCartAmountBasedAmountDiscount discount = new MinimumCartAmountBasedAmountDiscount(Amount.valueOf(10D), Amount.valueOf(5D));
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
    public void givenShoppingCartAmount10_whenMinimumCartAmount10_thenApplyDiscountAmountOf5() {
        //given
        ShoppingCart shoppingCart = mock(ShoppingCart.class, withSettings().useConstructor().defaultAnswer(CALLS_REAL_METHODS));
        when(shoppingCart.getCartAmount()).thenReturn(Amount.valueOf(10D));

        MinimumCartAmountBasedAmountDiscount discount = new MinimumCartAmountBasedAmountDiscount(Amount.valueOf(10D), Amount.valueOf(5D));
        DiscountProvidedEvent event = mock(DiscountProvidedEvent.class);
        when(event.getDiscount()).thenReturn(discount);

        //when
        shoppingCart.discountProvided(event);
        shoppingCart.applyDiscounts();

        //then
        assertThat(shoppingCart.getDiscountMap()).hasSize(1);
        assertThat(shoppingCart.getTotalDiscount()).isEqualTo(Amount.valueOf(5D));
    }

    @Test
    public void givenShoppingCartAmount20_whenMultipleAmountBasedMinimumCartAmountDiscounts_thenApplyTheGreatestDiscount() {
        //given
        ShoppingCart shoppingCart = mock(ShoppingCart.class, withSettings().useConstructor().defaultAnswer(CALLS_REAL_METHODS));
        when(shoppingCart.getCartAmount()).thenReturn(Amount.valueOf(20D));

        MinimumCartAmountBasedAmountDiscount discount1 = new MinimumCartAmountBasedAmountDiscount(Amount.valueOf(10D), Amount.valueOf(5D));
        MinimumCartAmountBasedAmountDiscount discount2 = new MinimumCartAmountBasedAmountDiscount(Amount.valueOf(20D), Amount.valueOf(10D));

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
        assertThat(shoppingCart.getTotalDiscount()).isEqualTo(Amount.valueOf(10D));
    }


}
