package com.trendyol.shoppingcart.core.domain;

import com.trendyol.shoppingcart.core.domain.value.Amount;
import com.trendyol.shoppingcart.core.domain.value.Title;
import com.trendyol.shoppingcart.core.exception.InvalidValueException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class ProductTest {

    @Test
    public void givenNullTitle_whenCreateProduct_thenThrowInvalidValueException() {
        //given
        Title title = null;
        Amount price = Amount.valueOf(10D);
        Category category = new Category(Title.valueOf("Category"));

        //when
        assertThat(title).isNull();
        Throwable throwable = catchThrowable(() -> {
            Product product = new Product(title, price, category);
        });

        //then
        assertThat(throwable)
                .isNotNull()
                .isInstanceOf(InvalidValueException.class)
                .hasMessage("Product title can not be null!");
    }

    @Test
    public void givenNullPrice_whenCreateProduct_thenThrowInvalidValueException() {
        //given
        Title title = Title.valueOf("Product");
        Amount price = null;
        Category category = new Category(Title.valueOf("Category"));

        //when
        assertThat(price).isNull();
        Throwable throwable = catchThrowable(() -> {
            Product product = new Product(title, price, category);
        });

        //then
        assertThat(throwable)
                .isNotNull()
                .isInstanceOf(InvalidValueException.class)
                .hasMessage("Product price can not be null!");
    }

    @Test
    public void givenZeroPrice_whenCreateProduct_thenThrowInvalidValueException() {
        //given
        Title title = Title.valueOf("Product");
        Amount price = Amount.ofZero();
        Category category = new Category(Title.valueOf("Category"));

        //when
        assertThat(price).isNotNull();
        assertThat(price.isZero()).isTrue();
        Throwable throwable = catchThrowable(() -> {
            Product product = new Product(title, price, category);
        });

        //then
        assertThat(throwable)
                .isNotNull()
                .isInstanceOf(InvalidValueException.class)
                .hasMessage("Product price can not be zero!");
    }

    @Test
    public void givenNullCategory_whenCreateProduct_thenThrowInvalidValueException() {
        //given
        Title title = Title.valueOf("Product");
        Amount price = Amount.valueOf(10D);
        Category category = null;

        //when
        assertThat(category).isNull();
        Throwable throwable = catchThrowable(() -> {
            Product product = new Product(title, price, category);
        });

        //then
        assertThat(throwable)
                .isNotNull()
                .isInstanceOf(InvalidValueException.class)
                .hasMessage("Product category can not be null!");
    }

    @Test
    public void givenValidTitleAndPriceAndCategory_whenCreateProduct_thenReturnProduct() {
        //given
        Title title = Title.valueOf("Product");
        Amount price = Amount.valueOf(10D);
        Category category = new Category(Title.valueOf("Category"));

        //when
        Product product = new Product(title, price, category);

        //then
        assertThat(product).isNotNull();
        assertThat(product.getTitle()).isEqualTo(title);
        assertThat(product.getPrice()).isEqualTo(price);
        assertThat(product.getCategory()).isEqualTo(category);
    }

    @Test
    public void givenSameTitlesDifferentPricesDifferentCategories_whenCheckEquality_thenReturnFalse() {
        //given
        Title title1 = Title.valueOf("Product");
        Title title2 = Title.valueOf("Product");
        Amount price1 = Amount.valueOf(10D);
        Amount price2 = Amount.valueOf(10D);
        Category category1 = new Category(Title.valueOf("Category 1"));
        Category category2 = new Category(Title.valueOf("Category 2"));

        //when
        Product product1 = new Product(title1, price1, category1);
        Product product2 = new Product(title2, price2, category2);

        //then
        assertThat(product1).isNotEqualTo(product2);
    }

    @Test
    public void givenDifferentTitlesSamePricesDifferentCategories_whenCheckEquality_thenReturnFalse() {
        //given
        Title title1 = Title.valueOf("Product 1");
        Title title2 = Title.valueOf("Product 2");
        Amount price1 = Amount.valueOf(10D);
        Amount price2 = Amount.valueOf(20D);
        Category category1 = new Category(Title.valueOf("Category 1"));
        Category category2 = new Category(Title.valueOf("Category 2"));

        //when
        Product product1 = new Product(title1, price1, category1);
        Product product2 = new Product(title2, price2, category2);

        //then
        assertThat(product1).isNotEqualTo(product2);
    }

    @Test
    public void givenDifferentTitlesDifferentPricesSameCategories_whenCheckEquality_thenReturnFalse() {
        //given
        Title title1 = Title.valueOf("Product 1");
        Title title2 = Title.valueOf("Product 2");
        Amount price1 = Amount.valueOf(10D);
        Amount price2 = Amount.valueOf(20D);
        Category category1 = new Category(Title.valueOf("Category 1"));
        Category category2 = new Category(Title.valueOf("Category 1"));

        //when
        Product product1 = new Product(title1, price1, category1);
        Product product2 = new Product(title2, price2, category2);

        //then
        assertThat(product1).isNotEqualTo(product2);
    }

    @Test
    public void givenDifferentTitlesSamePricesSameCategories_whenCheckEquality_thenReturnFalse() {
        //given
        Title title1 = Title.valueOf("Product 1");
        Title title2 = Title.valueOf("Product 2");
        Amount price1 = Amount.valueOf(10D);
        Amount price2 = Amount.valueOf(10D);
        Category category1 = new Category(Title.valueOf("Category 1"));
        Category category2 = new Category(Title.valueOf("Category 1"));

        //when
        Product product1 = new Product(title1, price1, category1);
        Product product2 = new Product(title2, price2, category2);

        //then
        assertThat(product1).isNotEqualTo(product2);
    }

    @Test
    public void givenSameTitlesDifferentPricesSameCategories_whenCheckEquality_thenReturnFalse() {
        //given
        Title title1 = Title.valueOf("Product 1");
        Title title2 = Title.valueOf("Product 1");
        Amount price1 = Amount.valueOf(10D);
        Amount price2 = Amount.valueOf(20D);
        Category category1 = new Category(Title.valueOf("Category 1"));
        Category category2 = new Category(Title.valueOf("Category 1"));

        //when
        Product product1 = new Product(title1, price1, category1);
        Product product2 = new Product(title2, price2, category2);

        //then
        assertThat(product1).isNotEqualTo(product2);
    }

    @Test
    public void givenSameTitlesSamePricesDifferentCategories_whenCheckEquality_thenReturnFalse() {
        //given
        Title title1 = Title.valueOf("Product 1");
        Title title2 = Title.valueOf("Product 1");
        Amount price1 = Amount.valueOf(10D);
        Amount price2 = Amount.valueOf(10D);
        Category category1 = new Category(Title.valueOf("Category 1"));
        Category category2 = new Category(Title.valueOf("Category 2"));

        //when
        Product product1 = new Product(title1, price1, category1);
        Product product2 = new Product(title2, price2, category2);

        //then
        assertThat(product1).isNotEqualTo(product2);
    }

    @Test
    public void givenSameTitlesSamePricesSameCategories_whenCheckEquality_thenReturnTrue() {
        //given
        Title title1 = Title.valueOf("Product 1");
        Title title2 = Title.valueOf("Product 1");
        Amount price1 = Amount.valueOf(10D);
        Amount price2 = Amount.valueOf(10D);
        Category category1 = new Category(Title.valueOf("Category 1"));
        Category category2 = new Category(Title.valueOf("Category 1"));

        //when
        Product product1 = new Product(title1, price1, category1);
        Product product2 = new Product(title2, price2, category2);

        //then
        assertThat(product1).isEqualTo(product2);
    }

    @Test
    public void givenSameTitlesSamePricesProduct1CategoryLessThanProduct2Category_whenCompareProducts_thenReturnNegative() {
        //given
        Title title1 = Title.valueOf("Product 1");
        Title title2 = Title.valueOf("Product 1");
        Amount price1 = Amount.valueOf(10D);
        Amount price2 = Amount.valueOf(10D);
        Category category1 = new Category(Title.valueOf("Category 1"));
        Category category2 = new Category(Title.valueOf("Category 2"));

        //when
        Product product1 = new Product(title1, price1, category1);
        Product product2 = new Product(title2, price2, category2);

        //then
        assertThat(product1).isLessThan(product2);
    }

    @Test
    public void givenSameTitlesSameCategoriesProduct1PriceLessThanProduct2Price_whenCompareProducts_thenReturnNegative() {
        //given
        Title title1 = Title.valueOf("Product 1");
        Title title2 = Title.valueOf("Product 1");
        Amount price1 = Amount.valueOf(10D);
        Amount price2 = Amount.valueOf(20D);
        Category category1 = new Category(Title.valueOf("Category 1"));
        Category category2 = new Category(Title.valueOf("Category 1"));

        //when
        Product product1 = new Product(title1, price1, category1);
        Product product2 = new Product(title2, price2, category2);

        //then
        assertThat(product1).isLessThan(product2);
    }

    @Test
    public void givenSamePricesSameCategoriesProduct1TitleLessThanProduct2Title_whenCompareProducts_thenReturnNegative() {
        //given
        Title title1 = Title.valueOf("Product 1");
        Title title2 = Title.valueOf("Product 2");
        Amount price1 = Amount.valueOf(10D);
        Amount price2 = Amount.valueOf(10D);
        Category category1 = new Category(Title.valueOf("Category 1"));
        Category category2 = new Category(Title.valueOf("Category 1"));

        //when
        Product product1 = new Product(title1, price1, category1);
        Product product2 = new Product(title2, price2, category2);

        //then
        assertThat(product1).isLessThan(product2);
    }

    @Test
    public void givenProductTitlesSameAndProduct1PriceLessThanProduct2Price_whenCompareProducts_thenReturnNegative() {
        //given
        Title title1 = Title.valueOf("Product 1");
        Title title2 = Title.valueOf("Product 1");
        Amount price1 = Amount.valueOf(10D);
        Amount price2 = Amount.valueOf(20D);
        Category category1 = new Category(Title.valueOf("Category 1"));
        Category category2 = new Category(Title.valueOf("Category 2"));

        //when
        Product product1 = new Product(title1, price1, category1);
        Product product2 = new Product(title2, price2, category2);

        //then
        assertThat(product1).isLessThan(product2);
    }
}
