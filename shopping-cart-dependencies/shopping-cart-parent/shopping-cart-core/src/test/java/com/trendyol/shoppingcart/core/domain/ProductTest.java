package com.trendyol.shoppingcart.core.domain;

import com.trendyol.shoppingcart.core.domain.value.Amount;
import com.trendyol.shoppingcart.core.domain.value.Title;
import com.trendyol.shoppingcart.core.exception.InvalidValueException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ProductTest {

    @Test
    public void givenNullTitle_whenCreateProduct_thenThrowInvalidValueException() {
        //given
        Title title = null;
        Amount price = Amount.valueOf(10D);
        Category category = new Category(Title.valueOf("Category"));

        //when
        assertNull(title);
        InvalidValueException exception = assertThrows(InvalidValueException.class, () -> new Product(title, price, category));

        //then
        assertNotNull(exception);
        assertEquals(exception.getMessage(), "Product title can not be null!");
    }

    @Test
    public void givenNullPrice_whenCreateProduct_thenThrowInvalidValueException() {
        //given
        Title title = Title.valueOf("Product");
        Amount price = null;
        Category category = new Category(Title.valueOf("Category"));

        //when
        assertNull(price);
        InvalidValueException exception = assertThrows(InvalidValueException.class, () -> new Product(title, price, category));

        //then
        assertNotNull(exception);
        assertEquals(exception.getMessage(), "Product price can not be null!");
    }

    @Test
    public void givenZeroPrice_whenCreateProduct_thenThrowInvalidValueException() {
        //given
        Title title = Title.valueOf("Product");
        Amount price = Amount.ofZero();
        Category category = new Category(Title.valueOf("Category"));

        //when
        assertTrue(price.isZero());
        InvalidValueException exception = assertThrows(InvalidValueException.class, () -> new Product(title, price, category));

        //then
        assertNotNull(exception);
        assertEquals(exception.getMessage(), "Product price can not be zero!");
    }

    @Test
    public void givenNullCategory_whenCreateProduct_thenThrowInvalidValueException() {
        //given
        Title title = Title.valueOf("Product");
        Amount price = Amount.valueOf(10D);
        Category category = null;

        //when
        assertNull(category);
        InvalidValueException exception = assertThrows(InvalidValueException.class, () -> new Product(title, price, category));

        //then
        assertNotNull(exception);
        assertEquals(exception.getMessage(), "Product category can not be null!");
    }

    @Test
    public void givenValidTitleAndPriceAndCategory_whenCreateCategory_thenReturnCategory() {
        //given
        Title title = Title.valueOf("Product");
        Amount price = Amount.valueOf(10D);
        Category category = new Category(Title.valueOf("Category"));

        //when
        Product product = new Product(title, price, category);

        //then
        assertNotNull(product);
        assertEquals(product.getTitle(), title);
        assertEquals(product.getPrice(), price);
        assertEquals(product.getCategory(), category);
    }

    @Test
    public void givenSameTitlesDifferentPricesDifferentCategories_WhenCheckEquality_thenReturnFalse() {
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
        boolean result = product1.equals(product2);

        //then
        assertFalse(result);
    }

    @Test
    public void givenDifferentTitlesSamePricesDifferentCategories_WhenCheckEquality_thenReturnFalse() {
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
        boolean result = product1.equals(product2);

        //then
        assertFalse(result);
    }

    @Test
    public void givenDifferentTitlesDifferentPricesSameCategories_WhenCheckEquality_thenReturnFalse() {
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
        boolean result = product1.equals(product2);

        //then
        assertFalse(result);
    }

    @Test
    public void givenDifferentTitlesSamePricesSameCategories_WhenCheckEquality_thenReturnFalse() {
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
        boolean result = product1.equals(product2);

        //then
        assertFalse(result);
    }

    @Test
    public void givenSameTitlesDifferentPricesSameCategories_WhenCheckEquality_thenReturnFalse() {
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
        boolean result = product1.equals(product2);

        //then
        assertFalse(result);
    }

    @Test
    public void givenSameTitlesSamePricesDifferentCategories_WhenCheckEquality_thenReturnFalse() {
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
        boolean result = product1.equals(product2);

        //then
        assertFalse(result);
    }

    @Test
    public void givenSameTitlesSamePricesSameCategories_WhenCheckEquality_thenReturnTrue() {
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
        boolean result = product1.equals(product2);

        //then
        assertTrue(result);
    }

    @Test
    public void givenSameTitlesSamePricesProduct1CategoryLesserThanProduct2Category_WhenCompareProducts_thenReturnNegative() {
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
        int result = product1.compareTo(product2);

        //then
        assertTrue(result < 0);
    }

    @Test
    public void givenSameTitlesSameCategoriesProduct1PriceLesserThanProduct2Price_WhenCompareProducts_thenReturnNegative() {
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
        int result = product1.compareTo(product2);

        //then
        assertTrue(result < 0);
    }

    @Test
    public void givenSamePricesSameCategoriesProduct1TitleLesserThanProduct2Title_WhenCompareProducts_thenReturnNegative() {
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
        int result = product1.compareTo(product2);

        //then
        assertTrue(result < 0);
    }

    @Test
    public void givenProductTitlesSameAndProduct1PriceLesserThanProduct2Price_WhenCompareProducts_thenReturnNegative() {
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
        int result = product1.compareTo(product2);

        //then
        assertTrue(result < 0);
    }
}
