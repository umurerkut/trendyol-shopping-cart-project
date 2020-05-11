package com.trendyol.shoppingcart.client.trendyol.discountprovider.campaign;


import com.trendyol.shoppingcart.client.trendyol.domain.value.CampaignName;
import com.trendyol.shoppingcart.core.discountprovider.DiscountProviderItem;
import com.trendyol.shoppingcart.core.domain.discount.Discount;
import com.trendyol.shoppingcart.core.exception.InvalidValueException;

import java.time.Instant;
import java.util.Objects;

public abstract class Campaign extends DiscountProviderItem {

    private final CampaignName campaignName;
    private final Instant startTime;
    private final Instant finishTime;

    public Campaign(Discount discount, CampaignName campaignName) {
        this(discount, campaignName, Instant.now(), null);
    }

    public Campaign(Discount discount, CampaignName campaignName, Instant startTime, Instant finishTime) {
        super(discount);

        if (campaignName == null) {
            throw new InvalidValueException("Campaign name can not be null!");
        }

        this.campaignName = campaignName;
        this.startTime = startTime;
        this.finishTime = finishTime;
    }

    @Override
    public boolean isValid() {
        if (finishTime == null) {
            //if the finish time is null, the campaign is valid forever until the beginning
            return true;
        }
        Instant now = Instant.now();
        return now.isAfter(startTime) && now.isBefore(finishTime);
    }

    public CampaignName getCampaignName() {
        return campaignName;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public Instant getFinishTime() {
        return finishTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Campaign campaign = (Campaign) o;

        if (!campaignName.equals(campaign.campaignName)) return false;
        if (!startTime.equals(campaign.startTime)) return false;
        return Objects.equals(finishTime, campaign.finishTime);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + campaignName.hashCode();
        result = 31 * result + startTime.hashCode();
        result = 31 * result + (finishTime != null ? finishTime.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Campaign: " +
                "CampaignName=" + campaignName +
                ", StartTime=" + startTime +
                ", FinishTime=" + finishTime;
    }
}
