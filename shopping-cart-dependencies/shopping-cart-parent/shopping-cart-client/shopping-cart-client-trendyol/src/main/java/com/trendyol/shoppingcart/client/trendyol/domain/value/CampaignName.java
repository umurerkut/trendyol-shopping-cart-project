package com.trendyol.shoppingcart.client.trendyol.domain.value;

import com.trendyol.shoppingcart.core.exception.InvalidValueException;

import java.util.Comparator;
import java.util.Objects;

/*
CampaignName value object
 */
public final class CampaignName implements Comparable<CampaignName> {

    private final String value;

    private CampaignName(String value) {

        if (value == null) {
            throw new InvalidValueException("Campaign name value can not be null!");
        }

        if ("".equals(value)) {
            throw new InvalidValueException("Campaign name value can not be blank!");
        }

        this.value = value;
    }

    public static CampaignName valueOf(String value) {
        return new CampaignName(value);
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CampaignName campaignName = (CampaignName) o;
        return value.equals(campaignName.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public int compareTo(CampaignName o) {
        return Comparator.comparing(CampaignName::getValue).compare(this, o);
    }
}
