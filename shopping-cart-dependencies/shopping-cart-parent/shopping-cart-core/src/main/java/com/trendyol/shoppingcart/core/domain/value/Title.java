package com.trendyol.shoppingcart.core.domain.value;

import com.trendyol.shoppingcart.core.exception.InvalidValueException;

import java.util.Comparator;
import java.util.Objects;

/*
Title value object
 */
public final class Title implements Comparable<Title> {

    private final String value;

    private Title(String value) {

        if (value == null) {
            throw new InvalidValueException("Title value can not be null!");
        }

        if ("".equals(value)) {
            throw new InvalidValueException("Title value can not be blank!");
        }

        this.value = value;
    }

    public static Title valueOf(String value) {
        return new Title(value);
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Title title = (Title) o;
        return value.equals(title.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public int compareTo(Title o) {
        return Comparator.comparing(Title::getValue).compare(this, o);
    }
}
