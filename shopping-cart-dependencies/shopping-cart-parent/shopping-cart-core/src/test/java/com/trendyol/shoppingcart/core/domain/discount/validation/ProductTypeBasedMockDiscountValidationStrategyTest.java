package com.trendyol.shoppingcart.core.domain.discount.validation;

import com.trendyol.shoppingcart.core.domain.Category;
import com.trendyol.shoppingcart.core.domain.Product;
import com.trendyol.shoppingcart.core.domain.ShoppingCart;
import com.trendyol.shoppingcart.core.domain.value.Amount;
import com.trendyol.shoppingcart.core.domain.value.Quantity;
import com.trendyol.shoppingcart.core.domain.value.Title;
import com.trendyol.shoppingcart.core.exception.InvalidValueException;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.*;

public class ProductTypeBasedMockDiscountValidationStrategyTest {

    @Test
    public void givenProduct_whenProductsIsNull_thenThrowInvalidValueException() {
        //given
        Product product = null;
        assertThat(product).isNull();

        //when
        Throwable throwable = catchThrowable(() -> {
            ProductTypeBasedMockDiscountValidationStrategy validationStrategy = new ProductTypeBasedMockDiscountValidationStrategy(product);
        });

        //then
        assertThat(throwable)
                .isNotNull()
                .isInstanceOf(InvalidValueException.class)
                .hasMessage("Given product can not be null");
    }

    @Test
    public void givenValidProduct_whenCreateProductTypeBasedMockDiscountValidationStrategy_thenReturnProductTypeBasedMockDiscountValidationStrategy() {
        //given
        Product product = new Product(Title.valueOf("Product"), Amount.valueOf(10D), new Category(Title.valueOf("Category")));
        assertThat(product).isNotNull();

        //when
        ProductTypeBasedMockDiscountValidationStrategy validationStrategy = new ProductTypeBasedMockDiscountValidationStrategy(product);

        //then
        assertThat(validationStrategy).isNotNull();
        assertThat(validationStrategy.getProduct()).isEqualTo(product);
    }

    @Test
    public void givenProductTypeBasedMockDiscountValidationStrategy_whenIsValid_thenReturnTrue() {
        //given
        Product product = new Product(Title.valueOf("Product"), Amount.valueOf(10D), new Category(Title.valueOf("Category")));
        assertThat(product).isNotNull();
        ProductTypeBasedMockDiscountValidationStrategy validationStrategy = new ProductTypeBasedMockDiscountValidationStrategy(product);

        //when
        ShoppingCart shoppingCart = mock(ShoppingCart.class);
        when(shoppingCart.getQuantityOfProductInCart(product)).thenReturn(Quantity.valueOf(1));
        Boolean result = validationStrategy.isValid(shoppingCart);

        verify(shoppingCart).getQuantityOfProductInCart(ArgumentMatchers.eq(product));
        verify(shoppingCart, times(1)).getQuantityOfProductInCart(product);
        verifyNoMoreInteractions(shoppingCart);

        //then
        assertThat(result).isTrue();
    }

    @Test
    public void givenProductTypeBasedMockDiscountValidationStrategywhenIsNotValid_thenReturnFalse() {
        //given
        Product product = new Product(Title.valueOf("Product"), Amount.valueOf(10D), new Category(Title.valueOf("Category")));
        assertThat(product).isNotNull();
        ProductTypeBasedMockDiscountValidationStrategy validationStrategy = new ProductTypeBasedMockDiscountValidationStrategy(product);

        //when
        ShoppingCart shoppingCart = mock(ShoppingCart.class);
        when(shoppingCart.getQuantityOfProductInCart(product)).thenReturn(Quantity.ofZero());
        Boolean result = validationStrategy.isValid(shoppingCart);

        verify(shoppingCart).getQuantityOfProductInCart(ArgumentMatchers.eq(product));
        verify(shoppingCart, times(1)).getQuantityOfProductInCart(product);
        verifyNoMoreInteractions(shoppingCart);

        //then
        assertThat(result).isFalse();
    }
}
