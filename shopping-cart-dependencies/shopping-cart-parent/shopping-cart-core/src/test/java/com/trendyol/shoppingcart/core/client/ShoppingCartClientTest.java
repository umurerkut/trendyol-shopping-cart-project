package com.trendyol.shoppingcart.core.client;

import com.trendyol.shoppingcart.core.discountprovider.DiscountProvider;
import com.trendyol.shoppingcart.core.discountprovider.DiscountProviderGroup;
import com.trendyol.shoppingcart.core.discountprovider.DiscountProviderItem;
import com.trendyol.shoppingcart.core.domain.ShoppingCart;
import com.trendyol.shoppingcart.core.domain.value.Amount;
import com.trendyol.shoppingcart.core.exception.InvalidValueException;
import com.trendyol.shoppingcart.core.service.DeliveryCostService;
import com.trendyol.shoppingcart.core.service.ShoppingCartService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.*;

public class ShoppingCartClientTest {

    @Test
    public void givenNullDeliveryCostServiceAndValidShoppingCartService_whenCreateShoppingCartClient_returnShoppingCartClient() {
        //given
        DeliveryCostService deliveryCostService = null;
        ShoppingCartService shoppingCartService = mock(ShoppingCartService.class);
        assertThat(deliveryCostService).isNull();
        assertThat(shoppingCartService).isNotNull();

        //when
        ShoppingCartClient shoppingCartClient = new ShoppingCartClient(deliveryCostService, shoppingCartService);

        //then
        assertThat(shoppingCartClient).isNotNull();
        assertThat(shoppingCartClient.getDeliveryCostService()).isNull();
        assertThat(shoppingCartClient.getShoppingCartService()).isNotNull().isEqualTo(shoppingCartService);
    }

    @Test
    public void givenValidDeliveryCostServiceAndNullValidShoppingCartService_whenCreateShoppingCartClient_returnShoppingCartClient() {
        //given
        DeliveryCostService deliveryCostService = mock(DeliveryCostService.class);
        ShoppingCartService shoppingCartService = null;
        assertThat(deliveryCostService).isNotNull();
        assertThat(shoppingCartService).isNull();

        //when
        Throwable throwable = catchThrowable(() -> {
            ShoppingCartClient shoppingCartClient = new ShoppingCartClient(deliveryCostService, shoppingCartService);
        });

        //then
        assertThat(throwable)
                .isNotNull()
                .isInstanceOf(InvalidValueException.class)
                .hasMessage("Shopping cart service can not be null!");
    }

    @Test
    public void givenValidDeliveryCostServiceAndValidShoppingCartService_whenCreateShoppingCartClient_returnShoppingCartClient() {
        //given
        DeliveryCostService deliveryCostService = mock(DeliveryCostService.class);
        ShoppingCartService shoppingCartService = mock(ShoppingCartService.class);
        assertThat(deliveryCostService).isNotNull();
        assertThat(shoppingCartService).isNotNull();

        //when
        ShoppingCartClient shoppingCartClient = new ShoppingCartClient(deliveryCostService, shoppingCartService);

        //then
        assertThat(shoppingCartClient).isNotNull();
        assertThat(shoppingCartClient.getDeliveryCostService()).isNotNull().isEqualTo(deliveryCostService);
        assertThat(shoppingCartClient.getShoppingCartService()).isNotNull().isEqualTo(shoppingCartService);
    }

    @Test
    public void givenValidShoppingCartClient_whenProvideDiscounts_thenRegisterShoppingCartAsListenerProvideDiscountsToAllChildProviders() {
        //given
        DeliveryCostService deliveryCostService = mock(DeliveryCostService.class);
        ShoppingCartService shoppingCartService = mock(ShoppingCartService.class);
        assertThat(deliveryCostService).isNotNull();
        assertThat(shoppingCartService).isNotNull();

        ShoppingCartClient shoppingCartClient = new ShoppingCartClient(deliveryCostService, shoppingCartService);
        assertThat(shoppingCartClient).isNotNull();

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
        assertThat(shoppingCart).isNotNull();

        shoppingCartClient.provideDiscounts(shoppingCart, discountProviderGroup);

        //then
        verify(discountProvider1, times(1)).registerDiscountProviderEventListener(ArgumentMatchers.eq(shoppingCart));
        verify(discountProvider2, times(1)).registerDiscountProviderEventListener(ArgumentMatchers.eq(shoppingCart));
        verify(discountProviderItem1, times(1)).registerDiscountProviderEventListener(ArgumentMatchers.eq(shoppingCart));
        verify(discountProviderItem2, times(1)).registerDiscountProviderEventListener(ArgumentMatchers.eq(shoppingCart));
        verify(childGroup1, times(1)).registerDiscountProviderEventListener(ArgumentMatchers.eq(shoppingCart));
        verify(childGroup2, times(1)).registerDiscountProviderEventListener(ArgumentMatchers.eq(shoppingCart));

        verify(discountProvider1, times(1)).provideDiscount();
        verify(discountProvider2, times(1)).provideDiscount();
        verify(discountProviderItem1, times(1)).provideDiscount();
        verify(discountProviderItem2, times(1)).provideDiscount();
        verify(childGroup1, times(1)).provideDiscount();
        verify(childGroup2, times(1)).provideDiscount();
    }

