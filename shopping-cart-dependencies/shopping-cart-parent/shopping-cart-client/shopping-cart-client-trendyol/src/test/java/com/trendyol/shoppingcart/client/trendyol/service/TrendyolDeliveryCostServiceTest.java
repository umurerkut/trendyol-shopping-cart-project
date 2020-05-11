package com.trendyol.shoppingcart.client.trendyol.service;

import com.trendyol.shoppingcart.client.trendyol.config.TrendyolShoppingCartClientProperties;
import com.trendyol.shoppingcart.core.domain.ShoppingCart;
import com.trendyol.shoppingcart.core.domain.value.Amount;
import com.trendyol.shoppingcart.core.domain.value.Quantity;
import com.trendyol.shoppingcart.core.exception.InvalidValueException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.*;

public class TrendyolDeliveryCostServiceTest {

    @Test
    public void givenNullTrendyolShoppingCartClientProperties_whenCreateTrendyolDeliveryCostService_thenReturnTrendyolDeliveryCostService() {
        //given
        TrendyolShoppingCartClientProperties trendyolShoppingCartClientProperties = null;
        assertThat(trendyolShoppingCartClientProperties).isNull();

        //when
        TrendyolDeliveryCostService trendyolDeliveryCostService = new TrendyolDeliveryCostService(trendyolShoppingCartClientProperties);

        //then
        assertThat(trendyolDeliveryCostService).isNotNull();
        assertThat(trendyolDeliveryCostService.getTrendyolShoppingCartProperties()).isNull();
    }

    @Test
    public void givenValidTrendyolShoppingCartClientProperties_whenCreateTrendyolDeliveryCostService_thenReturnTrendyolDeliveryCostService() {
        //given
        TrendyolShoppingCartClientProperties trendyolShoppingCartClientProperties = mock(TrendyolShoppingCartClientProperties.class);
        assertThat(trendyolShoppingCartClientProperties).isNotNull();

        //when
        TrendyolDeliveryCostService trendyolDeliveryCostService = new TrendyolDeliveryCostService(trendyolShoppingCartClientProperties);

        //then
        assertThat(trendyolDeliveryCostService).isNotNull();
        assertThat(trendyolDeliveryCostService.getTrendyolShoppingCartProperties()).isNotNull();
    }

    @Test
    public void givenTrendyolDeliveryCostService_whenShoppingCartParameterOfCalculateForMethodIsNull_thenThrowInvalidValueException() {
        //given
        TrendyolShoppingCartClientProperties trendyolShoppingCartClientProperties = mock(TrendyolShoppingCartClientProperties.class);
        assertThat(trendyolShoppingCartClientProperties).isNotNull();
        TrendyolDeliveryCostService trendyolDeliveryCostService = new TrendyolDeliveryCostService(trendyolShoppingCartClientProperties);

        //when
        Throwable throwable = catchThrowable(() -> {
            Amount deliveryCost = trendyolDeliveryCostService.calculateFor(null);
        });

        //then
        assertThat(throwable)
                .isNotNull()
                .isInstanceOf(InvalidValueException.class)
                .hasMessage("Shopping cart can not be null!");
    }

    @Test
    public void givenTrendyolDeliveryCostService_whenCostPerDeliveryIsNegative_thenCostPerDeliveryIsZero() {
        //given
        TrendyolShoppingCartClientProperties trendyolShoppingCartClientProperties = mock(TrendyolShoppingCartClientProperties.class, withSettings().useConstructor().defaultAnswer(CALLS_REAL_METHODS));
        assertThat(trendyolShoppingCartClientProperties).isNotNull();
        trendyolShoppingCartClientProperties.getDeliverCost().setCostPerDelivery(-2D);
        trendyolShoppingCartClientProperties.getDeliverCost().setCostPerProduct(2D);
        trendyolShoppingCartClientProperties.getDeliverCost().setFixedCost(2.99D);
        assertThat(trendyolShoppingCartClientProperties.getDeliverCost().getCostPerDelivery()).isNegative();

        TrendyolDeliveryCostService trendyolDeliveryCostService = new TrendyolDeliveryCostService(trendyolShoppingCartClientProperties);
        assertThat(trendyolDeliveryCostService).isNotNull();

        ShoppingCart shoppingCart = mock(ShoppingCart.class);
        assertThat(shoppingCart).isNotNull();
        when(shoppingCart.getNumberOfDistinctCategoryInCart()).thenReturn(Quantity.valueOf(2));
        when(shoppingCart.getNumberOfDistinctProductInCart()).thenReturn(Quantity.valueOf(2));

        //when
        Amount deliveryCost = trendyolDeliveryCostService.calculateFor(shoppingCart);

        //then
        assertThat(deliveryCost).isEqualTo(Amount.valueOf(6.99D));
    }

