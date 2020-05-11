package com.trendyol.shoppingcart.core.service;

import java.util.List;
import java.util.Optional;

public interface CRUDService<T, I> {

    T save(T t);

    Optional<T> get(I id);

    T update(T t);

    void delete(I id);

    List<T> getAll();
}
