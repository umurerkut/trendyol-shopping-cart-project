package com.trendyol.shoppingcart.client.trendyol.domain.discount.validation;

import com.trendyol.shoppingcart.core.domain.Category;
import com.trendyol.shoppingcart.core.domain.ShoppingCart;
import com.trendyol.shoppingcart.core.domain.value.Quantity;
import com.trendyol.shoppingcart.core.domain.value.Title;
import com.trendyol.shoppingcart.core.exception.InvalidValueException;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.*;

public class ProductQuantityInCategoryValidationStrategyTest {

    @Test
    public void givenCategory_whenCategoryIsNull_thenThrowInvalidValueException() {
        //given
        Category category = null;
        assertThat(category).isNull();
        Quantity minimumProductQuantityInCategory = Quantity.valueOf(1);
        assertThat(minimumProductQuantityInCategory).isNotNull();

        //when
        Throwable throwable = catchThrowable(() -> {
            ProductQuantityInCategoryValidationStrategy validationStrategy = new ProductQuantityInCategoryValidationStrategy(category, minimumProductQuantityInCategory);
        });

        //then
        assertThat(throwable)
                .isNotNull()
                .isInstanceOf(InvalidValueException.class)
                .hasMessage("Given category can not be null!");
    }

    @Test
    public void givenCategory_whenMinimumProductQuantityInCategoryIsNull_thenThrowInvalidValueException() {
        //given
        Category category = new Category(Title.valueOf("Category"));
        assertThat(category).isNotNull();
        Quantity minimumProductQuantityInCategory = null;
        assertThat(minimumProductQuantityInCategory).isNull();

        //when
        Throwable throwable = catchThrowable(() -> {
            ProductQuantityInCategoryValidationStrategy validationStrategy = new ProductQuantityInCategoryValidationStrategy(category, minimumProductQuantityInCategory);
        });

        //then
        assertThat(throwable)
                .isNotNull()
                .isInstanceOf(InvalidValueException.class)
                .hasMessage("Minimum product quantity in category can not be null!");
    }

    @Test
    public void givenValidCategory_whenCreateProductQuantityInCategoryValidationStrategy_thenReturnProductQuantityInCategoryValidationStrategy() {
        //given
        Category category = new Category(Title.valueOf("Category"));
        assertThat(category).isNotNull();
        Quantity minimumProductQuantityInCategory = Quantity.valueOf(1);
        assertThat(minimumProductQuantityInCategory).isNotNull();

        //when
        ProductQuantityInCategoryValidationStrategy validationStrategy = new ProductQuantityInCategoryValidationStrategy(category, minimumProductQuantityInCategory);

        //then
        assertThat(validationStrategy).isNotNull();
        assertThat(validationStrategy.getCategory()).isEqualTo(category);
    }

    @Test
    public void givenProductQuantityInCategoryValidationStrategy_whenIsValid_thenReturnTrue() {
        //given
        Category category = new Category(Title.valueOf("Category"));
        assertThat(category).isNotNull();
        Quantity minimumProductQuantityInCategory = Quantity.valueOf(1);
        assertThat(minimumProductQuantityInCategory).isNotNull();

        ProductQuantityInCategoryValidationStrategy validationStrategy = new ProductQuantityInCategoryValidationStrategy(category, minimumProductQuantityInCategory);

        //when
        ShoppingCart shoppingCart = mock(ShoppingCart.class);
        when(shoppingCart.getQuantityOfProductsBelongsToCategoryInCart(category)).thenReturn(Quantity.valueOf(1));
        boolean result = validationStrategy.isValid(shoppingCart);

        verify(shoppingCart).getQuantityOfProductsBelongsToCategoryInCart(ArgumentMatchers.eq(category));
        verifyNoMoreInteractions(shoppingCart);

        //then
        assertThat(result).isTrue();
    }

    @Test
    public void givenProductQuantityInCategoryValidationStrategy_whenIsNotValid_thenReturnFalse() {
        //given
        Category category = new Category(Title.valueOf("Category"));
        assertThat(category).isNotNull();
        Quantity minimumProductQuantityInCategory = Quantity.valueOf(1);
        assertThat(minimumProductQuantityInCategory).isNotNull();

        ProductQuantityInCategoryValidationStrategy validationStrategy = new ProductQuantityInCategoryValidationStrategy(category, minimumProductQuantityInCategory);

        //when
        ShoppingCart shoppingCart = mock(ShoppingCart.class);
        when(shoppingCart.getQuantityOfProductsBelongsToCategoryInCart(category)).thenReturn(Quantity.ofZero());
        boolean result = validationStrategy.isValid(shoppingCart);

        verify(shoppingCart).getQuantityOfProductsBelongsToCategoryInCart(ArgumentMatchers.eq(category));
        verifyNoMoreInteractions(shoppingCart);

        //then
        assertThat(result).isFalse();
    }
}
