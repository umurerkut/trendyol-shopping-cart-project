package com.trendyol.shoppingcart.core.domain.discount;

import com.trendyol.shoppingcart.core.domain.value.Amount;
import com.trendyol.shoppingcart.core.domain.value.DiscountName;
import com.trendyol.shoppingcart.core.domain.value.Quantity;
import com.trendyol.shoppingcart.core.exception.InvalidValueException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

//Mock discount: If more then or same number of minimumQuantityOfProducts items in cart, then apply discount amount of dismountAmount
public class ProductQuantityBasedMockDiscountTest {

    @Test
    public void givenMinimumQuantityOfProducts_whenMinimumQuantityOfProductsIsNull_thenThrowInvalidValueException() {
        //given
        Quantity minimumQuantityOfProducts = null;
        Amount discountAmount = Amount.valueOf(10D);
        assertThat(minimumQuantityOfProducts).isNull();

        //when
        Throwable throwable = catchThrowable(() -> {
            ProductQuantityBasedMockDiscount discount = new ProductQuantityBasedMockDiscount(minimumQuantityOfProducts, discountAmount);
        });

        //then
        assertThat(throwable)
                .isNotNull()
                .isInstanceOf(InvalidValueException.class)
                .hasMessage("Minimum quantity value of products in cart can not be null!");
    }

    @Test
    public void givenMinimumQuantityOfProducts_whenMinimumQuantityOfProductsIsZero_thenThrowInvalidValueException() {
        //given
        Quantity minimumQuantityOfProducts = Quantity.ofZero();
        Amount discountAmount = Amount.valueOf(10D);
        assertThat(minimumQuantityOfProducts).isNotNull();

        //when
        Throwable throwable = catchThrowable(() -> {
            ProductQuantityBasedMockDiscount discount = new ProductQuantityBasedMockDiscount(minimumQuantityOfProducts, discountAmount);
        });

        //then
        assertThat(throwable)
                .isNotNull()
                .isInstanceOf(InvalidValueException.class)
                .hasMessage("Minimum quantity value of products in cart can not be zero!");
    }

    @Test
    public void givenDiscountAmount_whenDiscountAmountIsNull_thenThrowInvalidValueException() {
        //given
        Quantity minimumQuantityOfProducts = Quantity.valueOf(1);
        Amount discountAmount = null;
        assertThat(discountAmount).isNull();

        //when
        Throwable throwable = catchThrowable(() -> {
            ProductQuantityBasedMockDiscount discount = new ProductQuantityBasedMockDiscount(minimumQuantityOfProducts, discountAmount);
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
        Quantity minimumQuantityOfProducts = Quantity.valueOf(1);
        Amount discountAmount = Amount.ofZero();
        assertThat(discountAmount).isNotNull();

        //when
        Throwable throwable = catchThrowable(() -> {
            ProductQuantityBasedMockDiscount discount = new ProductQuantityBasedMockDiscount(minimumQuantityOfProducts, discountAmount);
        });

        //then
        assertThat(throwable)
                .isNotNull()
                .isInstanceOf(InvalidValueException.class)
                .hasMessage("Amount of discount to be applied can not be zero!");
    }

    @Test
    public void givenValidDiscountAmountAndMinimumQuantityOfProducts_whenCreateProductQuantityBasedMockDiscount_thenReturnProductQuantityBasedMockDiscount() {
        //given
        Quantity minimumQuantityOfProducts = Quantity.valueOf(1);
        Amount discountAmount = Amount.valueOf(10D);
        assertThat(minimumQuantityOfProducts).isNotNull();
        assertThat(discountAmount).isNotNull();

        //when
        ProductQuantityBasedMockDiscount discount = new ProductQuantityBasedMockDiscount(minimumQuantityOfProducts, discountAmount);

        //then
        assertThat(discount).isNotNull();
        assertThat(discount.getDiscountName()).isEqualTo(DiscountName.valueOf("PRODUCT_QUANTITY_BASED_MOCK_DISCOUNT"));
        assertThat(discount.getCalculationStrategy().getDiscountAmount()).isEqualTo(discountAmount);
        assertThat(discount.getValidationStrategy().getMinimumQuantityOfProducts()).isEqualTo(minimumQuantityOfProducts);
    }

    @Test
    public void givenTwoProductQuantityBasedMockDiscounts_whenMinimumQuantityOfProductsGreater_thenTheDiscountAlsoGreater() {
        //given
        Amount discountAmount = Amount.valueOf(10D);
        Quantity quantity1 = Quantity.valueOf(2);
        Quantity quantity2 = Quantity.valueOf(1);
        ProductQuantityBasedMockDiscount discount1 = new ProductQuantityBasedMockDiscount(quantity1, discountAmount);
        ProductQuantityBasedMockDiscount discount2 = new ProductQuantityBasedMockDiscount(quantity2, discountAmount);

        //when
        assertThat(discountAmount).isNotNull();
        assertThat(quantity1).isNotNull();
        assertThat(quantity2).isNotNull();
        assertThat(quantity1.isGreaterThan(quantity2)).isTrue();

        //then
        assertThat(discount1.isGreaterThan(discount2)).isTrue();
    }

    @Test
    public void givenTwoProductQuantityBasedMockDiscounts_whenMinimumQuantityOfProductsSameButDiscountAmountGreater_thenTheDiscountAlsoGreater() {
        //given
        Amount discountAmount1 = Amount.valueOf(20D);
        Amount discountAmount2 = Amount.valueOf(10D);
        Quantity quantity = Quantity.valueOf(1);
        ProductQuantityBasedMockDiscount discount1 = new ProductQuantityBasedMockDiscount(quantity, discountAmount1);
        ProductQuantityBasedMockDiscount discount2 = new ProductQuantityBasedMockDiscount(quantity, discountAmount2);

        //when
        assertThat(discountAmount1).isNotNull();
        assertThat(discountAmount2).isNotNull();
        assertThat(quantity).isNotNull();
        assertThat(discountAmount1.isGreaterThan(discountAmount2)).isTrue();

        //then
        assertThat(discount1.isGreaterThan(discount2)).isTrue();
    }

}
