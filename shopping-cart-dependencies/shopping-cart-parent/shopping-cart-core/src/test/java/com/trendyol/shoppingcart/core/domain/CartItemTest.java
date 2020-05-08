package com.trendyol.shoppingcart.core.domain;

import com.trendyol.shoppingcart.core.domain.value.Amount;
import com.trendyol.shoppingcart.core.domain.value.Quantity;
import com.trendyol.shoppingcart.core.domain.value.Title;
import com.trendyol.shoppingcart.core.exception.InvalidValueException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CartItemTest {

    @Test
    public void givenNullProduct_whenCreateCartItem_thenThrowInvalidValueException() {
        //given
        Product product = null;
        Quantity quantity = Quantity.valueOf(1);

        //when
        assertNull(product);
        InvalidValueException exception = assertThrows(InvalidValueException.class, () -> new CartItem(product, quantity));

        //then
        assertNotNull(exception);
        assertEquals(exception.getMessage(), "Product can not be null!");
    }

    @Test
    public void givenNullQuantity_whenCreateCartItem_thenThrowInvalidValueException() {
        //given
        Product product = new Product(Title.valueOf("Product"), Amount.valueOf(10D), new Category(Title.valueOf("Category")));
        Quantity quantity = null;

        //when
        assertNull(quantity);
        InvalidValueException exception = assertThrows(InvalidValueException.class, () -> new CartItem(product, quantity));

        //then
        assertNotNull(exception);
        assertEquals(exception.getMessage(), "Quantity can not be null!");
    }

    @Test
    public void givenZeroQuantity_whenCreateCartItem_thenThrowInvalidValueException() {
        //given
        Product product = new Product(Title.valueOf("Product"), Amount.valueOf(10D), new Category(Title.valueOf("Category")));
        Quantity quantity = Quantity.valueOf(0);

        //when
        InvalidValueException exception = assertThrows(InvalidValueException.class, () -> new CartItem(product, quantity));

        //then
        assertNotNull(exception);
        assertEquals(exception.getMessage(), "Quantity can not be zero!");
    }

    @Test
    public void givenValidProductAndQuantity_whenCreateCartItem_thenReturnCartItem() {
        //given
        Product product = new Product(Title.valueOf("Product"), Amount.valueOf(10D), new Category(Title.valueOf("Category")));
        Quantity quantity = Quantity.valueOf(1);

        //when
        CartItem cartItem = new CartItem(product, quantity);

        //then
        assertNotNull(cartItem);
        assertEquals(cartItem.getProduct(), product);
        assertEquals(cartItem.getQuantity(), quantity);
    }

    @Test
    public void givenValidProductAndQuantity_whenCreateCartItem_thenCategoryOfCartItemEqualsProductCategory() {
        //given
        Product product = new Product(Title.valueOf("Product"), Amount.valueOf(10D), new Category(Title.valueOf("Category")));
        Quantity quantity = Quantity.valueOf(1);

        //when
        CartItem cartItem = new CartItem(product, quantity);

        //then
        assertNotNull(cartItem);
        assertEquals(cartItem.getCategory(), product.getCategory());
    }

    @Test
    public void givenValidProductAndQuantity_whenCreateCartItem_thenUnitPriceOfCartItemEqualsProductPrice() {
        //given
        Product product = new Product(Title.valueOf("Product"), Amount.valueOf(10D), new Category(Title.valueOf("Category")));
        Quantity quantity = Quantity.valueOf(1);

        //when
        CartItem cartItem = new CartItem(product, quantity);

        //then
        assertNotNull(cartItem);
        assertEquals(cartItem.getUnitPrice(), product.getPrice());
    }

    @Test
    public void givenValidProductAndQuantity_whenCreateCartItem_thenTotalPriceOfCartItemEqualsProductPriceMultipliedByQuantity() {
        //given
        Product product = new Product(Title.valueOf("Product"), Amount.valueOf(10D), new Category(Title.valueOf("Category")));
        Quantity quantity = Quantity.valueOf(2);

        //when
        CartItem cartItem = new CartItem(product, quantity);

        //then
        assertNotNull(cartItem);
        assertEquals(cartItem.getTotalPrice(), product.getPrice().multiply(quantity.doubleValue()));
        assertEquals(cartItem.getTotalPrice(), Amount.valueOf(20D));
    }

    @Test
    public void givenSameProductDifferentQuantities_whenCheckEquality_thenReturnFalse() {
        //given
        Product product1 = new Product(Title.valueOf("Product"), Amount.valueOf(10D), new Category(Title.valueOf("Category")));
        Product product2 = new Product(Title.valueOf("Product"), Amount.valueOf(10D), new Category(Title.valueOf("Category")));
        Quantity quantity1 = Quantity.valueOf(1);
        Quantity quantity2 = Quantity.valueOf(2);


        //when
        CartItem cartItem1 = new CartItem(product1, quantity1);
        CartItem cartItem2 = new CartItem(product2, quantity2);
        boolean result = cartItem1.equals(cartItem2);

        //then
        assertFalse(result);
    }

    @Test
    public void givenDifferentProductSameQuantities_whenCheckEquality_thenReturnFalse() {
        //given
        Product product1 = new Product(Title.valueOf("Product1"), Amount.valueOf(10D), new Category(Title.valueOf("Category")));
        Product product2 = new Product(Title.valueOf("Product2"), Amount.valueOf(10D), new Category(Title.valueOf("Category")));
        Quantity quantity1 = Quantity.valueOf(1);
        Quantity quantity2 = Quantity.valueOf(1);


        //when
        CartItem cartItem1 = new CartItem(product1, quantity1);
        CartItem cartItem2 = new CartItem(product2, quantity2);
        boolean result = cartItem1.equals(cartItem2);

        //then
        assertFalse(result);
    }

    @Test
    public void givenSameProductSameQuantities_whenCheckEquality_thenReturnTrue() {
        //given
        Product product1 = new Product(Title.valueOf("Product"), Amount.valueOf(10D), new Category(Title.valueOf("Category")));
        Product product2 = new Product(Title.valueOf("Product"), Amount.valueOf(10D), new Category(Title.valueOf("Category")));
        Quantity quantity1 = Quantity.valueOf(1);
        Quantity quantity2 = Quantity.valueOf(1);


        //when
        CartItem cartItem1 = new CartItem(product1, quantity1);
        CartItem cartItem2 = new CartItem(product2, quantity2);
        boolean result = cartItem1.equals(cartItem2);

        //then
        assertTrue(result);
    }

    @Test
    public void givenSameQuantitiesCartItem1ProductLesserThanCartItem2Product_whenCompareProducts_thenReturnNegative() {
        //given
        Product product1 = new Product(Title.valueOf("Product1"), Amount.valueOf(10D), new Category(Title.valueOf("Category")));
        Product product2 = new Product(Title.valueOf("Product2"), Amount.valueOf(10D), new Category(Title.valueOf("Category")));
        Quantity quantity1 = Quantity.valueOf(1);
        Quantity quantity2 = Quantity.valueOf(1);

        //when
        CartItem cartItem1 = new CartItem(product1, quantity1);
        CartItem cartItem2 = new CartItem(product2, quantity2);
        int result = cartItem1.compareTo(cartItem2);

        //then
        assertTrue(result < 0);
    }

    @Test
    public void givenSameProductsCartItem1QuantityLesserThanCartItem2Quantity_whenCompareProducts_thenReturnNegative() {
        //given
        Product product1 = new Product(Title.valueOf("Product"), Amount.valueOf(10D), new Category(Title.valueOf("Category")));
        Product product2 = new Product(Title.valueOf("Product"), Amount.valueOf(10D), new Category(Title.valueOf("Category")));
        Quantity quantity1 = Quantity.valueOf(1);
        Quantity quantity2 = Quantity.valueOf(2);

        //when
        CartItem cartItem1 = new CartItem(product1, quantity1);
        CartItem cartItem2 = new CartItem(product2, quantity2);
        int result = cartItem1.compareTo(cartItem2);

        //then
        assertTrue(result < 0);
    }

    @Test
    public void givenCartItem_whenIncreaseQuantityWithNewQuantity_thenReturnSumOfQuantityAndNewQuantityAsResult() {
        //given
        Product product = new Product(Title.valueOf("Product"), Amount.valueOf(10D), new Category(Title.valueOf("Category")));
        Quantity quantity = Quantity.valueOf(2);
        CartItem cartItem = new CartItem(product, quantity);

        //when
        cartItem.increaseQuantity(Quantity.valueOf(1));

        //then
        assertEquals(cartItem.getQuantity(), Quantity.valueOf(2).add(Quantity.valueOf(1)));
        assertEquals(cartItem.getQuantity(), Quantity.valueOf(3));

    }

    @Test
    public void givenCartItem_whenCalculateTotalPrice_thenReturnUnitPriceMultipliedByQuantity() {
        //given
        Product product = new Product(Title.valueOf("Product"), Amount.valueOf(10D), new Category(Title.valueOf("Category")));
        Quantity quantity = Quantity.valueOf(1);
        CartItem cartItem = new CartItem(product, quantity);

        //when
        cartItem.calculateTotalPrice();

        //then
        assertEquals(cartItem.getTotalPrice(), cartItem.getUnitPrice().multiply(cartItem.getQuantity().doubleValue()));
        assertEquals(cartItem.getTotalPrice(), Amount.valueOf(10D));

        //increase quantity by 1
        //when
        cartItem.increaseQuantity(Quantity.valueOf(1));

        //then
        assertEquals(cartItem.getTotalPrice(), cartItem.getUnitPrice().multiply(cartItem.getQuantity().doubleValue()));
        assertEquals(cartItem.getTotalPrice(), Amount.valueOf(20D));

    }
}
