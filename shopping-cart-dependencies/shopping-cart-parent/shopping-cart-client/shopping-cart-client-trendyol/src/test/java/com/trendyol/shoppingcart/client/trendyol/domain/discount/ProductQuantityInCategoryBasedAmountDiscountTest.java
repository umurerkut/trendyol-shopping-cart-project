package com.trendyol.shoppingcart.client.trendyol.domain.discount;

import com.trendyol.shoppingcart.core.discountprovider.listener.DiscountProvidedEvent;
import com.trendyol.shoppingcart.core.domain.Category;
import com.trendyol.shoppingcart.core.domain.ShoppingCart;
import com.trendyol.shoppingcart.core.domain.value.Amount;
import com.trendyol.shoppingcart.core.domain.value.DiscountName;
import com.trendyol.shoppingcart.core.domain.value.Quantity;
import com.trendyol.shoppingcart.core.domain.value.Title;
import com.trendyol.shoppingcart.core.exception.InvalidValueException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.*;

//Discount: If more than minimumProductQuantityInCategory products in specific category exists in cart, then apply discount amount of discountAmount
public class ProductQuantityInCategoryBasedAmountDiscountTest {

    @Test
    public void givenProduct_whenCategoryIsNull_thenThrowInvalidValueException() {
        //given
        Category category = null;
        Assertions.assertThat(category).isNull();
        Quantity minimumProductQuantityInCategory = Quantity.valueOf(1);
        assertThat(minimumProductQuantityInCategory).isNotNull();
        Amount discountAmount = Amount.valueOf(10D);
        assertThat(discountAmount).isNotNull();


        //when
        Throwable throwable = catchThrowable(() -> {
            ProductQuantityInCategoryBasedAmountDiscount discount = new ProductQuantityInCategoryBasedAmountDiscount(category, minimumProductQuantityInCategory, discountAmount);
        });

        //then
        assertThat(throwable)
                .isNotNull()
                .isInstanceOf(InvalidValueException.class)
                .hasMessage("Given category can not be null!");
    }

    @Test
    public void givenProduct_whenMinimumProductQuantityInCategoryIsNull_thenThrowInvalidValueException() {
        //given
        Category category = new Category(Title.valueOf("Category"));
        Assertions.assertThat(category).isNotNull();
        Quantity minimumProductQuantityInCategory = null;
        assertThat(minimumProductQuantityInCategory).isNull();
        Amount discountAmount = Amount.valueOf(10D);
        assertThat(discountAmount).isNotNull();

        //when
        Throwable throwable = catchThrowable(() -> {
            ProductQuantityInCategoryBasedAmountDiscount discount = new ProductQuantityInCategoryBasedAmountDiscount(category, minimumProductQuantityInCategory, discountAmount);
        });

        //then
        assertThat(throwable)
                .isNotNull()
                .isInstanceOf(InvalidValueException.class)
                .hasMessage("Minimum product quantity in category can not be null!");
    }

