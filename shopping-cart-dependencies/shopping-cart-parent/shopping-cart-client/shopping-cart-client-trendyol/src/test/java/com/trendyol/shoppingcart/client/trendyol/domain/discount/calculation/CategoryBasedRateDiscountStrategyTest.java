package com.trendyol.shoppingcart.client.trendyol.domain.discount.calculation;

import com.trendyol.shoppingcart.client.trendyol.domain.value.Rate;
import com.trendyol.shoppingcart.core.domain.Category;
import com.trendyol.shoppingcart.core.domain.ShoppingCart;
import com.trendyol.shoppingcart.core.domain.value.Amount;
import com.trendyol.shoppingcart.core.domain.value.Title;
import com.trendyol.shoppingcart.core.exception.InvalidValueException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CategoryBasedRateDiscountStrategyTest {

    @Test
    public void givenDiscountRate_whenDiscountRateIsNull_thenThrowInvalidValueException() {
        //given
        Category category = new Category(Title.valueOf("Category"));
        Rate discountRate = null;
        assertThat(category).isNotNull();
        assertThat(discountRate).isNull();

        //when
        Throwable throwable = catchThrowable(() -> {
            CategoryBasedRateDiscountStrategy calculationStrategy = new CategoryBasedRateDiscountStrategy(category, discountRate);
        });

        //then
        assertThat(throwable)
                .isNotNull()
                .isInstanceOf(InvalidValueException.class)
                .hasMessage("Rate of discount to be applied can not be null!");
    }

    @Test
    public void givenDiscountRate_whenCategoryIsNull_thenThrowInvalidValueException() {
        //given
        Category category = null;
        Rate discountRate = Rate.valueOf(10D);
        assertThat(category).isNull();
        assertThat(discountRate).isNotNull();

        //when
        Throwable throwable = catchThrowable(() -> {
            CategoryBasedRateDiscountStrategy calculationStrategy = new CategoryBasedRateDiscountStrategy(category, discountRate);
        });

        //then
        assertThat(throwable)
                .isNotNull()
                .isInstanceOf(InvalidValueException.class)
                .hasMessage("Category of discount to be applied can not be null!");
    }

    @Test
    public void givenValidDiscountRate_whenCreateCategoryBasedRateDiscountStrategy_thenReturnCategoryBasedRateDiscountStrategy() {
        //given
        Category category = new Category(Title.valueOf("Category"));
        Rate discountRate = Rate.valueOf(10D);
        assertThat(discountRate).isNotNull();

        //when
        CategoryBasedRateDiscountStrategy calculationStrategy = new CategoryBasedRateDiscountStrategy(category, discountRate);

        //then
        assertThat(calculationStrategy).isNotNull();
        assertThat(calculationStrategy.getDiscountRate()).isEqualTo(discountRate);
    }

    @Test
    public void givenCategoryBasedRateDiscountStrategy_whenCalculateDiscountRate_thenReturnCalculatedDiscountRate() {
        //given
        Category category = new Category(Title.valueOf("Category"));
        Rate discountRate = Rate.valueOf(10D);
        assertThat(discountRate).isNotNull();
        CategoryBasedRateDiscountStrategy calculationStrategy = new CategoryBasedRateDiscountStrategy(category, discountRate);

        //when
        ShoppingCart shoppingCart = mock(ShoppingCart.class);
        when(shoppingCart.getTotalPriceOfProductsBelongToCategoryInCart(category)).thenReturn(Amount.valueOf(100D));
        Amount calculatedDiscountAmount = calculationStrategy.calculateDiscountAmount(shoppingCart);

        //then
        assertThat(calculatedDiscountAmount)
                .isEqualTo(shoppingCart.getTotalPriceOfProductsBelongToCategoryInCart(category).multiply(discountRate.doubleValue()).divide(100D))
                .isEqualTo(Amount.valueOf(10D));
    }
}