    @Test
    public void givenValidShoppingCartClient_whenSubmitCart_thenApplyDiscounts() {
        //given
        ShoppingCart shoppingCart = mock(ShoppingCart.class);
        assertThat(shoppingCart).isNotNull();
        DeliveryCostService deliveryCostService = mock(DeliveryCostService.class);
        when(deliveryCostService.calculateFor(any(ShoppingCart.class))).thenReturn(Amount.valueOf(10D));
        ShoppingCartService shoppingCartService = mock(ShoppingCartService.class);
        when(shoppingCartService.save(any(ShoppingCart.class))).thenReturn(shoppingCart);
        assertThat(deliveryCostService).isNotNull();
        assertThat(shoppingCartService).isNotNull();

        ShoppingCartClient shoppingCartClient = new ShoppingCartClient(deliveryCostService, shoppingCartService);
        assertThat(shoppingCartClient).isNotNull();

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
        shoppingCartClient.submitCart(shoppingCart, discountProviderGroup);

        //then
        verify(shoppingCart, times(1)).applyDiscounts();
    }

    @Test
    public void givenValidShoppingCartClient_whenSubmitCart_thenSetDeliveryCost() {
        //given
        ShoppingCart shoppingCart = mock(ShoppingCart.class);
        assertThat(shoppingCart).isNotNull();

        DeliveryCostService deliveryCostService = mock(DeliveryCostService.class);
        assertThat(deliveryCostService).isNotNull();

        ShoppingCartService shoppingCartService = mock(ShoppingCartService.class);
        assertThat(shoppingCartService).isNotNull();

        ShoppingCartClient shoppingCartClient = new ShoppingCartClient(deliveryCostService, shoppingCartService);
        assertThat(shoppingCartClient).isNotNull();

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
        when(shoppingCart.getDeliveryCost()).thenCallRealMethod();
        doCallRealMethod().when(shoppingCart).setDeliveryCost(any(Amount.class));
        when(deliveryCostService.calculateFor(any(ShoppingCart.class))).thenReturn(Amount.valueOf(10D));
        when(shoppingCartService.save(any(ShoppingCart.class))).thenReturn(shoppingCart);
        shoppingCartClient.submitCart(shoppingCart, discountProviderGroup);

        //then
        verify(shoppingCart, times(1)).setDeliveryCost(any(Amount.class));
        assertThat(shoppingCart.getDeliveryCost()).isEqualTo(Amount.valueOf(10D));
    }

    @Test
    public void givenValidShoppingCartClient_whenDeliveryCostServiceNullAndSubmitCart_thenDoNotSetDeliveryCost() {
        //given
        ShoppingCart shoppingCart = mock(ShoppingCart.class);
        assertThat(shoppingCart).isNotNull();

        DeliveryCostService deliveryCostService = null;
        assertThat(deliveryCostService).isNull();

        ShoppingCartService shoppingCartService = mock(ShoppingCartService.class);
        assertThat(shoppingCartService).isNotNull();

        ShoppingCartClient shoppingCartClient = new ShoppingCartClient(deliveryCostService, shoppingCartService);
        assertThat(shoppingCartClient).isNotNull();

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
        when(shoppingCart.getDeliveryCost()).thenCallRealMethod();
        doCallRealMethod().when(shoppingCart).setDeliveryCost(any(Amount.class));
        when(shoppingCartService.save(any(ShoppingCart.class))).thenReturn(shoppingCart);
        shoppingCartClient.submitCart(shoppingCart, discountProviderGroup);

        //then
        verify(shoppingCart, never()).setDeliveryCost(any(Amount.class));
    }

    @Test
    public void givenValidShoppingCartClient_whenSubmitCart_thenPrintShoppingCartInfo() {
        //given
        ShoppingCart shoppingCart = mock(ShoppingCart.class);
        assertThat(shoppingCart).isNotNull();
        DeliveryCostService deliveryCostService = mock(DeliveryCostService.class);
        when(deliveryCostService.calculateFor(any(ShoppingCart.class))).thenReturn(Amount.valueOf(10D));
        ShoppingCartService shoppingCartService = mock(ShoppingCartService.class);
        when(shoppingCartService.save(any(ShoppingCart.class))).thenReturn(shoppingCart);
        assertThat(deliveryCostService).isNotNull();
        assertThat(shoppingCartService).isNotNull();

        ShoppingCartClient shoppingCartClient = new ShoppingCartClient(deliveryCostService, shoppingCartService);
        assertThat(shoppingCartClient).isNotNull();

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
        shoppingCartClient.submitCart(shoppingCart, discountProviderGroup);

        //then
        verify(shoppingCart, times(1)).print();
    }

    @Test
    public void givenValidShoppingCartClient_whenSubmitCart_thenSaveShoppingCart() {
        //given
        ShoppingCart shoppingCart = mock(ShoppingCart.class);
        assertThat(shoppingCart).isNotNull();
        DeliveryCostService deliveryCostService = mock(DeliveryCostService.class);
        when(deliveryCostService.calculateFor(any(ShoppingCart.class))).thenReturn(Amount.valueOf(10D));
        ShoppingCartService shoppingCartService = mock(ShoppingCartService.class);
        when(shoppingCartService.save(any(ShoppingCart.class))).thenReturn(shoppingCart);
        assertThat(deliveryCostService).isNotNull();
        assertThat(shoppingCartService).isNotNull();

        ShoppingCartClient shoppingCartClient = new ShoppingCartClient(deliveryCostService, shoppingCartService);
        assertThat(shoppingCartClient).isNotNull();

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
        shoppingCartClient.submitCart(shoppingCart, discountProviderGroup);

        //then
        ArgumentCaptor<ShoppingCart> argumentCaptor = ArgumentCaptor.forClass(ShoppingCart.class);
        verify(shoppingCartService, times(1)).save(argumentCaptor.capture());
        ShoppingCart savedShoppingCart = argumentCaptor.getValue();
        assertThat(savedShoppingCart).isNotNull();
    }
}