    @Test
    public void givenDiscountAmount_whenDiscountAmountIsNull_thenThrowInvalidValueException() {
        //given
        Category category = new Category(Title.valueOf("Category"));
        Assertions.assertThat(category).isNotNull();
        Quantity minimumProductQuantityInCategory = Quantity.valueOf(1);
        assertThat(minimumProductQuantityInCategory).isNotNull();
        Amount discountAmount = null;
        assertThat(discountAmount).isNull();

        //when
        Throwable throwable = catchThrowable(() -> {
            ProductQuantityInCategoryBasedAmountDiscount discount = new ProductQuantityInCategoryBasedAmountDiscount(category, minimumProductQuantityInCategory, discountAmount);
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
        Category category = new Category(Title.valueOf("Category"));
        Assertions.assertThat(category).isNotNull();
        Quantity minimumProductQuantityInCategory = Quantity.valueOf(1);
        assertThat(minimumProductQuantityInCategory).isNotNull();
        Amount discountAmount = Amount.ofZero();
        assertThat(discountAmount).isNotNull();

        //when
        Throwable throwable = catchThrowable(() -> {
            ProductQuantityInCategoryBasedAmountDiscount discount = new ProductQuantityInCategoryBasedAmountDiscount(category, minimumProductQuantityInCategory, discountAmount);
        });

        //then
        assertThat(throwable)
                .isNotNull()
                .isInstanceOf(InvalidValueException.class)
                .hasMessage("Amount of discount to be applied can not be zero!");
    }

    @Test
    public void givenValidDiscountAmountAndProduct_whenCreateProductQuantityInCategoryBasedAmountDiscount_thenReturnProductQuantityTypeMockDiscount() {
        //given
        Category category = new Category(Title.valueOf("Category"));
        Assertions.assertThat(category).isNotNull();
        Quantity minimumProductQuantityInCategory = Quantity.valueOf(1);
        assertThat(minimumProductQuantityInCategory).isNotNull();
        Amount discountAmount = Amount.valueOf(10D);
        assertThat(discountAmount).isNotNull();

        //when
        ProductQuantityInCategoryBasedAmountDiscount discount = new ProductQuantityInCategoryBasedAmountDiscount(category, minimumProductQuantityInCategory, discountAmount);

        //then
        assertThat(discount).isNotNull();
        String discountNameText = (category.getTitle().getValue() + "_CATEGORY_TYPE_BASED_DISCOUNT").replace(" ", "_").toUpperCase();
        assertThat(discount.getDiscountName()).isEqualTo(DiscountName.valueOf(discountNameText));
        assertThat(discount.getCalculationStrategy().getDiscountAmount()).isEqualTo(discountAmount);
        assertThat(discount.getValidationStrategy().getCategory()).isEqualTo(category);
    }

    @Test
    public void givenTwoProductQuantityBasedMockDiscounts_whenMinimumProductQuantityInCategoryGreater_thenTheDiscountAlsoGreater() {
        //given
        Category category1 = new Category(Title.valueOf("Category1"));
        Assertions.assertThat(category1).isNotNull();
        Quantity minimumProductQuantityInCategory1 = Quantity.valueOf(2);
        assertThat(minimumProductQuantityInCategory1).isNotNull();
        Amount discountAmount1 = Amount.valueOf(10D);
        assertThat(discountAmount1).isNotNull();

        Category category2 = new Category(Title.valueOf("Category2"));
        Assertions.assertThat(category2).isNotNull();
        Quantity minimumProductQuantityInCategory2 = Quantity.valueOf(1);
        assertThat(minimumProductQuantityInCategory2).isNotNull();
        Amount discountAmount2 = Amount.valueOf(10D);
        assertThat(discountAmount2).isNotNull();


        ProductQuantityInCategoryBasedAmountDiscount discount1 = new ProductQuantityInCategoryBasedAmountDiscount(category1, minimumProductQuantityInCategory1, discountAmount1);
        ProductQuantityInCategoryBasedAmountDiscount discount2 = new ProductQuantityInCategoryBasedAmountDiscount(category2, minimumProductQuantityInCategory2, discountAmount2);

        //when
        assertThat(minimumProductQuantityInCategory1.isGreaterThan(minimumProductQuantityInCategory2)).isTrue();

        //then
        assertThat(discount1.isGreaterThan(discount2)).isTrue();
    }

    @Test
    public void givenTwoProductQuantityBasedMockDiscounts_whenMinimumProductQuantityInCategorySameButDiscountAmountGreater_thenTheDiscountAlsoGreater() {
        //given
        Category category1 = new Category(Title.valueOf("Category1"));
        Assertions.assertThat(category1).isNotNull();
        Quantity minimumProductQuantityInCategory1 = Quantity.valueOf(1);
        assertThat(minimumProductQuantityInCategory1).isNotNull();
        Amount discountAmount1 = Amount.valueOf(15D);
        assertThat(discountAmount1).isNotNull();

        Category category2 = new Category(Title.valueOf("Category2"));
        Assertions.assertThat(category2).isNotNull();
        Quantity minimumProductQuantityInCategory2 = Quantity.valueOf(1);
        assertThat(minimumProductQuantityInCategory2).isNotNull();
        Amount discountAmount2 = Amount.valueOf(10D);
        assertThat(discountAmount2).isNotNull();


        ProductQuantityInCategoryBasedAmountDiscount discount1 = new ProductQuantityInCategoryBasedAmountDiscount(category1, minimumProductQuantityInCategory1, discountAmount1);
        ProductQuantityInCategoryBasedAmountDiscount discount2 = new ProductQuantityInCategoryBasedAmountDiscount(category2, minimumProductQuantityInCategory2, discountAmount2);

        //when
        assertThat(discountAmount1.isGreaterThan(discountAmount2)).isTrue();

        //then
        assertThat(discount1.isGreaterThan(discount2)).isTrue();
    }

    @Test
    public void givenCategory_whenQuantityOfProductsInCategoryIsZero_thenDoNotApplyDiscount() {
        //given
        Category category = new Category(Title.valueOf("Category"));
        Assertions.assertThat(category).isNotNull();
        Quantity minimumProductQuantityInCategory = Quantity.valueOf(1);
        assertThat(minimumProductQuantityInCategory).isNotNull();
        Amount discountAmount = Amount.valueOf(10D);
        assertThat(discountAmount).isNotNull();

        ShoppingCart shoppingCart = mock(ShoppingCart.class, withSettings().useConstructor().defaultAnswer(CALLS_REAL_METHODS));
        when(shoppingCart.getQuantityOfProductsBelongsToCategoryInCart(category)).thenReturn(Quantity.ofZero());

        ProductQuantityInCategoryBasedAmountDiscount discount = new ProductQuantityInCategoryBasedAmountDiscount(category, minimumProductQuantityInCategory, discountAmount);
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
    public void givenCategory_whenQuantityOfProductsInCategoryIsLessThan2_thenDoNotApplyDiscount() {
        //given
        Category category = new Category(Title.valueOf("Category"));
        Assertions.assertThat(category).isNotNull();
        Quantity minimumProductQuantityInCategory = Quantity.valueOf(2);
        assertThat(minimumProductQuantityInCategory).isNotNull();
        Amount discountAmount = Amount.valueOf(10D);
        assertThat(discountAmount).isNotNull();

        ShoppingCart shoppingCart = mock(ShoppingCart.class, withSettings().useConstructor().defaultAnswer(CALLS_REAL_METHODS));
        when(shoppingCart.getQuantityOfProductsBelongsToCategoryInCart(category)).thenReturn(Quantity.valueOf(1));

        ProductQuantityInCategoryBasedAmountDiscount discount = new ProductQuantityInCategoryBasedAmountDiscount(category, minimumProductQuantityInCategory, discountAmount);
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
    public void givenCategory_whenQuantityOfProductsInCategoryIsGreaterOrEquals1_thenApplyDiscountAmountOf10() {
        //given
        Category category = new Category(Title.valueOf("Category"));
        Assertions.assertThat(category).isNotNull();
        Quantity minimumProductQuantityInCategory = Quantity.valueOf(1);
        assertThat(minimumProductQuantityInCategory).isNotNull();
        Amount discountAmount = Amount.valueOf(10D);
        assertThat(discountAmount).isNotNull();

        ShoppingCart shoppingCart = mock(ShoppingCart.class, withSettings().useConstructor().defaultAnswer(CALLS_REAL_METHODS));
        when(shoppingCart.getQuantityOfProductsBelongsToCategoryInCart(category)).thenReturn(Quantity.valueOf(1));

        ProductQuantityInCategoryBasedAmountDiscount discount = new ProductQuantityInCategoryBasedAmountDiscount(category, minimumProductQuantityInCategory, discountAmount);
        DiscountProvidedEvent event = mock(DiscountProvidedEvent.class);
        when(event.getDiscount()).thenReturn(discount);

        //when
        shoppingCart.discountProvided(event);
        shoppingCart.applyDiscounts();

        //then
        assertThat(shoppingCart.getDiscountMap()).hasSize(1);
        assertThat(shoppingCart.getTotalDiscount()).isEqualTo(Amount.valueOf(10D));
    }

    @Test
    public void givenMultipleDifferentCategories_whenMultipleProductQuantityInCategoryBasedAmountDiscounts_thenApplyTheGreatestDiscountPerCategory() {
        //given
        Category category1 = new Category(Title.valueOf("Category1"));
        Assertions.assertThat(category1).isNotNull();
        Quantity minimumProductQuantityInCategory1 = Quantity.valueOf(1);
        assertThat(minimumProductQuantityInCategory1).isNotNull();
        Amount discountAmount1 = Amount.valueOf(15D);
        assertThat(discountAmount1).isNotNull();

        Category category2 = new Category(Title.valueOf("Category2"));
        Assertions.assertThat(category2).isNotNull();
        Quantity minimumProductQuantityInCategory2 = Quantity.valueOf(1);
        assertThat(minimumProductQuantityInCategory2).isNotNull();
        Amount discountAmount2 = Amount.valueOf(10D);
        assertThat(discountAmount2).isNotNull();

        ProductQuantityInCategoryBasedAmountDiscount discount1 = new ProductQuantityInCategoryBasedAmountDiscount(category1, minimumProductQuantityInCategory1, discountAmount1);
        ProductQuantityInCategoryBasedAmountDiscount discount2 = new ProductQuantityInCategoryBasedAmountDiscount(category2, minimumProductQuantityInCategory2, discountAmount2);

        ShoppingCart shoppingCart = mock(ShoppingCart.class, withSettings().useConstructor().defaultAnswer(CALLS_REAL_METHODS));
        when(shoppingCart.getQuantityOfProductsBelongsToCategoryInCart(category1)).thenReturn(Quantity.valueOf(2));
        when(shoppingCart.getQuantityOfProductsBelongsToCategoryInCart(category2)).thenReturn(Quantity.valueOf(3));

        DiscountProvidedEvent event1 = mock(DiscountProvidedEvent.class);
        when(event1.getDiscount()).thenReturn(discount1);
        DiscountProvidedEvent event2 = mock(DiscountProvidedEvent.class);
        when(event2.getDiscount()).thenReturn(discount2);

        //when
        shoppingCart.discountProvided(event1);
        shoppingCart.discountProvided(event2);
        shoppingCart.applyDiscounts();

        //then
        assertThat(shoppingCart.getDiscountMap()).hasSize(2);
        assertThat(shoppingCart.getTotalDiscount()).isEqualTo(Amount.valueOf(25D));
    }

    @Test
    public void givenMultipleSameCategories_whenMultipleProductQuantityInCategoryBasedAmountDiscounts_thenApplyTheGreatestDiscountForCategory() {
        //given
        Category category = new Category(Title.valueOf("Category"));
        Assertions.assertThat(category).isNotNull();

        Quantity minimumProductQuantityInCategory1 = Quantity.valueOf(1);
        assertThat(minimumProductQuantityInCategory1).isNotNull();
        Amount discountAmount1 = Amount.valueOf(15D);
        assertThat(discountAmount1).isNotNull();

        Quantity minimumProductQuantityInCategory2 = Quantity.valueOf(1);
        assertThat(minimumProductQuantityInCategory2).isNotNull();
        Amount discountAmount2 = Amount.valueOf(10D);
        assertThat(discountAmount2).isNotNull();

        ProductQuantityInCategoryBasedAmountDiscount discount1 = new ProductQuantityInCategoryBasedAmountDiscount(category, minimumProductQuantityInCategory1, discountAmount1);
        ProductQuantityInCategoryBasedAmountDiscount discount2 = new ProductQuantityInCategoryBasedAmountDiscount(category, minimumProductQuantityInCategory2, discountAmount2);

        ShoppingCart shoppingCart = mock(ShoppingCart.class, withSettings().useConstructor().defaultAnswer(CALLS_REAL_METHODS));
        when(shoppingCart.getQuantityOfProductsBelongsToCategoryInCart(category)).thenReturn(Quantity.valueOf(2));

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
        assertThat(shoppingCart.getTotalDiscount()).isEqualTo(Amount.valueOf(15D));
    }
}
