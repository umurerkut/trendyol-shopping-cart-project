package com.trendyol.shoppingcart.client.trendyol.domain.discount;

import com.trendyol.shoppingcart.client.trendyol.domain.value.Rate;
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

//Discount: If more than minimumProductQuantityInCategory products in specific category exists in cart, then apply discount rate of discountRate
public class ProductQuantityInCategoryBasedRateDiscountTest {

    @Test
    public void givenProduct_whenCategoryIsNull_thenThrowInvalidValueException() {
        //given
        Category category = null;
        Assertions.assertThat(category).isNull();
        Quantity minimumProductQuantityInCategory = Quantity.valueOf(1);
        assertThat(minimumProductQuantityInCategory).isNotNull();
        Rate discountRate = Rate.valueOf(10D);
        assertThat(discountRate).isNotNull();


        //when
        Throwable throwable = catchThrowable(() -> {
            ProductQuantityInCategoryBasedRateDiscount discount = new ProductQuantityInCategoryBasedRateDiscount(category, minimumProductQuantityInCategory, discountRate);
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
        Rate discountRate = Rate.valueOf(10D);
        assertThat(discountRate).isNotNull();

        //when
        Throwable throwable = catchThrowable(() -> {
            ProductQuantityInCategoryBasedRateDiscount discount = new ProductQuantityInCategoryBasedRateDiscount(category, minimumProductQuantityInCategory, discountRate);
        });

        //then
        assertThat(throwable)
                .isNotNull()
                .isInstanceOf(InvalidValueException.class)
                .hasMessage("Minimum product quantity in category can not be null!");
    }

    @Test
    public void givenDiscountRate_whenDiscountRateIsNull_thenThrowInvalidValueException() {
        //given
        Category category = new Category(Title.valueOf("Category"));
        Assertions.assertThat(category).isNotNull();
        Quantity minimumProductQuantityInCategory = Quantity.valueOf(1);
        assertThat(minimumProductQuantityInCategory).isNotNull();
        Rate discountRate = null;
        assertThat(discountRate).isNull();

        //when
        Throwable throwable = catchThrowable(() -> {
            ProductQuantityInCategoryBasedRateDiscount discount = new ProductQuantityInCategoryBasedRateDiscount(category, minimumProductQuantityInCategory, discountRate);
        });

        //then
        assertThat(throwable)
                .isNotNull()
                .isInstanceOf(InvalidValueException.class)
                .hasMessage("Rate of discount to be applied can not be null!");
    }

    @Test
    public void givenValidDiscountRateAndProduct_whenCreateProductQuantityInCategoryBasedRateDiscount_thenReturnProductQuantityTypeMockDiscount() {
        //given
        Category category = new Category(Title.valueOf("Category"));
        Assertions.assertThat(category).isNotNull();
        Quantity minimumProductQuantityInCategory = Quantity.valueOf(1);
        assertThat(minimumProductQuantityInCategory).isNotNull();
        Rate discountRate = Rate.valueOf(10D);
        assertThat(discountRate).isNotNull();

        //when
        ProductQuantityInCategoryBasedRateDiscount discount = new ProductQuantityInCategoryBasedRateDiscount(category, minimumProductQuantityInCategory, discountRate);

        //then
        assertThat(discount).isNotNull();
        String discountNameText = (category.getTitle().getValue() + "_CATEGORY_TYPE_BASED_DISCOUNT").replace(" ", "_").toUpperCase();
        assertThat(discount.getDiscountName()).isEqualTo(DiscountName.valueOf(discountNameText));
        assertThat(discount.getCalculationStrategy().getDiscountRate()).isEqualTo(discountRate);
        assertThat(discount.getValidationStrategy().getCategory()).isEqualTo(category);
    }

    @Test
    public void givenTwoProductQuantityBasedMockDiscounts_whenMinimumProductQuantityInCategoryGreater_thenTheDiscountAlsoGreater() {
        //given
        Category category1 = new Category(Title.valueOf("Category1"));
        Assertions.assertThat(category1).isNotNull();
        Quantity minimumProductQuantityInCategory1 = Quantity.valueOf(2);
        assertThat(minimumProductQuantityInCategory1).isNotNull();
        Rate discountRate1 = Rate.valueOf(10D);
        assertThat(discountRate1).isNotNull();

        Category category2 = new Category(Title.valueOf("Category2"));
        Assertions.assertThat(category2).isNotNull();
        Quantity minimumProductQuantityInCategory2 = Quantity.valueOf(1);
        assertThat(minimumProductQuantityInCategory2).isNotNull();
        Rate discountRate2 = Rate.valueOf(10D);
        assertThat(discountRate2).isNotNull();


        ProductQuantityInCategoryBasedRateDiscount discount1 = new ProductQuantityInCategoryBasedRateDiscount(category1, minimumProductQuantityInCategory1, discountRate1);
        ProductQuantityInCategoryBasedRateDiscount discount2 = new ProductQuantityInCategoryBasedRateDiscount(category2, minimumProductQuantityInCategory2, discountRate2);

        //when
        assertThat(minimumProductQuantityInCategory1.isGreaterThan(minimumProductQuantityInCategory2)).isTrue();

        //then
        assertThat(discount1.isGreaterThan(discount2)).isTrue();
    }

    @Test
    public void givenTwoProductQuantityBasedMockDiscounts_whenMinimumProductQuantityInCategorySameButDiscountRateGreater_thenTheDiscountAlsoGreater() {
        //given
        Category category1 = new Category(Title.valueOf("Category1"));
        Assertions.assertThat(category1).isNotNull();
        Quantity minimumProductQuantityInCategory1 = Quantity.valueOf(1);
        assertThat(minimumProductQuantityInCategory1).isNotNull();
        Rate discountRate1 = Rate.valueOf(15D);
        assertThat(discountRate1).isNotNull();

        Category category2 = new Category(Title.valueOf("Category2"));
        Assertions.assertThat(category2).isNotNull();
        Quantity minimumProductQuantityInCategory2 = Quantity.valueOf(1);
        assertThat(minimumProductQuantityInCategory2).isNotNull();
        Rate discountRate2 = Rate.valueOf(10D);
        assertThat(discountRate2).isNotNull();


        ProductQuantityInCategoryBasedRateDiscount discount1 = new ProductQuantityInCategoryBasedRateDiscount(category1, minimumProductQuantityInCategory1, discountRate1);
        ProductQuantityInCategoryBasedRateDiscount discount2 = new ProductQuantityInCategoryBasedRateDiscount(category2, minimumProductQuantityInCategory2, discountRate2);

        //when
        assertThat(discountRate1.isGreaterThan(discountRate2)).isTrue();

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
        Rate discountRate = Rate.valueOf(10D);
        assertThat(discountRate).isNotNull();

        ShoppingCart shoppingCart = mock(ShoppingCart.class, withSettings().useConstructor().defaultAnswer(CALLS_REAL_METHODS));
        when(shoppingCart.getQuantityOfProductsBelongsToCategoryInCart(category)).thenReturn(Quantity.ofZero());

        ProductQuantityInCategoryBasedRateDiscount discount = new ProductQuantityInCategoryBasedRateDiscount(category, minimumProductQuantityInCategory, discountRate);
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
        Rate discountRate = Rate.valueOf(10D);
        assertThat(discountRate).isNotNull();

        ShoppingCart shoppingCart = mock(ShoppingCart.class, withSettings().useConstructor().defaultAnswer(CALLS_REAL_METHODS));
        when(shoppingCart.getQuantityOfProductsBelongsToCategoryInCart(category)).thenReturn(Quantity.valueOf(1));

        ProductQuantityInCategoryBasedRateDiscount discount = new ProductQuantityInCategoryBasedRateDiscount(category, minimumProductQuantityInCategory, discountRate);
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
    public void givenCategory_whenQuantityOfProductsInCategoryIsGreaterOrEquals1_thenApplyDiscountRateOf10() {
        //given
        Category category = new Category(Title.valueOf("Category"));
        Assertions.assertThat(category).isNotNull();
        Quantity minimumProductQuantityInCategory = Quantity.valueOf(1);
        assertThat(minimumProductQuantityInCategory).isNotNull();
        Rate discountRate = Rate.valueOf(10D);
        assertThat(discountRate).isNotNull();

        ShoppingCart shoppingCart = mock(ShoppingCart.class, withSettings().useConstructor().defaultAnswer(CALLS_REAL_METHODS));
        when(shoppingCart.getQuantityOfProductsBelongsToCategoryInCart(category)).thenReturn(Quantity.valueOf(1));
        when(shoppingCart.getTotalPriceOfProductsBelongToCategoryInCart(category)).thenReturn(Amount.valueOf(100D));

        ProductQuantityInCategoryBasedRateDiscount discount = new ProductQuantityInCategoryBasedRateDiscount(category, minimumProductQuantityInCategory, discountRate);
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
    public void givenMultipleDifferentCategories_whenMultipleProductQuantityInCategoryBasedRateDiscounts_thenApplyTheGreatestDiscountPerCategory() {
        //given
        Category category1 = new Category(Title.valueOf("Category1"));
        Assertions.assertThat(category1).isNotNull();
        Quantity minimumProductQuantityInCategory1 = Quantity.valueOf(1);
        assertThat(minimumProductQuantityInCategory1).isNotNull();
        Rate discountRate1 = Rate.valueOf(15D);
        assertThat(discountRate1).isNotNull();

        Category category2 = new Category(Title.valueOf("Category2"));
        Assertions.assertThat(category2).isNotNull();
        Quantity minimumProductQuantityInCategory2 = Quantity.valueOf(1);
        assertThat(minimumProductQuantityInCategory2).isNotNull();
        Rate discountRate2 = Rate.valueOf(10D);
        assertThat(discountRate2).isNotNull();

        ProductQuantityInCategoryBasedRateDiscount discount1 = new ProductQuantityInCategoryBasedRateDiscount(category1, minimumProductQuantityInCategory1, discountRate1);
        ProductQuantityInCategoryBasedRateDiscount discount2 = new ProductQuantityInCategoryBasedRateDiscount(category2, minimumProductQuantityInCategory2, discountRate2);

        ShoppingCart shoppingCart = mock(ShoppingCart.class, withSettings().useConstructor().defaultAnswer(CALLS_REAL_METHODS));
        when(shoppingCart.getQuantityOfProductsBelongsToCategoryInCart(category1)).thenReturn(Quantity.valueOf(2));
        when(shoppingCart.getTotalPriceOfProductsBelongToCategoryInCart(category1)).thenReturn(Amount.valueOf(100D));
        when(shoppingCart.getQuantityOfProductsBelongsToCategoryInCart(category2)).thenReturn(Quantity.valueOf(3));
        when(shoppingCart.getTotalPriceOfProductsBelongToCategoryInCart(category2)).thenReturn(Amount.valueOf(100D));

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
    public void givenMultipleSameCategories_whenMultipleProductQuantityInCategoryBasedRateDiscounts_thenApplyTheGreatestDiscountForCategory() {
        //given
        Category category = new Category(Title.valueOf("Category"));
        Assertions.assertThat(category).isNotNull();

        Quantity minimumProductQuantityInCategory1 = Quantity.valueOf(1);
        assertThat(minimumProductQuantityInCategory1).isNotNull();
        Rate discountRate1 = Rate.valueOf(15D);
        assertThat(discountRate1).isNotNull();

        Quantity minimumProductQuantityInCategory2 = Quantity.valueOf(1);
        assertThat(minimumProductQuantityInCategory2).isNotNull();
        Rate discountRate2 = Rate.valueOf(10D);
        assertThat(discountRate2).isNotNull();

        ProductQuantityInCategoryBasedRateDiscount discount1 = new ProductQuantityInCategoryBasedRateDiscount(category, minimumProductQuantityInCategory1, discountRate1);
        ProductQuantityInCategoryBasedRateDiscount discount2 = new ProductQuantityInCategoryBasedRateDiscount(category, minimumProductQuantityInCategory2, discountRate2);

        ShoppingCart shoppingCart = mock(ShoppingCart.class, withSettings().useConstructor().defaultAnswer(CALLS_REAL_METHODS));
        when(shoppingCart.getQuantityOfProductsBelongsToCategoryInCart(category)).thenReturn(Quantity.valueOf(2));
        when(shoppingCart.getTotalPriceOfProductsBelongToCategoryInCart(category)).thenReturn(Amount.valueOf(100D));

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
