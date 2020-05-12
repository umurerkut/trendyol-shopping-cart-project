package com.trendyol.shoppingcart.core.domain;

import com.trendyol.shoppingcart.core.domain.value.Amount;
import com.trendyol.shoppingcart.core.domain.value.Title;
import com.trendyol.shoppingcart.core.exception.InvalidValueException;

import java.util.Comparator;

public final class Product implements Comparable<Product> {

    private final Title title;

    private final Amount price;

    private final Category category;

    public Product(Title title, Amount price, Category category) {

        if (title == null) {
            throw new InvalidValueException("Product title can not be null!");
        }

        if (price == null) {
            throw new InvalidValueException("Product price can not be null!");
        }

        if (price.isZero()) {
            throw new InvalidValueException("Product price can not be zero!");
        }

        if (category == null) {
            throw new InvalidValueException("Product category can not be null!");
        }

        this.title = title;
        this.price = price;
        this.category = category;
    }

    public Title getTitle() {
        return title;
    }

    public Amount getPrice() {
        return price;
    }

    public Category getCategory() {
        return category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;

        if (!title.equals(product.title)) return false;
        if (!price.equals(product.price)) return false;
        return category.equals(product.category);
    }

    @Override
    public int hashCode() {
        int result = title.hashCode();
        result = 31 * result + price.hashCode();
        result = 31 * result + category.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Product: " +
                "Title=" + title +
                ", Price=" + price +
                ", Category=" + category;
    }

    @Override
    public int compareTo(Product o) {
        return Comparator
                .comparing(Product::getTitle)
                .thenComparing(Product::getPrice)
                .compare(this, o);
    }
}

