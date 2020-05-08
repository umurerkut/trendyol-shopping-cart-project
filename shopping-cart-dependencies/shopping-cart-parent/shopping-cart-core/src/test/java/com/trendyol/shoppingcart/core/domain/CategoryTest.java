package com.trendyol.shoppingcart.core.domain;

import com.trendyol.shoppingcart.core.domain.value.Title;
import com.trendyol.shoppingcart.core.exception.InvalidValueException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CategoryTest {

    @Test
    public void givenNullParent_whenCreateCategory_thenReturnCategory() {
        //given
        Category parent = null;
        Title title = Title.valueOf("Title");

        //when
        Category category = new Category(parent, title);

        //then
        assertNotNull(category);
        assertNull(category.getParent());
        assertEquals(category.getTitle(), title);
    }

    @Test
    public void givenNullTitle_whenCreateCategory_thenThrowInvalidValueException() {
        //given
        Category parent = null;
        Title title = null;

        //when
        assertNull(title);
        InvalidValueException exception = assertThrows(InvalidValueException.class, () -> new Category(parent, title));

        //then
        assertNotNull(exception);
        assertEquals(exception.getMessage(), "Category title can not be null!");
    }

    @Test
    public void givenSameTitles_whenCheckEquality_thenReturnTrue() {
        //given
        Category category1 = new Category(Title.valueOf("Category 1"));
        Category category2 = new Category(Title.valueOf("Category 1"));

        //when
        boolean result = category1.equals(category2);

        //then
        assertTrue(result);
    }

    @Test
    public void givenSameTitlesAndSameParents_whenCheckEquality_thenReturnTrue() {
        //given
        Category parent = new Category(Title.valueOf("Parent Category"));
        Category category1 = new Category(parent, Title.valueOf("Category 1"));
        Category category2 = new Category(parent, Title.valueOf("Category 1"));

        //when
        boolean result = category1.equals(category2);

        //then
        assertTrue(result);
    }

    @Test
    public void givenDifferentTitles_whenCheckEquality_thenReturnFalse() {
        //given
        Category category1 = new Category(Title.valueOf("Category 1"));
        Category category2 = new Category(Title.valueOf("Category 2"));

        //when
        boolean result = category1.equals(category2);

        //then
        assertFalse(result);
    }

    @Test
    public void givenDifferentTitlesAndSameParents_whenCheckEquality_thenReturnFalse() {
        //given
        Category parent = new Category(Title.valueOf("Parent Category"));
        Category category1 = new Category(parent, Title.valueOf("Category 1"));
        Category category2 = new Category(parent, Title.valueOf("Category 2"));

        //when
        boolean result = category1.equals(category2);

        //then
        assertFalse(result);
    }

    @Test
    public void givenDifferentTitlesAndDifferentParents_whenCheckEquality_thenReturnFalse() {
        //given
        Category parent1 = new Category(Title.valueOf("Parent Category 1"));
        Category parent2 = new Category(Title.valueOf("Parent Category 2"));
        Category category1 = new Category(parent1, Title.valueOf("Category 1"));
        Category category2 = new Category(parent2, Title.valueOf("Category 2"));

        //when
        boolean result = category1.equals(category2);

        //then
        assertFalse(result);
    }

    @Test
    public void givenSameTitlesAndDifferentParents_whenCheckEquality_thenReturnFalse() {
        //given
        Category parent1 = new Category(Title.valueOf("Parent Category 1"));
        Category parent2 = new Category(Title.valueOf("Parent Category 2"));
        Category category1 = new Category(parent1, Title.valueOf("Category 1"));
        Category category2 = new Category(parent2, Title.valueOf("Category 1"));

        //when
        boolean result = category1.equals(category2);

        //then
        assertFalse(result);
    }

    @Test
    public void givenCategory1DeeperThanCategory2_whenCompareCategories_thenReturnPositive() {
        //given
        Category parent1 = new Category(Title.valueOf("Parent Category 1"));
        Category category1 = new Category(parent1, Title.valueOf("Category 1"));
        Category category2 = new Category(Title.valueOf("Category 2"));

        //when
        int result = category1.compareTo(category2);

        //then
        assertTrue(result > 0);
    }

    @Test
    public void givenCategory1ShallowerThanCategory2_whenCompareCategories_thenReturnNegative() {
        //given
        Category parent2 = new Category(Title.valueOf("Parent Category 2"));
        Category category1 = new Category(Title.valueOf("Category 1"));
        Category category2 = new Category(parent2, Title.valueOf("Category 2"));

        //when
        int result = category1.compareTo(category2);

        //then
        assertTrue(result < 0);
    }

    @Test
    public void givenSameDepthSameParentAndCategory1TitleLesserThanCategory2Title_whenCompareCategories_thenReturnNegative() {
        //given
        Category parent = new Category(Title.valueOf("Parent Category"));
        Category category1 = new Category(parent, Title.valueOf("Category 1"));
        Category category2 = new Category(parent, Title.valueOf("Category 2"));

        //when
        int result = category1.compareTo(category2);

        //then
        assertTrue(result < 0);
    }

    @Test
    public void givenSameDepthParent1TitleLesserThanParent2TitleAndCategory1TitleLesserThanCategory2Title_whenCompareCategories_thenReturnNegative() {
        //given
        Category parent1 = new Category(Title.valueOf("Parent Category 1"));
        Category parent2 = new Category(Title.valueOf("Parent Category 2"));
        Category category1 = new Category(parent1, Title.valueOf("Category 1"));
        Category category2 = new Category(parent2, Title.valueOf("Category 2"));

        //when
        int result = category1.compareTo(category2);

        //then
        assertTrue(result < 0);
    }

    @Test
    public void givenSameDepthParent1TitleLesserThanParent2TitleAndCategoryTitlesSame_whenCompareCategories_thenReturnNegative() {
        //given
        Category parent1 = new Category(Title.valueOf("Parent Category 1"));
        Category parent2 = new Category(Title.valueOf("Parent Category 2"));
        Category category1 = new Category(parent1, Title.valueOf("Category 1"));
        Category category2 = new Category(parent2, Title.valueOf("Category 1"));

        //when
        int result = category1.compareTo(category2);

        //then
        assertTrue(result < 0);
    }

}
