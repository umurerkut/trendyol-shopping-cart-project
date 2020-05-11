package com.trendyol.shoppingcart.client.mock.discount;

import com.trendyol.shoppingcart.core.domain.Category;
import com.trendyol.shoppingcart.core.domain.Product;
import com.trendyol.shoppingcart.core.domain.value.Amount;
import com.trendyol.shoppingcart.core.domain.value.DiscountName;
import com.trendyol.shoppingcart.core.domain.value.Title;
import com.trendyol.shoppingcart.core.exception.InvalidValueException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

//Mock discount: If more specific product exists in cart, then apply discount amount of dismountAmount
public class ProductTypeBasedMockDiscountTest {

    @Test
    public void givenProduct_whenProductsIsNull_thenThrowInvalidValueException() {
        //given
        Product product = null;
        Amount discountAmount = Amount.valueOf(10D);
        Assertions.assertThat(product).isNull();

        //when
        Throwable throwable = catchThrowable(() -> {
            ProductTypeBasedMockDiscount discount = new ProductTypeBasedMockDiscount(product, discountAmount);
        });

        //then
        assertThat(throwable)
                .isNotNull()
                .isInstanceOf(InvalidValueException.class)
                .hasMessage("Given product can not be null");
    }

    @Test
    public void givenDiscountAmount_whenDiscountAmountIsNull_thenThrowInvalidValueException() {
        //given
        Product product = new Product(Title.valueOf("Product"), Amount.valueOf(10D), new Category(Title.valueOf("Category")));
        Amount discountAmount = null;
        assertThat(discountAmount).isNull();

        //when
        Throwable throwable = catchThrowable(() -> {
            ProductTypeBasedMockDiscount discount = new ProductTypeBasedMockDiscount(product, discountAmount);
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
        Product product = new Product(Title.valueOf("Product"), Amount.valueOf(10D), new Category(Title.valueOf("Category")));
        Amount discountAmount = Amount.ofZero();
        assertThat(discountAmount).isNotNull();

        //when
        Throwable throwable = catchThrowable(() -> {
            ProductTypeBasedMockDiscount discount = new ProductTypeBasedMockDiscount(product, discountAmount);
        });

        //then
        assertThat(throwable)
                .isNotNull()
                .isInstanceOf(InvalidValueException.class)
                .hasMessage("Amount of discount to be applied can not be zero!");
    }

    @Test
    public void givenValidDiscountAmountAndProduct_whenCreateProductTypeBasedMockDiscount_thenReturnProductQuantityTypeMockDiscount() {
        //given
        Product product = new Product(Title.valueOf("Product"), Amount.valueOf(10D), new Category(Title.valueOf("Category")));
        Amount discountAmount = Amount.valueOf(10D);
        Assertions.assertThat(product).isNotNull();
        assertThat(discountAmount).isNotNull();

        //when
        ProductTypeBasedMockDiscount discount = new ProductTypeBasedMockDiscount(product, discountAmount);

        //then
        assertThat(discount).isNotNull();
        String discountNameText = (product.getTitle().getValue() + "_PRODUCT_TYPE_BASED_MOCK_DISCOUNT").replaceAll(" ", "_").toUpperCase();
        assertThat(discount.getDiscountName()).isEqualTo(DiscountName.valueOf(discountNameText));
        assertThat(discount.getCalculationStrategy().getDiscountAmount()).isEqualTo(discountAmount);
        assertThat(discount.getValidationStrategy().getProduct()).isEqualTo(product);
    }

    @Test
    public void givenTwoProductQuantityBasedMockDiscounts_whenDiscountAmountGreater_thenTheDiscountAlsoGreater() {
        //given
        Amount discountAmount1 = Amount.valueOf(20D);
        Amount discountAmount2 = Amount.valueOf(10D);
        Product product1 = new Product(Title.valueOf("Product1"), Amount.valueOf(10D), new Category(Title.valueOf("Category")));
        Product product2 = new Product(Title.valueOf("Product2"), Amount.valueOf(10D), new Category(Title.valueOf("Category")));
        ProductTypeBasedMockDiscount discount1 = new ProductTypeBasedMockDiscount(product1, discountAmount1);
        ProductTypeBasedMockDiscount discount2 = new ProductTypeBasedMockDiscount(product2, discountAmount2);

        //when
        assertThat(discountAmount1).isNotNull();
        assertThat(discountAmount2).isNotNull();
        assertThat(product1).isNotNull();
        assertThat(product2).isNotNull();
        assertThat(discountAmount1.isGreaterThan(discountAmount2)).isTrue();

        //then
        assertThat(discount1.isGreaterThan(discount2)).isTrue();
    }
}
