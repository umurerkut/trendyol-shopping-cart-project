package com.trendyol.shoppingcart.core.service;

import java.util.List;
import java.util.Optional;

public interface CRUDService<T, I> {

    T save(T t);

    Optional<T> get(I i);

    T update(T t);

    void delete(I i);

    List<T> getAll();
}
