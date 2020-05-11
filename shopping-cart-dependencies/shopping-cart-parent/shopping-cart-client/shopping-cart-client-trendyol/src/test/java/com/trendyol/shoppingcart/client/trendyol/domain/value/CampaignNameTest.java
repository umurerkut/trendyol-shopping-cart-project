package com.trendyol.shoppingcart.client.trendyol.domain.value;


import com.trendyol.shoppingcart.core.exception.InvalidValueException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class CampaignNameTest {

    @Test
    public void givenValue_whenValueIsNull_thenThrowInvalidValueException() {
        //given
        String value = null;

        //when
        assertThat(value).isNull();
        Throwable throwable = catchThrowable(() -> {
            CampaignName campaignName = CampaignName.valueOf(value);
        });

        //then
        assertThat(throwable)
                .isNotNull()
                .isInstanceOf(InvalidValueException.class)
                .hasMessage("Campaign name value can not be null!");
    }

    @Test
    public void givenValue_whenValueIsBlank_thenThrowInvalidValueException() {
        //given
        String value = "";

        //when
        assertThat(value).isBlank();
        Throwable throwable = catchThrowable(() -> {
            CampaignName campaignName = CampaignName.valueOf(value);
        });

        //then
        assertThat(throwable)
                .isNotNull()
                .isInstanceOf(InvalidValueException.class)
                .hasMessage("Campaign name value can not be blank!");
    }

    @Test
    public void givenValue_whenValueIsNotNullAndNotBlank_thenReturnCampaignNameOfValue() {
        //given
        String value = "Test CampaignName";

        //when
        assertThat(value).isNotNull().isNotBlank();

        //then
        CampaignName campaignName = CampaignName.valueOf(value);
        assertThat(campaignName).isNotNull();
        assertThat(campaignName.getValue()).isEqualTo(value);
    }

    @Test
    public void givenTwoValues_whenValuesAreSame_thenCampaignNameEqualsReturnTrue() {
        //given
        String value1 = "Test CampaignName";
        String value2 = "Test CampaignName";

        //when
        assertThat(value1).isEqualTo(value2);

        //then
        CampaignName campaignName1 = CampaignName.valueOf(value1);
        CampaignName campaignName2 = CampaignName.valueOf(value2);
        assertThat(campaignName1).isEqualTo(campaignName2);
    }

    @Test
    public void givenTwoValues_whenValuesAreDifferent_thenEqualsReturnFalse() {
        //given
        String value1 = "Test CampaignName 1";
        String value2 = "Test CampaignName 2";

        //when
        assertThat(value1).isNotEqualTo(value2);

        //then
        CampaignName campaignName1 = CampaignName.valueOf(value1);
        CampaignName campaignName2 = CampaignName.valueOf(value2);
        assertThat(campaignName1).isNotEqualTo(campaignName2);
    }

    @Test
    public void givenTwoValues_whenValueOfCampaignName1LessThanValueOfCampaignName2_thenCampaignName1LessThanCampaignName2() {
        //given
        String value1 = "Test CampaignName 1";
        String value2 = "Test CampaignName 2";

        //when
        assertThat(value1).isLessThan(value2);

        //then
        CampaignName campaignName1 = CampaignName.valueOf(value1);
        CampaignName campaignName2 = CampaignName.valueOf(value2);
        assertThat(campaignName1).isLessThan(campaignName2);
    }

    @Test
    public void givenTwoValues_whenValueOfCampaignName1GreaterThanValueOfCampaignName2_thenCampaignName1GreaterThanCampaignName2() {
        //given
        String value1 = "Test CampaignName 2";
        String value2 = "Test CampaignName 1";

        //when
        assertThat(value1).isGreaterThan(value2);

        //then
        CampaignName campaignName1 = CampaignName.valueOf(value1);
        CampaignName campaignName2 = CampaignName.valueOf(value2);
        assertThat(campaignName1).isGreaterThan(campaignName2);
    }
}
