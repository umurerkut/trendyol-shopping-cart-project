package com.trendyol.shoppingcart.client.trendyol;

import com.trendyol.shoppingcart.client.trendyol.discountprovider.campaign.Campaign;
import com.trendyol.shoppingcart.client.trendyol.discountprovider.coupon.Coupon;
import com.trendyol.shoppingcart.client.trendyol.domain.value.CampaignName;
import com.trendyol.shoppingcart.client.trendyol.domain.value.CouponCode;
import com.trendyol.shoppingcart.client.trendyol.service.CampaignService;
import com.trendyol.shoppingcart.client.trendyol.service.CouponService;
import com.trendyol.shoppingcart.client.trendyol.service.TrendyolDeliveryCostService;
import com.trendyol.shoppingcart.core.client.ShoppingCartClient;
import com.trendyol.shoppingcart.core.discountprovider.DiscountProviderGroup;
import com.trendyol.shoppingcart.core.service.ShoppingCartService;

import java.util.NoSuchElementException;

public class TrendyolShoppingCartClient extends ShoppingCartClient {

    private final CampaignService campaignService;
    private final CouponService couponService;

    private final DiscountProviderGroup campaigns = new DiscountProviderGroup();
    private final DiscountProviderGroup coupons = new DiscountProviderGroup();

    public TrendyolShoppingCartClient(TrendyolDeliveryCostService trendyolDeliveryCostService, ShoppingCartService shoppingCartService, CampaignService campaignService, CouponService couponService) {
        super(trendyolDeliveryCostService, shoppingCartService);
        this.campaignService = campaignService;
        this.couponService = couponService;
        createClientRuleDiscountProviderGroup();
    }

    //always provide campaigns first then provide coupons
    private void createClientRuleDiscountProviderGroup() {
        ruleDiscountProvider.addDiscountProvider(campaigns);
        ruleDiscountProvider.addDiscountProvider(coupons);
    }

    public void addCampaign(CampaignName campaignName) {
        Campaign campaign = campaignService.get(campaignName).orElseThrow(NoSuchElementException::new);
        campaigns.addDiscountProvider(campaign);
    }

    public void removeCampaign(CampaignName campaignName) {
        Campaign campaign = campaignService.get(campaignName).orElseThrow(NoSuchElementException::new);
        campaigns.removeDiscountProvider(campaign);
    }

    public void addCoupon(CouponCode couponCode) {
        Coupon coupon = couponService.get(couponCode).orElseThrow(NoSuchElementException::new);
        campaigns.addDiscountProvider(coupon);
    }

    public void removeCoupon(CouponCode couponCode) {
        Coupon coupon = couponService.get(couponCode).orElseThrow(NoSuchElementException::new);
        campaigns.removeDiscountProvider(coupon);
    }
}
