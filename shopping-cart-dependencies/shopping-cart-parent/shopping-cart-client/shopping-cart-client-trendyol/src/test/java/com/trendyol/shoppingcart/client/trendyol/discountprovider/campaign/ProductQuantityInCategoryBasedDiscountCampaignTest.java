package com.trendyol.shoppingcart.client.trendyol.discountprovider.campaign;


import com.trendyol.shoppingcart.client.trendyol.domain.value.CampaignName;
import com.trendyol.shoppingcart.client.trendyol.domain.value.Rate;
import com.trendyol.shoppingcart.core.discountprovider.listener.DiscountProvidedEvent;
import com.trendyol.shoppingcart.core.domain.Category;
import com.trendyol.shoppingcart.core.domain.ShoppingCart;
import com.trendyol.shoppingcart.core.domain.value.Amount;
import com.trendyol.shoppingcart.core.domain.value.Quantity;
import com.trendyol.shoppingcart.core.domain.value.Title;
import com.trendyol.shoppingcart.core.exception.InvalidValueException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import java.time.Duration;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.*;

public class ProductQuantityInCategoryBasedDiscountCampaignTest {

    @Test
    public void givenDiscount_whenCampaignNameIsNull_thenThrowInvalidValueException() {
        //given
        CampaignName campaignName = null;
        assertThat(campaignName).isNull();
        Category category = new Category(Title.valueOf("Category"));
        assertThat(category).isNotNull();
        Quantity minimumProductQuantityInCategory = Quantity.valueOf(10);
        assertThat(minimumProductQuantityInCategory).isNotNull();
        Rate discountRate = Rate.valueOf(10D);
        assertThat(discountRate).isNotNull();

        //when
        Throwable throwable = catchThrowable(() -> {
            ProductQuantityInCategoryBasedDiscountCampaign campaign = new ProductQuantityInCategoryBasedDiscountCampaign(campaignName, category, minimumProductQuantityInCategory, discountRate);
        });

        //then
        assertThat(throwable)
                .isNotNull()
                .isInstanceOf(InvalidValueException.class)
                .hasMessage("Campaign name can not be null!");
    }

    @Test
    public void givenDiscountMount_whenCreateProductQuantityInCategoryBasedDiscountCampaign_thenReturnMinimumCartAmountBasedAmountDiscountCoupon() {
        //given
        CampaignName campaignName = CampaignName.valueOf("CAMPAIGN");
        assertThat(campaignName).isNotNull();
        Category category = new Category(Title.valueOf("Category"));
        assertThat(category).isNotNull();
        Quantity minimumProductQuantityInCategory = Quantity.valueOf(10);
        assertThat(minimumProductQuantityInCategory).isNotNull();
        Amount discountAmount = Amount.valueOf(10D);
        assertThat(discountAmount).isNotNull();

        //when
        ProductQuantityInCategoryBasedDiscountCampaign campaign = new ProductQuantityInCategoryBasedDiscountCampaign(campaignName, category, minimumProductQuantityInCategory, discountAmount);

        //then
        Assertions.assertThat(campaign).isNotNull();
        Assertions.assertThat(campaign.getDiscount()).isNotNull();
        Assertions.assertThat(campaign.getFinishTime()).isNull();
    }

    @Test
    public void givenDiscountRate_whenCreateProductQuantityInCategoryBasedDiscountCampaign_thenReturnMinimumCartAmountBasedRateDiscountCoupon() {
        //given
        CampaignName campaignName = CampaignName.valueOf("CAMPAIGN");
        assertThat(campaignName).isNotNull();
        Category category = new Category(Title.valueOf("Category"));
        assertThat(category).isNotNull();
        Quantity minimumProductQuantityInCategory = Quantity.valueOf(10);
        assertThat(minimumProductQuantityInCategory).isNotNull();
        Rate discountRate = Rate.valueOf(10D);
        assertThat(discountRate).isNotNull();

        //when
        ProductQuantityInCategoryBasedDiscountCampaign campaign = new ProductQuantityInCategoryBasedDiscountCampaign(campaignName, category, minimumProductQuantityInCategory, discountRate);

        //then
        Assertions.assertThat(campaign).isNotNull();
        Assertions.assertThat(campaign.getDiscount()).isNotNull();
        Assertions.assertThat(campaign.getFinishTime()).isNull();
    }

