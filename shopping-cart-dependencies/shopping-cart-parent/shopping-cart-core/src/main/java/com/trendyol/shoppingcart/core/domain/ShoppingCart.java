package com.trendyol.shoppingcart.core.domain;

import com.trendyol.shoppingcart.core.discountprovider.listener.DiscountProvidedEvent;
import com.trendyol.shoppingcart.core.discountprovider.listener.DiscountProviderEventListener;
import com.trendyol.shoppingcart.core.domain.discount.Discount;
import com.trendyol.shoppingcart.core.domain.value.Amount;
import com.trendyol.shoppingcart.core.domain.value.DiscountName;
import com.trendyol.shoppingcart.core.domain.value.Quantity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public final class ShoppingCart implements DiscountProviderEventListener {

    private final static Logger logger = LoggerFactory.getLogger(ShoppingCart.class);
    private Long id;
    private Amount cartAmount;
    private Amount totalDiscount;
    private Amount deliveryCost;
    private Amount totalAmount;

    private final Set<CartItem> cartItems = new LinkedHashSet<>();
    private final Map<DiscountName, Discount> discountMap = new LinkedHashMap<>();


    public ShoppingCart() {
        this.cartAmount = Amount.ofZero();
        this.totalDiscount = Amount.ofZero();
        this.deliveryCost = Amount.ofZero();
        this.totalAmount = Amount.ofZero();
    }

    public void addProduct(Product product, Quantity quantity) {
        CartItem cartItem;
        try {
            cartItem = cartItems
                    .stream()
                    .filter(item -> product.equals(item.getProduct()))
                    .findFirst()
                    .orElseThrow(NoSuchElementException::new);
            cartItem.increaseQuantity(quantity);
        } catch (NoSuchElementException e) {
            cartItem = new CartItem(product, quantity);
            cartItems.add(cartItem);
        }

        logger.info("{} {} were added to cart.", quantity, product.getTitle());
        cartAmount = cartAmount.add(product.getPrice().multiply(quantity.doubleValue()));
        logger.info("Cart amount after the product addition: {}.", cartAmount);
    }

    @Override
    public void discountProvided(DiscountProvidedEvent event) {
        if (event != null) {
            event.getDiscount().addTo(this);
        }
    }

    public void applyDiscounts() {
        discountMap.values().forEach(this::applyDiscount);
        this.totalAmount = cartAmount;
    }

    public void applyDeliveryCost() {
        if (deliveryCost != null && deliveryCost.isPositive()) {
            this.totalAmount = totalAmount.add(deliveryCost);
        }
    }

    private void applyDiscount(Discount discount) {
        if (discount.getDiscountAmount().isLessThan(cartAmount)) {
            logger.info("Discount is applied: {}", discount);
            cartAmount = cartAmount.subtract(discount.getDiscountAmount());
        } else {
            logger.info("Discount amount {} is greater than cart amount {}!", discount.getDiscountAmount(), cartAmount);
            cartAmount = Amount.ofZero();
        }

        logger.info("Total amount after the discount: {}", cartAmount);
        totalDiscount = totalDiscount.add(discount.getDiscountAmount());
    }

    public void print() {
        logger.info("Shopping Cart Info:");
        cartItems
                .stream()
                .collect(Collectors.groupingBy(CartItem::getCategory))
                .forEach((key, value) -> {
                    logger.info("{}", key);
                    value.forEach(cartItem -> logger.info("{}", cartItem));
                });


        logger.info("Cart Amount: {}", getCartAmountWithoutDiscount());

        discountMap.forEach((key, value) -> logger.info("{}: {}", key, value.getDiscountAmount()));
        logger.info("Total Discount Amount: {}", getTotalDiscount());
        logger.info("Total Cart Amount After Discount(s): {}", getCartAmount());
        logger.info("Delivery Cost: {}", getDeliveryCost());
        logger.info("Total Amount(Cart Amount After Discount(s) + Delivery Cost): {}", getTotalAmount());
    }

    public Amount getCartAmountWithoutDiscount() {
        return Amount.valueOf(cartItems.stream().mapToDouble(cartItem -> cartItem.getTotalPrice().doubleValue()).sum());
    }

    public Amount getTotalPriceOfProductInCart(Product product) {
        return Amount.valueOf(cartItems.stream().filter(cartItem -> cartItem.getProduct().equals(product)).mapToDouble(cartItem -> cartItem.getTotalPrice().doubleValue()).sum());
    }

    public Quantity getQuantityOfProductInCart(Product product) {
        return cartItems.stream()
                .filter(cartItem -> cartItem.getProduct().equals(product))
                .findFirst()
                .map(CartItem::getQuantity)
                .orElse(Quantity.ofZero());
    }

    public Quantity getTotalQuantityOfProductsInCart() {
        return Quantity.valueOf(cartItems.stream().mapToInt(cartItem -> cartItem.getQuantity().intValue()).sum());

    }

    public Quantity getNumberOfDistinctProductInCart() {
        return Quantity.valueOf(cartItems.stream().map(CartItem::getProduct).collect(Collectors.toSet()).size());
    }

    public Quantity getNumberOfDistinctCategoryInCart() {
        return Quantity.valueOf(cartItems.stream().map(CartItem::getCategory).collect(Collectors.toSet()).size());
    }

    public Boolean containsProduct(Product product) {
        return cartItems.stream().anyMatch(cartItem -> cartItem.getProduct().equals(product));
    }

    public Amount getTotalAmount() {
        return totalAmount;
    }

    public Amount getCartAmount() {
        return cartAmount;
    }

    public Amount getDeliveryCost() {
        return deliveryCost;
    }

    public Amount getTotalDiscount() {
        return totalDiscount;
    }

    public void setDeliveryCost(Amount deliveryCost) {
        this.deliveryCost = deliveryCost;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<CartItem> getCartItems() {
        return cartItems;
    }

    public Map<DiscountName, Discount> getDiscountMap() {
        return discountMap;
    }
}
