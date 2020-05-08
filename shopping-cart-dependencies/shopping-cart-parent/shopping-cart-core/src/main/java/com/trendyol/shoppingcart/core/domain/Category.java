package com.trendyol.shoppingcart.core.domain;

import com.trendyol.shoppingcart.core.domain.value.Title;
import com.trendyol.shoppingcart.core.exception.InvalidValueException;

import java.util.Comparator;
import java.util.Objects;

import static java.util.Comparator.naturalOrder;
import static java.util.Comparator.nullsFirst;

public class Category implements Comparable<Category> {

    private final Title title;
    private Category parent;

    public Category(Title title) {
        this(null, title);
    }

    public Category(Category parent, Title title) {

        if (title == null) {
            throw new InvalidValueException("Category title can not be null!");
        }

        this.title = title;
        this.parent = parent;
    }


    public Category getParent() {
        return parent;
    }

    public void setParent(Category parent) {
        this.parent = parent;
    }

    public Title getTitle() {
        return title;
    }

    public int depth() {
        if (this.parent == null) {
            return 0;
        } else {
            return this.parent.depth() + 1;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Category category = (Category) o;

        if (!title.equals(category.title)) return false;
        return Objects.equals(parent, category.parent);
    }

    @Override
    public int hashCode() {
        int result = title.hashCode();
        result = 31 * result + (parent != null ? parent.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Category: " +
                "Title=" + title +
                ", Parent=" + parent;
    }

    @Override
    public int compareTo(Category o) {
        if (depth() == o.depth()) {
            return Comparator
                    .comparing(Category::getParent, nullsFirst(naturalOrder()))
                    .thenComparing(Category::getTitle).compare(this, o);
        } else {
            return Integer.compare(this.depth(), o.depth());
        }
    }
}
