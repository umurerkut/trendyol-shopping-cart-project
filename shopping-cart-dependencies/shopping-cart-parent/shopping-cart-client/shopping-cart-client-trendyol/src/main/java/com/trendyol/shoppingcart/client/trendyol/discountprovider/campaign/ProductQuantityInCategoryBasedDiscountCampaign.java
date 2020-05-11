package com.trendyol.shoppingcart.client.trendyol.discountprovider.campaign;


import com.trendyol.shoppingcart.client.trendyol.domain.discount.ProductQuantityInCategoryBasedAmountDiscount;
import com.trendyol.shoppingcart.client.trendyol.domain.discount.ProductQuantityInCategoryBasedRateDiscount;
import com.trendyol.shoppingcart.client.trendyol.domain.value.CampaignName;
import com.trendyol.shoppingcart.client.trendyol.domain.value.Rate;
import com.trendyol.shoppingcart.core.domain.Category;
import com.trendyol.shoppingcart.core.domain.value.Amount;
import com.trendyol.shoppingcart.core.domain.value.Quantity;

import java.time.Instant;

public class ProductQuantityInCategoryBasedDiscountCampaign extends Campaign {

    public ProductQuantityInCategoryBasedDiscountCampaign(CampaignName campaignName, Category category, Quantity minimumCategoryProductQuantity, Rate discountRate) {
        this(campaignName, category, minimumCategoryProductQuantity, discountRate, Instant.now(), null);
    }

    public ProductQuantityInCategoryBasedDiscountCampaign(CampaignName campaignName, Category category, Quantity minimumCategoryProductQuantity, Amount discountAmount) {
        this(campaignName, category, minimumCategoryProductQuantity, discountAmount, Instant.now(), null);
    }

    public ProductQuantityInCategoryBasedDiscountCampaign(CampaignName campaignName, Category category, Quantity minimumCategoryProductQuantity, Rate discountRate, Instant startTime, Instant finishTime) {
        super(new ProductQuantityInCategoryBasedRateDiscount(category, minimumCategoryProductQuantity, discountRate), campaignName, startTime, finishTime);

    }

    public ProductQuantityInCategoryBasedDiscountCampaign(CampaignName campaignName, Category category, Quantity minimumCategoryProductQuantity, Amount discountAmount, Instant startTime, Instant finishTime) {
        super(new ProductQuantityInCategoryBasedAmountDiscount(category, minimumCategoryProductQuantity, discountAmount), campaignName, startTime, finishTime);
    }

}
