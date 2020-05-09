package com.trendyol.shoppingcart.core.discountprovider;

import com.trendyol.shoppingcart.core.discountprovider.listener.DiscountProvidedEvent;
import com.trendyol.shoppingcart.core.domain.ShoppingCart;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class DiscountProviderGroupTest {

    @Test
    public void given_whenCreateDiscountProviderGroup_thenReturnEmptyDiscountProviderGroup() {
        //given

        //when
        DiscountProviderGroup discountProviderGroup = new DiscountProviderGroup();

        //then
        assertThat(discountProviderGroup).isNotNull();
        assertThat(discountProviderGroup.getChildDiscountProviders()).hasSize(0);
    }

    @Test
    public void givenDiscountProviderGroup_whenAddDiscountProvider_thenAddDiscountProviderAsChildProvider() {
        //given
        DiscountProviderGroup discountProviderGroup = new DiscountProviderGroup();

        //when
        DiscountProvider discountProvider = mock(DiscountProvider.class);
        discountProviderGroup.addDiscountProvider(discountProvider);

        //then
        assertThat(discountProviderGroup).isNotNull();
        assertThat(discountProviderGroup.getChildDiscountProviders()).hasSize(1);
    }

    @Test
    public void givenDiscountProviderGroup_whenAddDiscountProviderItem_thenAddDiscountProviderItemAsChildProvider() {
        //given
        DiscountProviderGroup discountProviderGroup = new DiscountProviderGroup();

        //when
        DiscountProviderItem discountProviderItem = mock(DiscountProviderItem.class);
        discountProviderGroup.addDiscountProvider(discountProviderItem);

        //then
        assertThat(discountProviderGroup).isNotNull();
        assertThat(discountProviderGroup.getChildDiscountProviders()).hasSize(1);
    }

    @Test
    public void givenDiscountProviderGroup_whenAddDiscountProviderGroup_thenAddDiscountProviderGroupAsChildProvider() {
        //given
        DiscountProviderGroup discountProviderGroup = new DiscountProviderGroup();

        //when
        DiscountProviderGroup childGroup = mock(DiscountProviderGroup.class);
        discountProviderGroup.addDiscountProvider(childGroup);

        //then
        assertThat(discountProviderGroup).isNotNull();
        assertThat(discountProviderGroup.getChildDiscountProviders()).hasSize(1);
    }

    @Test
    public void givenDiscountProviderGroup_whenAddSameDiscountProviderTwice_thenAddDiscountProviderAsChildProviderOnce() {
        //given
        DiscountProviderGroup discountProviderGroup = new DiscountProviderGroup();

        //when
        DiscountProvider discountProvider = mock(DiscountProvider.class);
        discountProviderGroup.addDiscountProvider(discountProvider);
        discountProviderGroup.addDiscountProvider(discountProvider);

        //then
        assertThat(discountProviderGroup).isNotNull();
        assertThat(discountProviderGroup.getChildDiscountProviders()).hasSize(1);
    }

    @Test
    public void givenDiscountProviderGroup_whenAddSameDiscountProviderItemTwice_thenAddDiscountProviderItemAsChildProviderOnce() {
        //given
        DiscountProviderGroup discountProviderGroup = new DiscountProviderGroup();

        //when
        DiscountProviderItem discountProviderItem = mock(DiscountProviderItem.class);
        discountProviderGroup.addDiscountProvider(discountProviderItem);
        discountProviderGroup.addDiscountProvider(discountProviderItem);

        //then
        assertThat(discountProviderGroup).isNotNull();
        assertThat(discountProviderGroup.getChildDiscountProviders()).hasSize(1);
    }

    @Test
    public void givenDiscountProviderGroup_whenAddSameDiscountProviderGroupTwice_thenAddDiscountProviderGroupAsChildProviderOnce() {
        //given
        DiscountProviderGroup discountProviderGroup = new DiscountProviderGroup();

        //when
        DiscountProviderGroup childGroup = mock(DiscountProviderGroup.class);
        discountProviderGroup.addDiscountProvider(childGroup);
        discountProviderGroup.addDiscountProvider(childGroup);

        //then
        assertThat(discountProviderGroup).isNotNull();
        assertThat(discountProviderGroup.getChildDiscountProviders()).hasSize(1);
    }

    @Test
    public void givenDiscountProviderGroup_whenAddTwoDifferentDiscountProviders_thenAddBothOfDiscountProvidersAsChildProviders() {
        //given
        DiscountProviderGroup discountProviderGroup = new DiscountProviderGroup();

        //when
        DiscountProvider discountProvider1 = mock(DiscountProvider.class);
        DiscountProvider discountProvider2 = mock(DiscountProvider.class);
        discountProviderGroup.addDiscountProvider(discountProvider1);
        discountProviderGroup.addDiscountProvider(discountProvider2);

        //then
        assertThat(discountProviderGroup).isNotNull();
        assertThat(discountProviderGroup.getChildDiscountProviders()).hasSize(2);
    }

    @Test
    public void givenDiscountProviderGroup_whenAddTwoDifferentDiscountProviderItems_thenAddBothOfDiscountProviderItemsAsChildProviders() {
        //given
        DiscountProviderGroup discountProviderGroup = new DiscountProviderGroup();

        //when
        DiscountProviderItem discountProviderItem1 = mock(DiscountProviderItem.class);
        DiscountProviderItem discountProviderItem2 = mock(DiscountProviderItem.class);
        discountProviderGroup.addDiscountProvider(discountProviderItem1);
        discountProviderGroup.addDiscountProvider(discountProviderItem2);

        //then
        assertThat(discountProviderGroup).isNotNull();
        assertThat(discountProviderGroup.getChildDiscountProviders()).hasSize(2);
    }

    @Test
    public void givenDiscountProviderGroup_whenAddTwoDifferentDiscountProviderGroups_thenAddBothOfDiscountProviderGroupsAsChildProviders() {
        //given
        DiscountProviderGroup discountProviderGroup = new DiscountProviderGroup();

        //when
        DiscountProviderGroup childGroup1 = mock(DiscountProviderGroup.class);
        DiscountProviderGroup childGroup2 = mock(DiscountProviderGroup.class);
        discountProviderGroup.addDiscountProvider(childGroup1);
        discountProviderGroup.addDiscountProvider(childGroup2);

        //then
        assertThat(discountProviderGroup).isNotNull();
        assertThat(discountProviderGroup.getChildDiscountProviders()).hasSize(2);
    }

    @Test
    public void givenDiscountProviderGroup_whenRegisterListener_thenRegisterListenerToAllChildProviders() {
        //given
        DiscountProviderGroup discountProviderGroup = new DiscountProviderGroup();
        DiscountProvider discountProvider1 = mock(DiscountProvider.class);
        DiscountProvider discountProvider2 = mock(DiscountProvider.class);
        DiscountProviderItem discountProviderItem1 = mock(DiscountProviderItem.class);
        DiscountProviderItem discountProviderItem2 = mock(DiscountProviderItem.class);
        DiscountProviderGroup childGroup1 = mock(DiscountProviderGroup.class);
        DiscountProviderGroup childGroup2 = mock(DiscountProviderGroup.class);

        discountProviderGroup.addDiscountProvider(discountProvider1);
        discountProviderGroup.addDiscountProvider(discountProvider2);
        discountProviderGroup.addDiscountProvider(discountProviderItem1);
        discountProviderGroup.addDiscountProvider(discountProviderItem2);
        discountProviderGroup.addDiscountProvider(childGroup1);
        discountProviderGroup.addDiscountProvider(childGroup2);

        ShoppingCart shoppingCart = mock(ShoppingCart.class);

        //when
        discountProviderGroup.registerDiscountProviderEventListener(shoppingCart);

        //then
        verify(discountProvider1, times(1)).registerDiscountProviderEventListener(ArgumentMatchers.eq(shoppingCart));
        verify(discountProvider2, times(1)).registerDiscountProviderEventListener(ArgumentMatchers.eq(shoppingCart));
        verify(discountProviderItem1, times(1)).registerDiscountProviderEventListener(ArgumentMatchers.eq(shoppingCart));
        verify(discountProviderItem2, times(1)).registerDiscountProviderEventListener(ArgumentMatchers.eq(shoppingCart));
        verify(childGroup1, times(1)).registerDiscountProviderEventListener(ArgumentMatchers.eq(shoppingCart));
        verify(childGroup2, times(1)).registerDiscountProviderEventListener(ArgumentMatchers.eq(shoppingCart));
    }

    @Test
    public void givenDiscountProviderGroup_whenUnregisterListener_thenUnregisterListenerFromAllChildProviders() {
        //given
        DiscountProviderGroup discountProviderGroup = new DiscountProviderGroup();
        DiscountProvider discountProvider1 = mock(DiscountProvider.class);
        DiscountProvider discountProvider2 = mock(DiscountProvider.class);
        DiscountProviderItem discountProviderItem1 = mock(DiscountProviderItem.class);
        DiscountProviderItem discountProviderItem2 = mock(DiscountProviderItem.class);
        DiscountProviderGroup childGroup1 = mock(DiscountProviderGroup.class);
        DiscountProviderGroup childGroup2 = mock(DiscountProviderGroup.class);

        discountProviderGroup.addDiscountProvider(discountProvider1);
        discountProviderGroup.addDiscountProvider(discountProvider2);
        discountProviderGroup.addDiscountProvider(discountProviderItem1);
        discountProviderGroup.addDiscountProvider(discountProviderItem2);
        discountProviderGroup.addDiscountProvider(childGroup1);
        discountProviderGroup.addDiscountProvider(childGroup2);

        ShoppingCart shoppingCart = mock(ShoppingCart.class);
        discountProviderGroup.registerDiscountProviderEventListener(shoppingCart);

        //when
        discountProviderGroup.unregisterDiscountProviderEventListener(shoppingCart);

        //then
        verify(discountProvider1, times(1)).unregisterDiscountProviderEventListener(ArgumentMatchers.eq(shoppingCart));
        verify(discountProvider2, times(1)).unregisterDiscountProviderEventListener(ArgumentMatchers.eq(shoppingCart));
        verify(discountProviderItem1, times(1)).unregisterDiscountProviderEventListener(ArgumentMatchers.eq(shoppingCart));
        verify(discountProviderItem2, times(1)).unregisterDiscountProviderEventListener(ArgumentMatchers.eq(shoppingCart));
        verify(childGroup1, times(1)).unregisterDiscountProviderEventListener(ArgumentMatchers.eq(shoppingCart));
        verify(childGroup2, times(1)).unregisterDiscountProviderEventListener(ArgumentMatchers.eq(shoppingCart));
    }

    @Test
    public void givenDiscountProviderGroup_whenNotifyListener_thenNotifyListenerOfAllChildProviders() {
        //given
        DiscountProviderGroup discountProviderGroup = new DiscountProviderGroup();
        DiscountProvider discountProvider1 = mock(DiscountProvider.class);
        DiscountProvider discountProvider2 = mock(DiscountProvider.class);
        DiscountProviderItem discountProviderItem1 = mock(DiscountProviderItem.class);
        DiscountProviderItem discountProviderItem2 = mock(DiscountProviderItem.class);
        DiscountProviderGroup childGroup1 = mock(DiscountProviderGroup.class);
        DiscountProviderGroup childGroup2 = mock(DiscountProviderGroup.class);

        discountProviderGroup.addDiscountProvider(discountProvider1);
        discountProviderGroup.addDiscountProvider(discountProvider2);
        discountProviderGroup.addDiscountProvider(discountProviderItem1);
        discountProviderGroup.addDiscountProvider(discountProviderItem2);
        discountProviderGroup.addDiscountProvider(childGroup1);
        discountProviderGroup.addDiscountProvider(childGroup2);

        //when
        DiscountProvidedEvent event = mock(DiscountProvidedEvent.class);
        discountProviderGroup.notifyDiscountProviderEventListeners(event);

        //then
        verify(discountProvider1, times(1)).notifyDiscountProviderEventListeners(ArgumentMatchers.eq(event));
        verify(discountProvider2, times(1)).notifyDiscountProviderEventListeners(ArgumentMatchers.eq(event));
        verify(discountProviderItem1, times(1)).notifyDiscountProviderEventListeners(ArgumentMatchers.eq(event));
        verify(discountProviderItem2, times(1)).notifyDiscountProviderEventListeners(ArgumentMatchers.eq(event));
        verify(childGroup1, times(1)).notifyDiscountProviderEventListeners(ArgumentMatchers.eq(event));
        verify(childGroup2, times(1)).notifyDiscountProviderEventListeners(ArgumentMatchers.eq(event));
    }

    @Test
    public void givenDiscountProviderGroup_whenProvideDiscounts_thenProvideDiscountsOfAllChildProviders() {
        //given
        DiscountProviderGroup discountProviderGroup = new DiscountProviderGroup();
        DiscountProvider discountProvider1 = mock(DiscountProvider.class);
        DiscountProvider discountProvider2 = mock(DiscountProvider.class);
        DiscountProviderItem discountProviderItem1 = mock(DiscountProviderItem.class);
        DiscountProviderItem discountProviderItem2 = mock(DiscountProviderItem.class);
        DiscountProviderGroup childGroup1 = mock(DiscountProviderGroup.class);
        DiscountProviderGroup childGroup2 = mock(DiscountProviderGroup.class);

        discountProviderGroup.addDiscountProvider(discountProvider1);
        discountProviderGroup.addDiscountProvider(discountProvider2);
        discountProviderGroup.addDiscountProvider(discountProviderItem1);
        discountProviderGroup.addDiscountProvider(discountProviderItem2);
        discountProviderGroup.addDiscountProvider(childGroup1);
        discountProviderGroup.addDiscountProvider(childGroup2);

        //when
        discountProviderGroup.provideDiscount();

        //then
        verify(discountProvider1, times(1)).provideDiscount();
        verify(discountProvider2, times(1)).provideDiscount();
        verify(discountProviderItem1, times(1)).provideDiscount();
        verify(discountProviderItem2, times(1)).provideDiscount();
        verify(childGroup1, times(1)).provideDiscount();
        verify(childGroup2, times(1)).provideDiscount();
    }


}
