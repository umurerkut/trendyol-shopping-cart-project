package com.trendyol.shoppingcart.core.domain.discount.validation;

import com.trendyol.shoppingcart.core.domain.ShoppingCart;
import com.trendyol.shoppingcart.core.domain.value.Quantity;
import com.trendyol.shoppingcart.core.exception.InvalidValueException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.*;

public class ProductQuantityBasedMockDiscountValidationStrategyTest {

    @Test
    public void givenMinimumQuantityOfProducts_whenMinimumQuantityOfProductsIsNull_thenThrowInvalidValueException() {
        //given
        Quantity minimumQuantityOfProducts = null;
        assertThat(minimumQuantityOfProducts).isNull();

        //when
        Throwable throwable = catchThrowable(() -> {
            ProductQuantityBasedMockDiscountValidationStrategy validationStrategy = new ProductQuantityBasedMockDiscountValidationStrategy(minimumQuantityOfProducts);
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
        assertThat(minimumQuantityOfProducts).isNotNull();

        //when
        Throwable throwable = catchThrowable(() -> {
            ProductQuantityBasedMockDiscountValidationStrategy validationStrategy = new ProductQuantityBasedMockDiscountValidationStrategy(minimumQuantityOfProducts);
        });

        //then
        assertThat(throwable)
                .isNotNull()
                .isInstanceOf(InvalidValueException.class)
                .hasMessage("Minimum quantity value of products in cart can not be zero!");
    }

    @Test
    public void givenValidMinimumQuantityOfProducts_whenCreateProductQuantityBasedMockDiscountValidationStrategy_thenReturnProductQuantityBasedMockDiscountValidationStrategy() {
        //given
        Quantity minimumQuantityOfProducts = Quantity.valueOf(1);
        assertThat(minimumQuantityOfProducts).isNotNull();

        //when
        ProductQuantityBasedMockDiscountValidationStrategy validationStrategy = new ProductQuantityBasedMockDiscountValidationStrategy(minimumQuantityOfProducts);

        //then
        assertThat(validationStrategy).isNotNull();
        assertThat(validationStrategy.getMinimumQuantityOfProducts()).isEqualTo(minimumQuantityOfProducts);
    }

    @Test
    public void givenProductQuantityBasedMockDiscountValidationStrategy_whenIsValid_thenReturnTrue() {
        //given
        Quantity minimumQuantityOfProducts = Quantity.valueOf(5);
        assertThat(minimumQuantityOfProducts).isNotNull();
        ProductQuantityBasedMockDiscountValidationStrategy validationStrategy = new ProductQuantityBasedMockDiscountValidationStrategy(minimumQuantityOfProducts);

        //when
        ShoppingCart shoppingCart = mock(ShoppingCart.class);
        when(shoppingCart.getTotalQuantityOfProductsInCart()).thenReturn(Quantity.valueOf(6));
        Boolean result = validationStrategy.isValid(shoppingCart);

        verify(shoppingCart, times(1)).getTotalQuantityOfProductsInCart();
        verifyNoMoreInteractions(shoppingCart);

        //then
        assertThat(result).isTrue();
    }

    @Test
    public void givenProductQuantityBasedMockDiscountValidationStrategy_whenIsNotValid_thenReturnFalse() {
        //given
        Quantity minimumQuantityOfProducts = Quantity.valueOf(5);
        assertThat(minimumQuantityOfProducts).isNotNull();
        ProductQuantityBasedMockDiscountValidationStrategy validationStrategy = new ProductQuantityBasedMockDiscountValidationStrategy(minimumQuantityOfProducts);

        //when
        ShoppingCart shoppingCart = mock(ShoppingCart.class);
        when(shoppingCart.getTotalQuantityOfProductsInCart()).thenReturn(Quantity.valueOf(1));
        Boolean result = validationStrategy.isValid(shoppingCart);

        verify(shoppingCart, times(1)).getTotalQuantityOfProductsInCart();
        verifyNoMoreInteractions(shoppingCart);

        //then
        assertThat(result).isFalse();
    }

}
