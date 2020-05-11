package com.trendyol.shoppingcart.client.trendyol;

import com.trendyol.shoppingcart.client.trendyol.discountprovider.campaign.Campaign;
import com.trendyol.shoppingcart.client.trendyol.discountprovider.campaign.ProductQuantityInCategoryBasedDiscountCampaign;
import com.trendyol.shoppingcart.client.trendyol.domain.value.CampaignName;
import com.trendyol.shoppingcart.client.trendyol.domain.value.Rate;
import com.trendyol.shoppingcart.core.domain.Category;
import com.trendyol.shoppingcart.core.domain.value.Amount;
import com.trendyol.shoppingcart.core.domain.value.Quantity;

public final class CampaignFakeData {

    public static Campaign createCampaignIfQuantityOfProductsInFoodCategoryGreaterEquals5ThenApplyDiscountRateOf10(Category category) {
        String campaignNameText = "CAMPAIGN-1";
        return new ProductQuantityInCategoryBasedDiscountCampaign(CampaignName.valueOf(campaignNameText), category, Quantity.valueOf(5), Rate.valueOf(10D));
    }

    public static Campaign createCampaignIfQuantityOfProductsInFoodCategoryGreaterEquals10ThenApplyDiscountAmountOf15(Category category) {
        String campaignNameText = "CAMPAIGN-2";
        return new ProductQuantityInCategoryBasedDiscountCampaign(CampaignName.valueOf(campaignNameText), category, Quantity.valueOf(10), Amount.valueOf(15D));
    }

    public static Campaign createCampaignIfQuantityOfProductsInBeverageCategoryGreaterEquals2ThenApplyDiscountRateOf10(Category category) {
        String campaignNameText = "CAMPAIGN-3";
        return new ProductQuantityInCategoryBasedDiscountCampaign(CampaignName.valueOf(campaignNameText), category, Quantity.valueOf(2), Rate.valueOf(10D));
    }

    public static Campaign createCampaignIfQuantityOfProductsInAlcoholicBeverageCategoryGreaterEquals5ThenApplyDiscountAmountOf15(Category category) {
        String campaignNameText = "CAMPAIGN-4";
        return new ProductQuantityInCategoryBasedDiscountCampaign(CampaignName.valueOf(campaignNameText), category, Quantity.valueOf(5), Amount.valueOf(15D));
    }

}