    @Test
    public void givenValidDiscountAndFinishTime_whenCreateProductQuantityInCategoryBasedDiscountCampaign_thenReturnProductQuantityInCategoryBasedDiscountCampaign() {
        //given
        CampaignName campaignName = CampaignName.valueOf("CAMPAIGN");
        assertThat(campaignName).isNotNull();
        Category category = new Category(Title.valueOf("Category"));
        assertThat(category).isNotNull();
        Quantity minimumProductQuantityInCategory = Quantity.valueOf(10);
        assertThat(minimumProductQuantityInCategory).isNotNull();
        Amount discountAmount = Amount.valueOf(10D);
        assertThat(discountAmount).isNotNull();
        Instant startTime = Instant.now().minus(Duration.ofDays(10));
        assertThat(startTime).isNotNull();
        Instant finishTime = Instant.now().plus(Duration.ofDays(10));
        assertThat(finishTime).isNotNull();

        //when
        ProductQuantityInCategoryBasedDiscountCampaign campaign = new ProductQuantityInCategoryBasedDiscountCampaign(campaignName, category, minimumProductQuantityInCategory, discountAmount, startTime, finishTime);

        //then
        Assertions.assertThat(campaign).isNotNull();
        Assertions.assertThat(campaign.getDiscount()).isNotNull();
        Assertions.assertThat(campaign.getFinishTime()).isNotNull().isEqualTo(finishTime);
    }

    @Test
    public void givenProductQuantityInCategoryBasedDiscountCampaign_whenFinishTimeNull_thenIsValid() {
        //given
        CampaignName campaignName = CampaignName.valueOf("CAMPAIGN");
        assertThat(campaignName).isNotNull();
        Category category = new Category(Title.valueOf("Category"));
        assertThat(category).isNotNull();
        Quantity minimumProductQuantityInCategory = Quantity.valueOf(10);
        assertThat(minimumProductQuantityInCategory).isNotNull();
        Amount discountAmount = Amount.valueOf(10D);
        assertThat(discountAmount).isNotNull();

        ProductQuantityInCategoryBasedDiscountCampaign campaign = new ProductQuantityInCategoryBasedDiscountCampaign(campaignName, category, minimumProductQuantityInCategory, discountAmount);
        Assertions.assertThat(campaign.getFinishTime()).isNull();

        //when
        boolean result = campaign.isValid();

        //then
        assertThat(result).isTrue();
    }

    @Test
    public void givenProductQuantityInCategoryBasedDiscountCampaign_whenFinishTimeAfterNow_thenIsValid() {
        //given
        CampaignName campaignName = CampaignName.valueOf("CAMPAIGN");
        assertThat(campaignName).isNotNull();
        Category category = new Category(Title.valueOf("Category"));
        assertThat(category).isNotNull();
        Quantity minimumProductQuantityInCategory = Quantity.valueOf(10);
        assertThat(minimumProductQuantityInCategory).isNotNull();
        Amount discountAmount = Amount.valueOf(10D);
        assertThat(discountAmount).isNotNull();
        Instant startTime = Instant.now().minus(Duration.ofDays(10));
        assertThat(startTime).isNotNull();
        Instant finishTime = Instant.now().plus(Duration.ofDays(10));
        assertThat(finishTime).isNotNull();

        ProductQuantityInCategoryBasedDiscountCampaign campaign = new ProductQuantityInCategoryBasedDiscountCampaign(campaignName, category, minimumProductQuantityInCategory, discountAmount, startTime, finishTime);
        Assertions.assertThat(campaign.getFinishTime()).isNotNull();

        //when
        boolean result = campaign.isValid();

        //then
        assertThat(result).isTrue();
    }

