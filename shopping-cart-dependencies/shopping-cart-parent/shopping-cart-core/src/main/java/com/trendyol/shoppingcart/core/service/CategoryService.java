package com.trendyol.shoppingcart.core.service;

import com.trendyol.shoppingcart.core.domain.Category;
import com.trendyol.shoppingcart.core.domain.value.Title;

import java.util.List;

public interface CategoryService extends CRUDService<Category, Title> {

    List<Category> getChildrenOf(Title title);
}
