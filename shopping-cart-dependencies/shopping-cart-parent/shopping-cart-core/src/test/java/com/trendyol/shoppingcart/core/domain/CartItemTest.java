package com.trendyol.shoppingcart.core.domain;

import com.trendyol.shoppingcart.core.domain.value.Amount;
import com.trendyol.shoppingcart.core.domain.value.Quantity;
import com.trendyol.shoppingcart.core.domain.value.Title;
import com.trendyol.shoppingcart.core.exception.InvalidValueException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class CartItemTest {

    @Test
    public void givenNullProduct_whenCreateCartItem_thenThrowInvalidValueException() {
        //given
        Product product = null;
        Quantity quantity = Quantity.valueOf(1);

        //when
        assertThat(product).isNull();
        Throwable throwable = catchThrowable(() -> {
            CartItem cartItem = new CartItem(product, quantity);
        });

        //then
        assertThat(throwable)
                .isNotNull()
                .isInstanceOf(InvalidValueException.class)
                .hasMessage("Product can not be null!");
    }

    @Test
    public void givenNullQuantity_whenCreateCartItem_thenThrowInvalidValueException() {
        //given
        Product product = new Product(Title.valueOf("Product"), Amount.valueOf(10D), new Category(Title.valueOf("Category")));
        Quantity quantity = null;

        //when
        assertThat(quantity).isNull();
        Throwable throwable = catchThrowable(() -> {
            CartItem cartItem = new CartItem(product, quantity);
        });

        //then
        assertThat(throwable)
                .isNotNull()
                .isInstanceOf(InvalidValueException.class)
                .hasMessage("Quantity can not be null!");
    }

    @Test
    public void givenZeroQuantity_whenCreateCartItem_thenThrowInvalidValueException() {
        //given
        Product product = new Product(Title.valueOf("Product"), Amount.valueOf(10D), new Category(Title.valueOf("Category")));
        Quantity quantity = Quantity.valueOf(0);

        //when
        assertThat(quantity).isNotNull();
        Throwable throwable = catchThrowable(() -> {
            CartItem cartItem = new CartItem(product, quantity);
        });

        //then
        assertThat(throwable)
                .isNotNull()
                .isInstanceOf(InvalidValueException.class)
                .hasMessage("Quantity can not be zero!");
    }

    @Test
    public void givenValidProductAndQuantity_whenCreateCartItem_thenReturnCartItem() {
        //given
        Product product = new Product(Title.valueOf("Product"), Amount.valueOf(10D), new Category(Title.valueOf("Category")));
        Quantity quantity = Quantity.valueOf(1);

        //when
        CartItem cartItem = new CartItem(product, quantity);

        //then
        assertThat(cartItem).isNotNull();
        assertThat(cartItem.getProduct()).isEqualTo(product);
        assertThat(cartItem.getQuantity()).isEqualTo(quantity);
    }

    @Test
    public void givenValidProductAndQuantity_whenCreateCartItem_thenCategoryOfCartItemEqualsProductCategory() {
        //given
        Product product = new Product(Title.valueOf("Product"), Amount.valueOf(10D), new Category(Title.valueOf("Category")));
        Quantity quantity = Quantity.valueOf(1);

        //when
        CartItem cartItem = new CartItem(product, quantity);

        //then
        assertThat(cartItem).isNotNull();
        assertThat(cartItem.getCategory()).isEqualTo(product.getCategory());
    }

    @Test
    public void givenValidProductAndQuantity_whenCreateCartItem_thenUnitPriceOfCartItemEqualsProductPrice() {
        //given
        Product product = new Product(Title.valueOf("Product"), Amount.valueOf(10D), new Category(Title.valueOf("Category")));
        Quantity quantity = Quantity.valueOf(1);

        //when
        CartItem cartItem = new CartItem(product, quantity);

        //then
        assertThat(cartItem).isNotNull();
        assertThat(cartItem.getUnitPrice()).isEqualTo(product.getPrice());
    }

    @Test
    public void givenValidProductAndQuantity_whenCreateCartItem_thenTotalPriceOfCartItemEqualsProductPriceMultipliedByQuantity() {
        //given
        Product product = new Product(Title.valueOf("Product"), Amount.valueOf(10D), new Category(Title.valueOf("Category")));
        Quantity quantity = Quantity.valueOf(2);

        //when
        CartItem cartItem = new CartItem(product, quantity);

        //then
        assertThat(cartItem).isNotNull();
        assertThat(cartItem.getTotalPrice()).isEqualTo(product.getPrice().multiply(quantity.doubleValue())).isEqualTo(Amount.valueOf(20D));
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

        //then
        assertThat(cartItem1).isNotEqualTo(cartItem2);
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

        //then
        assertThat(cartItem1).isNotEqualTo(cartItem2);
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

        //then
        assertThat(cartItem1).isEqualTo(cartItem2);
    }

    @Test
    public void givenSameQuantitiesCartItem1ProductLessThanCartItem2Product_whenCompareProducts_thenReturnNegative() {
        //given
        Product product1 = new Product(Title.valueOf("Product1"), Amount.valueOf(10D), new Category(Title.valueOf("Category")));
        Product product2 = new Product(Title.valueOf("Product2"), Amount.valueOf(10D), new Category(Title.valueOf("Category")));
        Quantity quantity1 = Quantity.valueOf(1);
        Quantity quantity2 = Quantity.valueOf(1);

        //when
        CartItem cartItem1 = new CartItem(product1, quantity1);
        CartItem cartItem2 = new CartItem(product2, quantity2);

        //then
        assertThat(cartItem1).isLessThan(cartItem2);
    }

    @Test
    public void givenSameProductsCartItem1QuantityLessThanCartItem2Quantity_whenCompareProducts_thenReturnNegative() {
        //given
        Product product1 = new Product(Title.valueOf("Product"), Amount.valueOf(10D), new Category(Title.valueOf("Category")));
        Product product2 = new Product(Title.valueOf("Product"), Amount.valueOf(10D), new Category(Title.valueOf("Category")));
        Quantity quantity1 = Quantity.valueOf(1);
        Quantity quantity2 = Quantity.valueOf(2);

        //when
        CartItem cartItem1 = new CartItem(product1, quantity1);
        CartItem cartItem2 = new CartItem(product2, quantity2);

        //then
        assertThat(cartItem1).isLessThan(cartItem2);
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
        assertThat(cartItem.getQuantity()).isEqualTo(Quantity.valueOf(2).add(Quantity.valueOf(1))).isEqualTo(Quantity.valueOf(3));
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
        assertThat(cartItem.getTotalPrice()).isEqualTo(cartItem.getUnitPrice().multiply(cartItem.getQuantity().doubleValue())).isEqualTo(Amount.valueOf(10D));

        //increase quantity by 1
        //when
        cartItem.increaseQuantity(Quantity.valueOf(1));

        //then
        assertThat(cartItem.getTotalPrice()).isEqualTo(cartItem.getUnitPrice().multiply(cartItem.getQuantity().doubleValue())).isEqualTo(Amount.valueOf(20D));
    }
}