    @Test
    public void givenProductQuantityInCategoryBasedDiscountCampaign_whenFinishTimeBeforeNow_thenIsNotValid() {
        //given
        CampaignName campaignName = CampaignName.valueOf("CAMPAIGN");
        assertThat(campaignName).isNotNull();
        Category category = new Category(Title.valueOf("Category"));
        assertThat(category).isNotNull();
        Quantity minimumProductQuantityInCategory = Quantity.valueOf(10);
        assertThat(minimumProductQuantityInCategory).isNotNull();
        Amount discountAmount = Amount.valueOf(10D);
        assertThat(discountAmount).isNotNull();
        Instant startTime = Instant.now().minus(Duration.ofDays(10));
        assertThat(startTime).isNotNull();
        Instant finishTime = Instant.now().minus(Duration.ofDays(1));
        assertThat(finishTime).isNotNull();

        ProductQuantityInCategoryBasedDiscountCampaign campaign = new ProductQuantityInCategoryBasedDiscountCampaign(campaignName, category, minimumProductQuantityInCategory, discountAmount, startTime, finishTime);
        Assertions.assertThat(campaign.getFinishTime()).isNotNull();

        //when
        boolean result = campaign.isValid();

        //then
        assertThat(result).isFalse();
    }

    @Test
    public void givenProductQuantityInCategoryBasedDiscountCampaign_whenRegisterShoppingCartAsListener_thenAddShoppingCartToListenerList() {
        //given
        CampaignName campaignName = CampaignName.valueOf("CAMPAIGN");
        assertThat(campaignName).isNotNull();
        Category category = new Category(Title.valueOf("Category"));
        assertThat(category).isNotNull();
        Quantity minimumProductQuantityInCategory = Quantity.valueOf(10);
        assertThat(minimumProductQuantityInCategory).isNotNull();
        Amount discountAmount = Amount.valueOf(10D);
        assertThat(discountAmount).isNotNull();

        ProductQuantityInCategoryBasedDiscountCampaign campaign = new ProductQuantityInCategoryBasedDiscountCampaign(campaignName, category, minimumProductQuantityInCategory, discountAmount);
        Assertions.assertThat(campaign.getFinishTime()).isNull();

        //when
        ShoppingCart shoppingCart = mock(ShoppingCart.class);
        campaign.registerDiscountProviderEventListener(shoppingCart);

        //then
        Assertions.assertThat(campaign.getDiscountProviderEventListenerList()).hasSize(1);
        Assertions.assertThat(campaign.getDiscountProviderEventListenerList()).contains(shoppingCart);
    }

    @Test
    public void givenProductQuantityInCategoryBasedDiscountCampaign_whenUnregisterShoppingCartAsListener_thenRemoveShoppingCartToListenerList() {
        //given
        CampaignName campaignName = CampaignName.valueOf("CAMPAIGN");
        assertThat(campaignName).isNotNull();
        Category category = new Category(Title.valueOf("Category"));
        assertThat(category).isNotNull();
        Quantity minimumProductQuantityInCategory = Quantity.valueOf(10);
        assertThat(minimumProductQuantityInCategory).isNotNull();
        Amount discountAmount = Amount.valueOf(10D);
        assertThat(discountAmount).isNotNull();

        ProductQuantityInCategoryBasedDiscountCampaign campaign = new ProductQuantityInCategoryBasedDiscountCampaign(campaignName, category, minimumProductQuantityInCategory, discountAmount);
        Assertions.assertThat(campaign.getFinishTime()).isNull();

        ShoppingCart shoppingCart = mock(ShoppingCart.class);
        campaign.registerDiscountProviderEventListener(shoppingCart);

        //when
        campaign.unregisterDiscountProviderEventListener(shoppingCart);

        //then
        Assertions.assertThat(campaign.getDiscountProviderEventListenerList()).hasSize(0);
        Assertions.assertThat(campaign.getDiscountProviderEventListenerList()).doesNotContain(shoppingCart);
    }

