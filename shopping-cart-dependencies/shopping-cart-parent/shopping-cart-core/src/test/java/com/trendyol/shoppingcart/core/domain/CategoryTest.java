package com.trendyol.shoppingcart.core.domain;

import com.trendyol.shoppingcart.core.domain.value.Title;
import com.trendyol.shoppingcart.core.exception.InvalidValueException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class CategoryTest {

    @Test
    public void givenNoChildrenAndValidTitle_whenCreateCategory_thenReturnCategory() {
        //given
        Category parent = null;
        Title title = Title.valueOf("Title");

        //when
        Category category = new Category(parent, title);

        //then
        assertThat(category).isNotNull();
        assertThat(category.getParent()).isNull();
        assertThat(category.getChildrenCount()).isZero();
        assertThat(category.getTitle()).isEqualTo(title);
    }

    @Test
    public void givenValidParentAndChildAndTitle_whenCreateCategory_thenReturnCategory() {
        //given
        Category parent = new Category(Title.valueOf("Parent"));

        //when
        Title title = Title.valueOf("Title");
        Category category = parent.addChild(title);
        category.setParent(parent);
        Category child = category.addChild(Title.valueOf("Child"));
        child.setParent(category);

        //then
        assertThat(category).isNotNull();
        assertThat(category.getParent()).isNotNull();
        assertThat(category.getChildrenCount()).isEqualTo(1);
        assertThat(category.containsChild(child.getTitle())).isTrue();
        assertThat(category.getTitle()).isEqualTo(title);
    }

    @Test
    public void givenNullTitle_whenCreateCategory_thenThrowInvalidValueException() {
        //given
        Category parent = null;
        Title title = null;

        //when
        assertThat(title).isNull();
        Throwable throwable = catchThrowable(() -> {
            Category category = new Category(parent, title);
        });

        //then
        assertThat(throwable)
                .isNotNull()
                .isInstanceOf(InvalidValueException.class)
                .hasMessage("Category title can not be null!");
    }

    @Test
    public void givenSameTitles_whenCheckEquality_thenReturnTrue() {
        //given
        Category parent = new Category(Title.valueOf("Parent"));
        Title title = Title.valueOf("Category 1");

        //when
        Category category1 = parent.addChild(title);
        Category category2 = parent.addChild(title);

        //then
        assertThat(category1).isEqualTo(category2);
    }


    @Test
    public void givenDifferentTitles_whenCheckEquality_thenReturnFalse() {
        //given
        Title title1 = Title.valueOf("Category 1");
        Title title2 = Title.valueOf("Category 2");

        //when
        Category category1 = new Category(title1);
        Category category2 = new Category(title2);

        //then
        assertThat(category1).isNotEqualTo(category2);
    }

    @Test
    public void givenDifferentTitlesAndSameParents_whenCheckEquality_thenReturnFalse() {
        //given
        Category parent = new Category(Title.valueOf("Parent Category"));
        Title title1 = Title.valueOf("Category 1");
        Title title2 = Title.valueOf("Category 2");

        //when
        Category category1 = new Category(parent, title1);
        Category category2 = new Category(parent, title2);

        //then
        assertThat(category1).isNotEqualTo(category2);
    }

    @Test
    public void givenDifferentTitlesAndDifferentParents_whenCheckEquality_thenReturnFalse() {
        //given
        Category parent1 = new Category(Title.valueOf("Parent Category 1"));
        Category parent2 = new Category(Title.valueOf("Parent Category 2"));
        Title title1 = Title.valueOf("Category 1");
        Title title2 = Title.valueOf("Category 2");

        //when
        Category category1 = new Category(parent1, title1);
        Category category2 = new Category(parent2, title2);

        //then
        assertThat(category1).isNotEqualTo(category2);
    }

    @Test
    public void givenSameTitlesAndDifferentParents_whenCheckEquality_thenReturnFalse() {
        //given
        Category parent1 = new Category(Title.valueOf("Parent Category 1"));
        Category parent2 = new Category(Title.valueOf("Parent Category 2"));
        Title title = Title.valueOf("Category");

        //when
        Category category1 = new Category(parent1, title);
        Category category2 = new Category(parent2, title);

        //then
        assertThat(category1).isNotEqualTo(category2);
    }

    @Test
    public void givenSameDepthSameParentAndCategory1TitleLessThanCategory2Title_whenCompareCategories_thenReturnNegative() {
        //given
        Category parent = new Category(Title.valueOf("Parent Category"));
        Title title1 = Title.valueOf("Category 1");
        Title title2 = Title.valueOf("Category 2");

        //when
        Category category1 = new Category(parent, title1);
        Category category2 = new Category(parent, title2);

        //then
        assertThat(category1).isLessThan(category2);
    }

    @Test
    public void givenSameDepthParent1TitleLessThanParent2TitleAndCategory1TitleLessThanCategory2Title_whenCompareCategories_thenReturnNegative() {
        //given
        Category parent1 = new Category(Title.valueOf("Parent Category 1"));
        Category parent2 = new Category(Title.valueOf("Parent Category 2"));
        Title title1 = Title.valueOf("Category 1");
        Title title2 = Title.valueOf("Category 2");

        //when
        Category category1 = new Category(parent1, title1);
        Category category2 = new Category(parent2, title2);

        //then
        assertThat(category1).isLessThan(category2);
    }

    @Test
    public void givenSameDepthParent1TitleLessThanParent2TitleAndCategoryTitlesSame_whenCompareCategories_thenReturnNegative() {
        //given
        Category parent1 = new Category(Title.valueOf("Parent Category 1"));
        Category parent2 = new Category(Title.valueOf("Parent Category 2"));
        Title title = Title.valueOf("Category");

        //when
        Category category1 = parent1.addChild(title);
        category1.setParent(parent1);
        Category category2 = parent2.addChild(title);
        category2.setParent(parent2);

        //then
        assertThat(category1).isLessThan(category2);
    }

}
