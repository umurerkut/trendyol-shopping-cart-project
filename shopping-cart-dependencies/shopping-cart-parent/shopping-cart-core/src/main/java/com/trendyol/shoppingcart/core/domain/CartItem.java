package com.trendyol.shoppingcart.core.domain;

import com.trendyol.shoppingcart.core.domain.value.Amount;
import com.trendyol.shoppingcart.core.domain.value.Quantity;
import com.trendyol.shoppingcart.core.exception.InvalidValueException;

import java.util.Comparator;

public final class CartItem implements Comparable<CartItem> {

    private final Product product;

    private final Category category;

    private final Amount unitPrice;

    private Quantity quantity;

    private Amount totalPrice = Amount.ofZero();

    public CartItem(Product product, Quantity quantity) {

        if (product == null) {
            throw new InvalidValueException("Product can not be null!");
        }

        if (quantity == null) {
            throw new InvalidValueException("Quantity can not be null!");
        }

        if (quantity.isZero()) {
            throw new InvalidValueException("Quantity can not be zero!");
        }

        this.product = product;
        this.quantity = quantity;
        this.category = product.getCategory();
        this.unitPrice = product.getPrice();

        calculateTotalPrice();
    }

    public void calculateTotalPrice() {
        this.totalPrice = unitPrice.multiply(quantity.doubleValue());
    }

    public void increaseQuantity(Quantity newQuantity) {
        quantity = quantity.add(newQuantity);
        calculateTotalPrice();
    }

    public Product getProduct() {
        return product;
    }

    public Quantity getQuantity() {
        return quantity;
    }

    public void setQuantity(Quantity quantity) {
        this.quantity = quantity;
    }

    public Category getCategory() {
        return category;
    }

    public Amount getUnitPrice() {
        return unitPrice;
    }

    public Amount getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Amount totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CartItem cartItem = (CartItem) o;

        if (!product.equals(cartItem.product)) return false;
        if (!category.equals(cartItem.category)) return false;
        if (!unitPrice.equals(cartItem.unitPrice)) return false;
        if (!quantity.equals(cartItem.quantity)) return false;
        return totalPrice.equals(cartItem.totalPrice);
    }

    @Override
    public int hashCode() {
        int result = product.hashCode();
        result = 31 * result + category.hashCode();
        result = 31 * result + unitPrice.hashCode();
        result = 31 * result + quantity.hashCode();
        result = 31 * result + totalPrice.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Product=" + product.getTitle() +
                ", UnitPrice=" + unitPrice +
                ", Quantity=" + quantity +
                ", TotalPrice=" + totalPrice;
    }

    @Override
    public int compareTo(CartItem o) {
        return Comparator.comparing(CartItem::getProduct)
                .thenComparing(CartItem::getQuantity)
                .compare(this, o);
    }
}