    @Test
    public void givenTrendyolDeliveryCostService_whenCostPerProductIsNegative_thenCostPerProductIsZero() {
        //given
        TrendyolShoppingCartClientProperties trendyolShoppingCartClientProperties = mock(TrendyolShoppingCartClientProperties.class, withSettings().useConstructor().defaultAnswer(CALLS_REAL_METHODS));
        assertThat(trendyolShoppingCartClientProperties).isNotNull();
        trendyolShoppingCartClientProperties.getDeliverCost().setCostPerDelivery(2D);
        trendyolShoppingCartClientProperties.getDeliverCost().setCostPerProduct(-2D);
        trendyolShoppingCartClientProperties.getDeliverCost().setFixedCost(2.99D);
        assertThat(trendyolShoppingCartClientProperties.getDeliverCost().getCostPerProduct()).isNegative();

        TrendyolDeliveryCostService trendyolDeliveryCostService = new TrendyolDeliveryCostService(trendyolShoppingCartClientProperties);
        assertThat(trendyolDeliveryCostService).isNotNull();

        ShoppingCart shoppingCart = mock(ShoppingCart.class);
        assertThat(shoppingCart).isNotNull();
        when(shoppingCart.getNumberOfDistinctCategoryInCart()).thenReturn(Quantity.valueOf(2));
        when(shoppingCart.getNumberOfDistinctProductInCart()).thenReturn(Quantity.valueOf(2));

        //when
        Amount deliveryCost = trendyolDeliveryCostService.calculateFor(shoppingCart);

        //then
        assertThat(deliveryCost).isEqualTo(Amount.valueOf(6.99D));
    }

    @Test
    public void givenTrendyolDeliveryCostService_whenFixedCostIsNegative_thenFixedCos2_99() {
        //given
        TrendyolShoppingCartClientProperties trendyolShoppingCartClientProperties = mock(TrendyolShoppingCartClientProperties.class, withSettings().useConstructor().defaultAnswer(CALLS_REAL_METHODS));
        assertThat(trendyolShoppingCartClientProperties).isNotNull();
        trendyolShoppingCartClientProperties.getDeliverCost().setCostPerDelivery(2D);
        trendyolShoppingCartClientProperties.getDeliverCost().setCostPerProduct(2D);
        trendyolShoppingCartClientProperties.getDeliverCost().setFixedCost(-2D);
        assertThat(trendyolShoppingCartClientProperties.getDeliverCost().getFixedCost()).isNegative();

        TrendyolDeliveryCostService trendyolDeliveryCostService = new TrendyolDeliveryCostService(trendyolShoppingCartClientProperties);
        assertThat(trendyolDeliveryCostService).isNotNull();

        ShoppingCart shoppingCart = mock(ShoppingCart.class);
        assertThat(shoppingCart).isNotNull();
        when(shoppingCart.getNumberOfDistinctCategoryInCart()).thenReturn(Quantity.valueOf(2));
        when(shoppingCart.getNumberOfDistinctProductInCart()).thenReturn(Quantity.valueOf(2));

        //when
        Amount deliveryCost = trendyolDeliveryCostService.calculateFor(shoppingCart);

        //then
        assertThat(deliveryCost).isEqualTo(Amount.valueOf(10.99D));
    }

    @Test
    public void givenTrendyolDeliveryCostService_whenFixedCostIsZero_thenFixedCos2_99() {
        //given
        TrendyolShoppingCartClientProperties trendyolShoppingCartClientProperties = mock(TrendyolShoppingCartClientProperties.class, withSettings().useConstructor().defaultAnswer(CALLS_REAL_METHODS));
        assertThat(trendyolShoppingCartClientProperties).isNotNull();
        trendyolShoppingCartClientProperties.getDeliverCost().setCostPerDelivery(2D);
        trendyolShoppingCartClientProperties.getDeliverCost().setCostPerProduct(2D);
        trendyolShoppingCartClientProperties.getDeliverCost().setFixedCost(0D);
        assertThat(trendyolShoppingCartClientProperties.getDeliverCost().getFixedCost()).isZero();

        TrendyolDeliveryCostService trendyolDeliveryCostService = new TrendyolDeliveryCostService(trendyolShoppingCartClientProperties);
        assertThat(trendyolDeliveryCostService).isNotNull();

        ShoppingCart shoppingCart = mock(ShoppingCart.class);
        assertThat(shoppingCart).isNotNull();
        when(shoppingCart.getNumberOfDistinctCategoryInCart()).thenReturn(Quantity.valueOf(2));
        when(shoppingCart.getNumberOfDistinctProductInCart()).thenReturn(Quantity.valueOf(2));

        //when
        Amount deliveryCost = trendyolDeliveryCostService.calculateFor(shoppingCart);

        //then
        assertThat(deliveryCost).isEqualTo(Amount.valueOf(10.99D));
    }

    @Test
    public void givenTrendyolDeliveryCostService_whenDefaultTrendyolShoppingCartClientProperties_thenCostPerProductAndCostPerDeliveryAreZero() {
        //given
        TrendyolShoppingCartClientProperties trendyolShoppingCartClientProperties = mock(TrendyolShoppingCartClientProperties.class, withSettings().useConstructor().defaultAnswer(CALLS_REAL_METHODS));
        assertThat(trendyolShoppingCartClientProperties).isNotNull();
        assertThat(trendyolShoppingCartClientProperties.getDeliverCost().getCostPerDelivery()).isZero();
        assertThat(trendyolShoppingCartClientProperties.getDeliverCost().getCostPerProduct()).isZero();

        TrendyolDeliveryCostService trendyolDeliveryCostService = new TrendyolDeliveryCostService(trendyolShoppingCartClientProperties);
        assertThat(trendyolDeliveryCostService).isNotNull();

        ShoppingCart shoppingCart = mock(ShoppingCart.class);
        assertThat(shoppingCart).isNotNull();
        when(shoppingCart.getNumberOfDistinctCategoryInCart()).thenReturn(Quantity.valueOf(2));
        when(shoppingCart.getNumberOfDistinctProductInCart()).thenReturn(Quantity.valueOf(2));

        //when
        Amount deliveryCost = trendyolDeliveryCostService.calculateFor(shoppingCart);

        //then
        assertThat(deliveryCost).isEqualTo(Amount.valueOf(2.99D));
    }
}