    @Test
    public void givenProductQuantityInCategoryBasedDiscountCampaign_whenNotifyListeners_thenNotifyShoppingCartAboutDiscountProvided() {
        //given
        CampaignName campaignName = CampaignName.valueOf("CAMPAIGN");
        assertThat(campaignName).isNotNull();
        Category category = new Category(Title.valueOf("Category"));
        assertThat(category).isNotNull();
        Quantity minimumProductQuantityInCategory = Quantity.valueOf(10);
        assertThat(minimumProductQuantityInCategory).isNotNull();
        Amount discountAmount = Amount.valueOf(10D);
        assertThat(discountAmount).isNotNull();

        ProductQuantityInCategoryBasedDiscountCampaign campaign = new ProductQuantityInCategoryBasedDiscountCampaign(campaignName, category, minimumProductQuantityInCategory, discountAmount);
        Assertions.assertThat(campaign.getFinishTime()).isNull();

        ShoppingCart shoppingCart = mock(ShoppingCart.class);
        campaign.registerDiscountProviderEventListener(shoppingCart);

        //when
        DiscountProvidedEvent event = mock(DiscountProvidedEvent.class);
        campaign.notifyDiscountProviderEventListeners(event);

        //then
        Assertions.assertThat(campaign.getDiscountProviderEventListenerList()).hasSize(1);
        Assertions.assertThat(campaign.getDiscountProviderEventListenerList()).contains(shoppingCart);
        verify(shoppingCart, times(1)).discountProvided(ArgumentMatchers.eq(event));
    }

    @Test
    public void givenProductQuantityInCategoryBasedDiscountCampaign_whenIsValid_thenProvideDiscount() {
        //given
        CampaignName campaignName = CampaignName.valueOf("CAMPAIGN");
        assertThat(campaignName).isNotNull();
        Category category = new Category(Title.valueOf("Category"));
        assertThat(category).isNotNull();
        Quantity minimumProductQuantityInCategory = Quantity.valueOf(10);
        assertThat(minimumProductQuantityInCategory).isNotNull();
        Amount discountAmount = Amount.valueOf(10D);
        assertThat(discountAmount).isNotNull();

        ProductQuantityInCategoryBasedDiscountCampaign campaign = new ProductQuantityInCategoryBasedDiscountCampaign(campaignName, category, minimumProductQuantityInCategory, discountAmount);
        Assertions.assertThat(campaign.getFinishTime()).isNull();

        ShoppingCart shoppingCart = mock(ShoppingCart.class);
        campaign.registerDiscountProviderEventListener(shoppingCart);

        //when
        Assertions.assertThat(campaign.isValid()).isTrue();
        campaign.provideDiscount();

        //then
        Assertions.assertThat(campaign.getDiscountProviderEventListenerList()).hasSize(1);
        Assertions.assertThat(campaign.getDiscountProviderEventListenerList()).contains(shoppingCart);
        verify(shoppingCart, times(1)).discountProvided(ArgumentMatchers.any(DiscountProvidedEvent.class));
    }

    @Test
    public void givenProductQuantityInCategoryBasedDiscountCampaign_whenIsNotValid_thenDoNotProvideDiscount() {
        //given
        CampaignName campaignName = CampaignName.valueOf("CAMPAIGN");
        assertThat(campaignName).isNotNull();
        Category category = new Category(Title.valueOf("Category"));
        assertThat(category).isNotNull();
        Quantity minimumProductQuantityInCategory = Quantity.valueOf(10);
        assertThat(minimumProductQuantityInCategory).isNotNull();
        Amount discountAmount = Amount.valueOf(10D);
        assertThat(discountAmount).isNotNull();
        Instant startTime = Instant.now().minus(Duration.ofDays(10));
        assertThat(startTime).isNotNull();
        Instant finishTime = Instant.now().minus(Duration.ofDays(1));
        assertThat(finishTime).isNotNull();

        ProductQuantityInCategoryBasedDiscountCampaign campaign = new ProductQuantityInCategoryBasedDiscountCampaign(campaignName, category, minimumProductQuantityInCategory, discountAmount, startTime, finishTime);
        Assertions.assertThat(campaign.getFinishTime()).isNotNull();

        ShoppingCart shoppingCart = mock(ShoppingCart.class);
        campaign.registerDiscountProviderEventListener(shoppingCart);

        //when
        Assertions.assertThat(campaign.isValid()).isFalse();
        campaign.provideDiscount();

        //then
        Assertions.assertThat(campaign.getDiscountProviderEventListenerList()).hasSize(1);
        Assertions.assertThat(campaign.getDiscountProviderEventListenerList()).contains(shoppingCart);
        verify(shoppingCart, never()).discountProvided(ArgumentMatchers.any(DiscountProvidedEvent.class));
    }

}
