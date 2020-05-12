package com.trendyol.shoppingcart.core.domain;

import com.trendyol.shoppingcart.core.domain.value.Title;
import com.trendyol.shoppingcart.core.exception.InvalidValueException;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import static java.util.Comparator.naturalOrder;
import static java.util.Comparator.nullsLast;

public class Category implements Comparable<Category> {

    private Category parent;
    private final Title title;
    private final Map<Title, Category> children = new HashMap<>();

    public Category(Title title) {
        this(null, title);
    }

    public Category(Category parent, Title title) {
        if (title == null) {
            throw new InvalidValueException("Category title can not be null!");
        }
        this.title = title;
        this.parent = parent;
        if (this.parent != null) {
            this.parent.addChild(this);
        }
    }

    public Category addChild(Category category) {
        children.put(category.getTitle(), category);
        return category;
    }

    public Category addChild(Title title) {
        Category category = new Category(title);
        children.put(category.getTitle(), category);
        return category;
    }

    public Stream<Category> descendants() {
        return Stream.concat(
                Stream.of(this),
                children.values().stream().flatMap(Category::descendants));
    }

    public boolean isDescendant(Category category) {
        return descendants()
                .anyMatch(d -> d.getTitle().equals(category.getTitle()));
    }

    public void removeChild(Title title) {
        children.remove(title);
    }

    public int getChildrenCount() {
        return this.children.size();
    }

    public boolean containsChild(Title title) {
        return children.get(title) != null;
    }

    public boolean hasChildren() {
        return this.children.size() > 0;
    }

    public Title getTitle() {
        return title;
    }

    public Category getParent() {
        return parent;
    }

    public void setParent(Category parent) {
        this.parent = parent;
    }

    public Map<Title, Category> getChildren() {
        return children;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Category category = (Category) o;

        if (!Objects.equals(parent, category.parent)) return false;
        if (!title.equals(category.title)) return false;
        return children.equals(category.children);
    }

    @Override
    public String toString() {
        return "Category: " +
                "parent=" + parent +
                ", title=" + title +
                ", children=" + children;
    }

    @Override
    public int hashCode() {
        int result = parent != null ? parent.hashCode() : 0;
        result = 31 * result + title.hashCode();
        result = 31 * result + children.hashCode();
        return result;
    }

    @Override
    public int compareTo(Category o) {
        return Comparator
                .comparing(Category::getParent, nullsLast(naturalOrder()))
                .thenComparing(Category::getTitle)
                .thenComparing(Category::getChildrenCount)
                .compare(this, o);
    }
}
