package com.trendyol.shoppingcart.client.trendyol;

import com.trendyol.shoppingcart.client.trendyol.config.TrendyolShoppingCartClientProperties;
import com.trendyol.shoppingcart.client.trendyol.discountprovider.campaign.Campaign;
import com.trendyol.shoppingcart.client.trendyol.discountprovider.coupon.Coupon;
import com.trendyol.shoppingcart.client.trendyol.domain.value.CampaignName;
import com.trendyol.shoppingcart.client.trendyol.domain.value.CouponCode;
import com.trendyol.shoppingcart.client.trendyol.service.CampaignService;
import com.trendyol.shoppingcart.client.trendyol.service.CouponService;
import com.trendyol.shoppingcart.client.trendyol.service.TrendyolDeliveryCostService;
import com.trendyol.shoppingcart.core.domain.CartItem;
import com.trendyol.shoppingcart.core.domain.Product;
import com.trendyol.shoppingcart.core.domain.ShoppingCart;
import com.trendyol.shoppingcart.core.domain.value.Amount;
import com.trendyol.shoppingcart.core.domain.value.Quantity;
import com.trendyol.shoppingcart.core.domain.value.Title;
import com.trendyol.shoppingcart.core.service.CategoryService;
import com.trendyol.shoppingcart.core.service.ProductService;
import com.trendyol.shoppingcart.core.service.ShoppingCartService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.trendyol.shoppingcart.client.trendyol.CategoryFakeData.*;
import static com.trendyol.shoppingcart.client.trendyol.ProductFakeData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TrendyolShoppingCartClientTest {


    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private TrendyolShoppingCartClient trendyolShoppingCartClient;
    private ShoppingCart shoppingCart;

    private ProductService productService;

    @BeforeEach
    public void beforeEach() {

        System.setOut(new PrintStream(outContent));

        CategoryService categoryService = mock(CategoryService.class);
        when(categoryService.get(Title.valueOf("FOOD"))).thenReturn(Optional.of(food));
        when(categoryService.get(Title.valueOf("BEVERAGE"))).thenReturn(Optional.of(beverage));
        when(categoryService.get(Title.valueOf("ALCOHOLIC_BEVERAGE"))).thenReturn(Optional.of(alcoholicBeverage));
        when(categoryService.get(Title.valueOf("NONALCOHOLIC_BEVERAGE"))).thenReturn(Optional.of(nonalcoholicBeverage));

        this.productService = mock(ProductService.class);
        when(productService.get(Title.valueOf("APPLE"))).thenReturn(Optional.of(apple));
        when(productService.get(Title.valueOf("PEAR"))).thenReturn(Optional.of(pear));
        when(productService.get(Title.valueOf("BANANA"))).thenReturn(Optional.of(banana));
        when(productService.get(Title.valueOf("WATER"))).thenReturn(Optional.of(water));
        when(productService.get(Title.valueOf("BEER"))).thenReturn(Optional.of(beer));

        CampaignService campaignService = mock(CampaignService.class);
        Campaign campaign1 = CampaignFakeData.createCampaignIfQuantityOfProductsInFoodCategoryGreaterEquals5ThenApplyDiscountRateOf10(food);
        Campaign campaign2 = CampaignFakeData.createCampaignIfQuantityOfProductsInFoodCategoryGreaterEquals10ThenApplyDiscountAmountOf15(food);
        Campaign campaign3 = CampaignFakeData.createCampaignIfQuantityOfProductsInBeverageCategoryGreaterEquals2ThenApplyDiscountRateOf10(beverage);
        Campaign campaign4 = CampaignFakeData.createCampaignIfQuantityOfProductsInAlcoholicBeverageCategoryGreaterEquals5ThenApplyDiscountAmountOf15(alcoholicBeverage);
        when(campaignService.get(CampaignName.valueOf("CAMPAIGN-1"))).thenReturn(Optional.of(campaign1));
        when(campaignService.get(CampaignName.valueOf("CAMPAIGN-2"))).thenReturn(Optional.of(campaign2));
        when(campaignService.get(CampaignName.valueOf("CAMPAIGN-3"))).thenReturn(Optional.of(campaign3));
        when(campaignService.get(CampaignName.valueOf("CAMPAIGN-4"))).thenReturn(Optional.of(campaign4));

        CouponService couponService = mock(CouponService.class);
        Coupon coupon1 = CouponFakeData.createCouponIfCartAmountGreaterEquals50ThenApplyDiscountAmountOf5;
        Coupon coupon2 = CouponFakeData.createCouponIfCartAmountGreaterEquals100ThenApplyDiscountRateOf10;
        when(couponService.get(CouponCode.valueOf("COUPON-1"))).thenReturn(Optional.of(coupon1));
        when(couponService.get(CouponCode.valueOf("COUPON-2"))).thenReturn(Optional.of(coupon2));

        this.shoppingCart = mock(ShoppingCart.class, withSettings().useConstructor().defaultAnswer(CALLS_REAL_METHODS));

        ShoppingCartService shoppingCartService = mock(ShoppingCartService.class);
        when(shoppingCartService.save(any(ShoppingCart.class))).thenReturn(shoppingCart);

        TrendyolShoppingCartClientProperties trendyolShoppingCartClientProperties = new TrendyolShoppingCartClientProperties();
        trendyolShoppingCartClientProperties.getDeliverCost().setCostPerProduct(2D);
        trendyolShoppingCartClientProperties.getDeliverCost().setCostPerDelivery(1D);

        TrendyolDeliveryCostService trendyolDeliveryCostService = mock(TrendyolDeliveryCostService.class, withSettings().useConstructor(trendyolShoppingCartClientProperties).defaultAnswer(CALLS_REAL_METHODS));

        this.trendyolShoppingCartClient = new TrendyolShoppingCartClient(trendyolDeliveryCostService, shoppingCartService, campaignService, couponService);
    }

    @AfterEach
    public void afterEach() {
        System.setOut(originalOut);
    }

    private void assertShoppingCartConsoleOutput() {
        assertThat(outContent).asString().contains("Shopping Cart Info:");
        shoppingCart.getCartItems()
                .stream()
                .collect(Collectors.groupingBy(CartItem::getCategory))
                .forEach((key, value) -> {
                    assertThat(outContent).asString().contains(key.toString());
                    value.forEach(cartItem -> assertThat(outContent).asString().contains(cartItem.toString()));
                });
        assertThat(outContent).asString().contains("Cart Amount: " + shoppingCart.getCartAmountWithoutDiscount());
        shoppingCart.getDiscountMap().forEach((key, value) -> assertThat(outContent).asString().contains(key + ": " + value.getDiscountAmount()));
        assertThat(outContent).asString().contains("Total Discount Amount: " + shoppingCart.getTotalDiscount());
        assertThat(outContent).asString().contains("Total Cart Amount After Discount(s): " + shoppingCart.getCartAmount());
        assertThat(outContent).asString().contains("Delivery Cost: " + shoppingCart.getDeliveryCost());
        assertThat(outContent).asString().contains("Total Amount(Cart Amount After Discount(s) + Delivery Cost): " + shoppingCart.getTotalAmount());
    }

    @Test
    public void givenCoupon1_whenSubmitCart_thenApplyDiscountAmount() {
        //given
        trendyolShoppingCartClient.addCoupon(CouponCode.valueOf("COUPON-1"));
        //IfCartAmountGreaterEquals50ThenApplyDiscountAmountOf5

        Product apple = productService.get(Title.valueOf("APPLE")).orElse(null);
        assertThat(apple).isNotNull();
        assertThat(apple.getTitle()).isEqualTo(Title.valueOf("APPLE"));
        Product banana = productService.get(Title.valueOf("BANANA")).orElse(null);
        assertThat(banana).isNotNull();
        assertThat(banana.getTitle()).isEqualTo(Title.valueOf("BANANA"));

        // 10 x 4 = 40
        shoppingCart.addProduct(apple, Quantity.valueOf(4));
        // 20 x 1 = 20
        shoppingCart.addProduct(banana, Quantity.valueOf(1));

        //when
        trendyolShoppingCartClient.submitCart(shoppingCart);

        //then
        assertThat(trendyolShoppingCartClient.getRuleDiscountProvider().getChildDiscountProviders()).isNotEmpty();
        assertThat(shoppingCart.getDiscountMap()).hasSize(1);
        assertThat(shoppingCart.getCartAmountWithoutDiscount()).isEqualTo(Amount.valueOf(60D));
        assertThat(shoppingCart.getTotalDiscount()).isEqualTo(Amount.valueOf(5D));
        assertThat(shoppingCart.getCartAmount()).isEqualTo(Amount.valueOf(55D));
        assertThat(shoppingCart.getCartAmount()).isNotEqualTo(shoppingCart.getCartAmountWithoutDiscount());
        assertThat(shoppingCart.getDeliveryCost()).isEqualTo(Amount.valueOf(7.99D));
        assertThat(shoppingCart.getTotalAmount()).isEqualTo(Amount.valueOf(62.99D));
        assertShoppingCartConsoleOutput();
    }

    @Test
    public void givenCoupon1_whenCartAmountLessThan50SubmitCart_thenDoNotApplyDiscountAmount() {
        //given
        trendyolShoppingCartClient.addCoupon(CouponCode.valueOf("COUPON-1"));
        //IfCartAmountGreaterEquals50ThenApplyDiscountAmountOf5

        Product apple = productService.get(Title.valueOf("APPLE")).orElse(null);
        assertThat(apple).isNotNull();
        assertThat(apple.getTitle()).isEqualTo(Title.valueOf("APPLE"));

        // 10 x 3 = 30
        shoppingCart.addProduct(apple, Quantity.valueOf(3));

        //when
        trendyolShoppingCartClient.submitCart(shoppingCart);

        //then
        assertThat(trendyolShoppingCartClient.getRuleDiscountProvider().getChildDiscountProviders()).isNotEmpty();
        assertThat(shoppingCart.getDiscountMap()).hasSize(0);
        assertThat(shoppingCart.getCartAmountWithoutDiscount()).isEqualTo(Amount.valueOf(30D));
        assertThat(shoppingCart.getTotalDiscount()).isEqualTo(Amount.ofZero());
        assertThat(shoppingCart.getCartAmount()).isEqualTo(Amount.valueOf(30D));
        assertThat(shoppingCart.getCartAmount()).isEqualTo(shoppingCart.getCartAmountWithoutDiscount());
        assertThat(shoppingCart.getDeliveryCost()).isEqualTo(Amount.valueOf(5.99D));
        assertThat(shoppingCart.getTotalAmount()).isEqualTo(Amount.valueOf(35.99D));
        assertShoppingCartConsoleOutput();
    }

    @Test
    public void givenCoupon2_whenSubmitCart_thenApplyDiscountAmount() {
        //given
        trendyolShoppingCartClient.addCoupon(CouponCode.valueOf("COUPON-2"));
        //IfCartAmountGreaterEquals100ThenApplyDiscountRateOf10

        Product apple = productService.get(Title.valueOf("APPLE")).orElse(null);
        assertThat(apple).isNotNull();
        assertThat(apple.getTitle()).isEqualTo(Title.valueOf("APPLE"));

        // 10 x 10 = 100
        shoppingCart.addProduct(apple, Quantity.valueOf(10));

        //when
        trendyolShoppingCartClient.submitCart(shoppingCart);

        //then
        assertThat(trendyolShoppingCartClient.getRuleDiscountProvider().getChildDiscountProviders()).isNotEmpty();
        assertThat(shoppingCart.getDiscountMap()).hasSize(1);
        assertThat(shoppingCart.getCartAmountWithoutDiscount()).isEqualTo(Amount.valueOf(100D));
        assertThat(shoppingCart.getTotalDiscount()).isEqualTo(Amount.valueOf(10D));
        assertThat(shoppingCart.getCartAmount()).isEqualTo(Amount.valueOf(90D));
        assertThat(shoppingCart.getCartAmount()).isNotEqualTo(shoppingCart.getCartAmountWithoutDiscount());
        assertThat(shoppingCart.getDeliveryCost()).isEqualTo(Amount.valueOf(5.99D));
        assertThat(shoppingCart.getTotalAmount()).isEqualTo(Amount.valueOf(95.99D));
        assertShoppingCartConsoleOutput();
    }

    @Test
    public void givenCoupon2_whenCartAmountLessThan100SubmitCart_thenDoNotApplyDiscountAmount() {
        //given
        trendyolShoppingCartClient.addCoupon(CouponCode.valueOf("COUPON-2"));
        //IfCartAmountGreaterEquals100ThenApplyDiscountRateOf10

        Product apple = productService.get(Title.valueOf("APPLE")).orElse(null);
        assertThat(apple).isNotNull();
        assertThat(apple.getTitle()).isEqualTo(Title.valueOf("APPLE"));

        // 10 x 6 = 60
        shoppingCart.addProduct(apple, Quantity.valueOf(6));

        //when
        trendyolShoppingCartClient.submitCart(shoppingCart);

        //then
        assertThat(trendyolShoppingCartClient.getRuleDiscountProvider().getChildDiscountProviders()).isNotEmpty();
        assertThat(shoppingCart.getDiscountMap()).hasSize(0);
        assertThat(shoppingCart.getCartAmountWithoutDiscount()).isEqualTo(Amount.valueOf(60D));
        assertThat(shoppingCart.getTotalDiscount()).isEqualTo(Amount.ofZero());
        assertThat(shoppingCart.getCartAmount()).isEqualTo(Amount.valueOf(60D));
        assertThat(shoppingCart.getCartAmount()).isEqualTo(shoppingCart.getCartAmountWithoutDiscount());
        assertThat(shoppingCart.getDeliveryCost()).isEqualTo(Amount.valueOf(5.99D));
        assertThat(shoppingCart.getTotalAmount()).isEqualTo(Amount.valueOf(65.99D));
        assertShoppingCartConsoleOutput();
    }

    @Test
    public void givenCoupon1AndCoupon2_whenCartAmountGreaterThan50LessThan100SubmitCart_thenOnlyApplyCoupon1DiscountAmount() {
        //given
        trendyolShoppingCartClient.addCoupon(CouponCode.valueOf("COUPON-1"));
        //IfCartAmountGreaterEquals50ThenApplyDiscountAmountOf5
        trendyolShoppingCartClient.addCoupon(CouponCode.valueOf("COUPON-2"));
        //IfCartAmountGreaterEquals100ThenApplyDiscountRateOf10

        Product apple = productService.get(Title.valueOf("APPLE")).orElse(null);
        assertThat(apple).isNotNull();
        assertThat(apple.getTitle()).isEqualTo(Title.valueOf("APPLE"));
        Product banana = productService.get(Title.valueOf("BANANA")).orElse(null);
        assertThat(banana).isNotNull();
        assertThat(banana.getTitle()).isEqualTo(Title.valueOf("BANANA"));

        // 10 x 4 = 40
        shoppingCart.addProduct(apple, Quantity.valueOf(4));
        // 20 x 1 = 20
        shoppingCart.addProduct(banana, Quantity.valueOf(1));

        //when
        trendyolShoppingCartClient.submitCart(shoppingCart);

        //then
        assertThat(trendyolShoppingCartClient.getRuleDiscountProvider().getChildDiscountProviders()).isNotEmpty();
        assertThat(shoppingCart.getDiscountMap()).hasSize(1);
        assertThat(shoppingCart.getCartAmountWithoutDiscount()).isEqualTo(Amount.valueOf(60D));
        assertThat(shoppingCart.getTotalDiscount()).isEqualTo(Amount.valueOf(5D));
        assertThat(shoppingCart.getCartAmount()).isEqualTo(Amount.valueOf(55D));
        assertThat(shoppingCart.getCartAmount()).isNotEqualTo(shoppingCart.getCartAmountWithoutDiscount());
        assertThat(shoppingCart.getDeliveryCost()).isEqualTo(Amount.valueOf(7.99D));
        assertThat(shoppingCart.getTotalAmount()).isEqualTo(Amount.valueOf(62.99D));
        assertShoppingCartConsoleOutput();
    }

    @Test
    public void givenCoupon1AndCoupon2_whenCartAmountGreaterThan100SubmitCart_thenOnlyApplyCoupon2DiscountAmount() {
        //given
        trendyolShoppingCartClient.addCoupon(CouponCode.valueOf("COUPON-1"));
        //IfCartAmountGreaterEquals50ThenApplyDiscountAmountOf5
        trendyolShoppingCartClient.addCoupon(CouponCode.valueOf("COUPON-2"));
        //IfCartAmountGreaterEquals100ThenApplyDiscountRateOf10

        Product apple = productService.get(Title.valueOf("APPLE")).orElse(null);
        assertThat(apple).isNotNull();
        assertThat(apple.getTitle()).isEqualTo(Title.valueOf("APPLE"));
        Product banana = productService.get(Title.valueOf("BANANA")).orElse(null);
        assertThat(banana).isNotNull();
        assertThat(banana.getTitle()).isEqualTo(Title.valueOf("BANANA"));
        Product pear = productService.get(Title.valueOf("PEAR")).orElse(null);
        assertThat(pear).isNotNull();
        assertThat(pear.getTitle()).isEqualTo(Title.valueOf("PEAR"));

        // 10 x 4 = 40
        shoppingCart.addProduct(apple, Quantity.valueOf(4));
        // 20 x 1 = 20
        shoppingCart.addProduct(banana, Quantity.valueOf(1));
        // 15 x 4 = 60
        shoppingCart.addProduct(pear, Quantity.valueOf(4));

        //when
        trendyolShoppingCartClient.submitCart(shoppingCart);

        //then
        assertThat(trendyolShoppingCartClient.getRuleDiscountProvider().getChildDiscountProviders()).isNotEmpty();
        assertThat(shoppingCart.getDiscountMap()).hasSize(1);
        assertThat(shoppingCart.getCartAmountWithoutDiscount()).isEqualTo(Amount.valueOf(120D));
        assertThat(shoppingCart.getTotalDiscount()).isEqualTo(Amount.valueOf(12D));
        assertThat(shoppingCart.getCartAmount()).isEqualTo(Amount.valueOf(108D));
        assertThat(shoppingCart.getCartAmount()).isNotEqualTo(shoppingCart.getCartAmountWithoutDiscount());
        assertThat(shoppingCart.getDeliveryCost()).isEqualTo(Amount.valueOf(9.99D));
        assertThat(shoppingCart.getTotalAmount()).isEqualTo(Amount.valueOf(117.99D));
        assertShoppingCartConsoleOutput();
    }

    @Test
    public void givenCampaign1_whenSubmitCart_thenApplyDiscountAmount() {
        //given
        trendyolShoppingCartClient.addCampaign(CampaignName.valueOf("CAMPAIGN-1"));
        //IfQuantityOfProductsInFoodCategoryGreaterEquals5ThenApplyDiscountRateOf10

        Product apple = productService.get(Title.valueOf("APPLE")).orElse(null);
        assertThat(apple).isNotNull();
        assertThat(apple.getTitle()).isEqualTo(Title.valueOf("APPLE"));

        // 10 x 10 = 100
        shoppingCart.addProduct(apple, Quantity.valueOf(10));

        //when
        trendyolShoppingCartClient.submitCart(shoppingCart);

        //then
        assertThat(trendyolShoppingCartClient.getRuleDiscountProvider().getChildDiscountProviders()).isNotEmpty();
        assertThat(shoppingCart.getDiscountMap()).hasSize(1);
        assertThat(shoppingCart.getCartAmountWithoutDiscount()).isEqualTo(Amount.valueOf(100D));
        assertThat(shoppingCart.getTotalDiscount()).isEqualTo(Amount.valueOf(10D));
        assertThat(shoppingCart.getCartAmount()).isEqualTo(Amount.valueOf(90D));
        assertThat(shoppingCart.getCartAmount()).isNotEqualTo(shoppingCart.getCartAmountWithoutDiscount());
        assertThat(shoppingCart.getDeliveryCost()).isEqualTo(Amount.valueOf(5.99D));
        assertThat(shoppingCart.getTotalAmount()).isEqualTo(Amount.valueOf(95.99D));
        assertShoppingCartConsoleOutput();
    }

    @Test
    public void givenCampaign1_whenSubmitCartWithZeroProductsInFoodCategory_thenDoNotApplyDiscountAmount() {
        //given
        trendyolShoppingCartClient.addCampaign(CampaignName.valueOf("CAMPAIGN-1"));
        //IfQuantityOfProductsInFoodCategoryGreaterEquals5ThenApplyDiscountRateOf10

        Product beer = productService.get(Title.valueOf("BEER")).orElse(null);
        assertThat(beer).isNotNull();
        assertThat(beer.getTitle()).isEqualTo(Title.valueOf("BEER"));

        // 20 x 10 = 200
        shoppingCart.addProduct(beer, Quantity.valueOf(10));

        //when
        trendyolShoppingCartClient.submitCart(shoppingCart);

        //then
        assertThat(trendyolShoppingCartClient.getRuleDiscountProvider().getChildDiscountProviders()).isNotEmpty();
        assertThat(shoppingCart.getDiscountMap()).hasSize(0);
        assertThat(shoppingCart.getCartAmountWithoutDiscount()).isEqualTo(Amount.valueOf(200D));
        assertThat(shoppingCart.getTotalDiscount()).isEqualTo(Amount.valueOf(0D));
        assertThat(shoppingCart.getCartAmount()).isEqualTo(Amount.valueOf(200D));
        assertThat(shoppingCart.getCartAmount()).isEqualTo(shoppingCart.getCartAmountWithoutDiscount());
        assertThat(shoppingCart.getDeliveryCost()).isEqualTo(Amount.valueOf(5.99D));
        assertThat(shoppingCart.getTotalAmount()).isEqualTo(Amount.valueOf(205.99D));
        assertShoppingCartConsoleOutput();
    }

    @Test
    public void givenCampaign1_whenSubmitCartWithQuantityOfProductsLessThen5_thenDoNotApplyDiscountAmount() {
        //given
        trendyolShoppingCartClient.addCampaign(CampaignName.valueOf("CAMPAIGN-1"));
        //IfQuantityOfProductsInFoodCategoryGreaterEquals5ThenApplyDiscountRateOf10

        Product apple = productService.get(Title.valueOf("APPLE")).orElse(null);
        assertThat(apple).isNotNull();
        assertThat(apple.getTitle()).isEqualTo(Title.valueOf("APPLE"));

        // 10 x 1 = 10
        shoppingCart.addProduct(apple, Quantity.valueOf(1));

        //when
        trendyolShoppingCartClient.submitCart(shoppingCart);

        //then
        assertThat(trendyolShoppingCartClient.getRuleDiscountProvider().getChildDiscountProviders()).isNotEmpty();
        assertThat(shoppingCart.getDiscountMap()).hasSize(0);
        assertThat(shoppingCart.getCartAmountWithoutDiscount()).isEqualTo(Amount.valueOf(10D));
        assertThat(shoppingCart.getTotalDiscount()).isEqualTo(Amount.valueOf(0D));
        assertThat(shoppingCart.getCartAmount()).isEqualTo(Amount.valueOf(10D));
        assertThat(shoppingCart.getCartAmount()).isEqualTo(shoppingCart.getCartAmountWithoutDiscount());
        assertThat(shoppingCart.getDeliveryCost()).isEqualTo(Amount.valueOf(5.99D));
        assertThat(shoppingCart.getTotalAmount()).isEqualTo(Amount.valueOf(15.99D));
        assertShoppingCartConsoleOutput();
    }

    @Test
    public void givenCampaign2_whenSubmitCart_thenApplyDiscountAmount() {
        //given
        trendyolShoppingCartClient.addCampaign(CampaignName.valueOf("CAMPAIGN-2"));
        //IfQuantityOfProductsInFoodCategoryGreaterEquals10ThenApplyDiscountAmountOf15

        Product apple = productService.get(Title.valueOf("APPLE")).orElse(null);
        assertThat(apple).isNotNull();
        assertThat(apple.getTitle()).isEqualTo(Title.valueOf("APPLE"));

        // 10 x 10 = 100
        shoppingCart.addProduct(apple, Quantity.valueOf(10));

        //when
        trendyolShoppingCartClient.submitCart(shoppingCart);

        //then
        assertThat(trendyolShoppingCartClient.getRuleDiscountProvider().getChildDiscountProviders()).isNotEmpty();
        assertThat(shoppingCart.getDiscountMap()).hasSize(1);
        assertThat(shoppingCart.getCartAmountWithoutDiscount()).isEqualTo(Amount.valueOf(100D));
        assertThat(shoppingCart.getTotalDiscount()).isEqualTo(Amount.valueOf(15D));
        assertThat(shoppingCart.getCartAmount()).isEqualTo(Amount.valueOf(85D));
        assertThat(shoppingCart.getCartAmount()).isNotEqualTo(shoppingCart.getCartAmountWithoutDiscount());
        assertThat(shoppingCart.getDeliveryCost()).isEqualTo(Amount.valueOf(5.99D));
        assertThat(shoppingCart.getTotalAmount()).isEqualTo(Amount.valueOf(90.99D));
        assertShoppingCartConsoleOutput();
    }

    @Test
    public void givenCampaign2_whenSubmitCartWithZeroProductsInFoodCategory_thenDoNotApplyDiscountAmount() {
        //given
        trendyolShoppingCartClient.addCampaign(CampaignName.valueOf("CAMPAIGN-2"));
        //IfQuantityOfProductsInFoodCategoryGreaterEquals10ThenApplyDiscountAmountOf15

        Product beer = productService.get(Title.valueOf("BEER")).orElse(null);
        assertThat(beer).isNotNull();
        assertThat(beer.getTitle()).isEqualTo(Title.valueOf("BEER"));

        // 20 x 10 = 200
        shoppingCart.addProduct(beer, Quantity.valueOf(10));

        //when
        trendyolShoppingCartClient.submitCart(shoppingCart);

        //then
        assertThat(trendyolShoppingCartClient.getRuleDiscountProvider().getChildDiscountProviders()).isNotEmpty();
        assertThat(shoppingCart.getDiscountMap()).hasSize(0);
        assertThat(shoppingCart.getCartAmountWithoutDiscount()).isEqualTo(Amount.valueOf(200D));
        assertThat(shoppingCart.getTotalDiscount()).isEqualTo(Amount.valueOf(0D));
        assertThat(shoppingCart.getCartAmount()).isEqualTo(Amount.valueOf(200D));
        assertThat(shoppingCart.getCartAmount()).isEqualTo(shoppingCart.getCartAmountWithoutDiscount());
        assertThat(shoppingCart.getDeliveryCost()).isEqualTo(Amount.valueOf(5.99D));
        assertThat(shoppingCart.getTotalAmount()).isEqualTo(Amount.valueOf(205.99D));
        assertShoppingCartConsoleOutput();
    }

    @Test
    public void givenCampaign2_whenSubmitCartWithQuantityOfProductsLessThen10_thenDoNotApplyDiscountAmount() {
        //given
        trendyolShoppingCartClient.addCampaign(CampaignName.valueOf("CAMPAIGN-2"));
        //IfQuantityOfProductsInFoodCategoryGreaterEquals10ThenApplyDiscountAmountOf15

        Product apple = productService.get(Title.valueOf("APPLE")).orElse(null);
        assertThat(apple).isNotNull();
        assertThat(apple.getTitle()).isEqualTo(Title.valueOf("APPLE"));

        // 10 x 1 = 10
        shoppingCart.addProduct(apple, Quantity.valueOf(1));

        //when
        trendyolShoppingCartClient.submitCart(shoppingCart);

        //then
        assertThat(trendyolShoppingCartClient.getRuleDiscountProvider().getChildDiscountProviders()).isNotEmpty();
        assertThat(shoppingCart.getDiscountMap()).hasSize(0);
        assertThat(shoppingCart.getCartAmountWithoutDiscount()).isEqualTo(Amount.valueOf(10D));
        assertThat(shoppingCart.getTotalDiscount()).isEqualTo(Amount.valueOf(0D));
        assertThat(shoppingCart.getCartAmount()).isEqualTo(Amount.valueOf(10D));
        assertThat(shoppingCart.getCartAmount()).isEqualTo(shoppingCart.getCartAmountWithoutDiscount());
        assertThat(shoppingCart.getDeliveryCost()).isEqualTo(Amount.valueOf(5.99D));
        assertThat(shoppingCart.getTotalAmount()).isEqualTo(Amount.valueOf(15.99D));
        assertShoppingCartConsoleOutput();
    }

    @Test
    public void givenCampaign1AndCampaign2_whenQuantityOfProductsInFoodCategoryLessThan10_thenOnlyApplyCampaign1DiscountAmount() {
        //given
        trendyolShoppingCartClient.addCampaign(CampaignName.valueOf("CAMPAIGN-1"));
        //IfQuantityOfProductsInFoodCategoryGreaterEquals5ThenApplyDiscountRateOf10
        trendyolShoppingCartClient.addCampaign(CampaignName.valueOf("CAMPAIGN-2"));
        //IfQuantityOfProductsInFoodCategoryGreaterEquals10ThenApplyDiscountAmountOf15

        Product apple = productService.get(Title.valueOf("APPLE")).orElse(null);
        assertThat(apple).isNotNull();
        assertThat(apple.getTitle()).isEqualTo(Title.valueOf("APPLE"));
        Product banana = productService.get(Title.valueOf("BANANA")).orElse(null);
        assertThat(banana).isNotNull();
        assertThat(banana.getTitle()).isEqualTo(Title.valueOf("BANANA"));

        // 10 x 4 = 40
        shoppingCart.addProduct(apple, Quantity.valueOf(4));
        // 20 x 1 = 20
        shoppingCart.addProduct(banana, Quantity.valueOf(1));

        //when
        trendyolShoppingCartClient.submitCart(shoppingCart);

        //then
        assertThat(trendyolShoppingCartClient.getRuleDiscountProvider().getChildDiscountProviders()).isNotEmpty();
        assertThat(shoppingCart.getDiscountMap()).hasSize(1);
        assertThat(shoppingCart.getCartAmountWithoutDiscount()).isEqualTo(Amount.valueOf(60D));
        assertThat(shoppingCart.getTotalDiscount()).isEqualTo(Amount.valueOf(6D));
        assertThat(shoppingCart.getCartAmount()).isEqualTo(Amount.valueOf(54D));
        assertThat(shoppingCart.getCartAmount()).isNotEqualTo(shoppingCart.getCartAmountWithoutDiscount());
        assertThat(shoppingCart.getDeliveryCost()).isEqualTo(Amount.valueOf(7.99D));
        assertThat(shoppingCart.getTotalAmount()).isEqualTo(Amount.valueOf(61.99D));
        assertShoppingCartConsoleOutput();
    }

    @Test
    public void givenCampaign1AndCampaign2_whenQuantityOfProductsInFoodCategoryGreaterThan10_thenOnlyApplyCampaign2DiscountAmount() {
        //given
        trendyolShoppingCartClient.addCampaign(CampaignName.valueOf("CAMPAIGN-1"));
        //IfQuantityOfProductsInFoodCategoryGreaterEquals5ThenApplyDiscountRateOf10
        trendyolShoppingCartClient.addCampaign(CampaignName.valueOf("CAMPAIGN-2"));
        //IfQuantityOfProductsInFoodCategoryGreaterEquals10ThenApplyDiscountAmountOf15

        Product apple = productService.get(Title.valueOf("APPLE")).orElse(null);
        assertThat(apple).isNotNull();
        assertThat(apple.getTitle()).isEqualTo(Title.valueOf("APPLE"));
        Product banana = productService.get(Title.valueOf("BANANA")).orElse(null);
        assertThat(banana).isNotNull();
        assertThat(banana.getTitle()).isEqualTo(Title.valueOf("BANANA"));

        // 10 x 8 = 80
        shoppingCart.addProduct(apple, Quantity.valueOf(8));
        // 20 x 4 = 80
        shoppingCart.addProduct(banana, Quantity.valueOf(4));

        //when
        trendyolShoppingCartClient.submitCart(shoppingCart);

        //then
        assertThat(trendyolShoppingCartClient.getRuleDiscountProvider().getChildDiscountProviders()).isNotEmpty();
        assertThat(shoppingCart.getDiscountMap()).hasSize(1);
        assertThat(shoppingCart.getCartAmountWithoutDiscount()).isEqualTo(Amount.valueOf(160D));
        assertThat(shoppingCart.getTotalDiscount()).isEqualTo(Amount.valueOf(15D));
        assertThat(shoppingCart.getCartAmount()).isEqualTo(Amount.valueOf(145D));
        assertThat(shoppingCart.getCartAmount()).isNotEqualTo(shoppingCart.getCartAmountWithoutDiscount());
        assertThat(shoppingCart.getDeliveryCost()).isEqualTo(Amount.valueOf(7.99D));
        assertThat(shoppingCart.getTotalAmount()).isEqualTo(Amount.valueOf(152.99D));
        assertShoppingCartConsoleOutput();
    }


    @Test
    public void givenCampaign3_whenSubmitCart_thenApplyDiscountAmount() {
        //given
        trendyolShoppingCartClient.addCampaign(CampaignName.valueOf("CAMPAIGN-3"));
        //IfQuantityOfProductsInBeverageCategoryGreaterEquals2ThenApplyDiscountRateOf10

        Product water = productService.get(Title.valueOf("WATER")).orElse(null);
        assertThat(water).isNotNull();
        assertThat(water.getTitle()).isEqualTo(Title.valueOf("WATER"));
        Product beer = productService.get(Title.valueOf("BEER")).orElse(null);
        assertThat(beer).isNotNull();
        assertThat(beer.getTitle()).isEqualTo(Title.valueOf("BEER"));

        // 2 x 1 = 2
        shoppingCart.addProduct(water, Quantity.valueOf(1));
        // 20 x 1 = 20
        shoppingCart.addProduct(beer, Quantity.valueOf(1));

        //when
        trendyolShoppingCartClient.submitCart(shoppingCart);

        //then
        assertThat(trendyolShoppingCartClient.getRuleDiscountProvider().getChildDiscountProviders()).isNotEmpty();
        assertThat(shoppingCart.getDiscountMap()).hasSize(1);
        assertThat(shoppingCart.getCartAmountWithoutDiscount()).isEqualTo(Amount.valueOf(22D));
        assertThat(shoppingCart.getTotalDiscount()).isEqualTo(Amount.valueOf(2.2D));
        assertThat(shoppingCart.getCartAmount()).isEqualTo(Amount.valueOf(19.8D));
        assertThat(shoppingCart.getCartAmount()).isNotEqualTo(shoppingCart.getCartAmountWithoutDiscount());
        assertThat(shoppingCart.getDeliveryCost()).isEqualTo(Amount.valueOf(5.99D));
        assertThat(shoppingCart.getTotalAmount()).isEqualTo(Amount.valueOf(100.99D));
        assertShoppingCartConsoleOutput();
    }

    @Test
    public void givenCampaign3_whenSubmitCartWithZeroProductsInBeverageCategory_thenDoNotApplyDiscountAmount() {
        //given
        trendyolShoppingCartClient.addCampaign(CampaignName.valueOf("CAMPAIGN-3"));
        //IfQuantityOfProductsInBeverageCategoryGreaterEquals2ThenApplyDiscountRateOf10

        Product pear = productService.get(Title.valueOf("PEAR")).orElse(null);
        assertThat(pear).isNotNull();
        assertThat(pear.getTitle()).isEqualTo(Title.valueOf("PEAR"));

        // 15 x 10 = 150
        shoppingCart.addProduct(pear, Quantity.valueOf(10));

        //when
        trendyolShoppingCartClient.submitCart(shoppingCart);

        //then
        assertThat(trendyolShoppingCartClient.getRuleDiscountProvider().getChildDiscountProviders()).isNotEmpty();
        assertThat(shoppingCart.getDiscountMap()).hasSize(0);
        assertThat(shoppingCart.getCartAmountWithoutDiscount()).isEqualTo(Amount.valueOf(150D));
        assertThat(shoppingCart.getTotalDiscount()).isEqualTo(Amount.valueOf(0D));
        assertThat(shoppingCart.getCartAmount()).isEqualTo(Amount.valueOf(150D));
        assertThat(shoppingCart.getCartAmount()).isEqualTo(shoppingCart.getCartAmountWithoutDiscount());
        assertThat(shoppingCart.getDeliveryCost()).isEqualTo(Amount.valueOf(5.99D));
        assertThat(shoppingCart.getTotalAmount()).isEqualTo(Amount.valueOf(155.99D));
        assertShoppingCartConsoleOutput();
    }

    @Test
    public void givenCampaign3_whenSubmitCartWithQuantityOfProductsLessThen2_thenDoNotApplyDiscountAmount() {
        //given
        trendyolShoppingCartClient.addCampaign(CampaignName.valueOf("CAMPAIGN-3"));
        //IfQuantityOfProductsInBeverageCategoryGreaterEquals2ThenApplyDiscountRateOf10

        Product water = productService.get(Title.valueOf("WATER")).orElse(null);
        assertThat(water).isNotNull();
        assertThat(water.getTitle()).isEqualTo(Title.valueOf("WATER"));

        // 2 x 1 = 2
        shoppingCart.addProduct(water, Quantity.valueOf(1));

        //when
        trendyolShoppingCartClient.submitCart(shoppingCart);

        //then
        assertThat(trendyolShoppingCartClient.getRuleDiscountProvider().getChildDiscountProviders()).isNotEmpty();
        assertThat(shoppingCart.getDiscountMap()).hasSize(0);
        assertThat(shoppingCart.getCartAmountWithoutDiscount()).isEqualTo(Amount.valueOf(2D));
        assertThat(shoppingCart.getTotalDiscount()).isEqualTo(Amount.valueOf(0D));
        assertThat(shoppingCart.getCartAmount()).isEqualTo(Amount.valueOf(2D));
        assertThat(shoppingCart.getCartAmount()).isEqualTo(shoppingCart.getCartAmountWithoutDiscount());
        assertThat(shoppingCart.getDeliveryCost()).isEqualTo(Amount.valueOf(5.99D));
        assertThat(shoppingCart.getTotalAmount()).isEqualTo(Amount.valueOf(7.99D));
        assertShoppingCartConsoleOutput();
    }

    @Test
    public void givenCampaign4_whenSubmitCart_thenApplyDiscountAmount() {
        //given
        trendyolShoppingCartClient.addCampaign(CampaignName.valueOf("CAMPAIGN-4"));
        //IfQuantityOfProductsInAlcoholicBeverageCategoryGreaterEquals5ThenApplyDiscountAmountOf15

        Product beer = productService.get(Title.valueOf("BEER")).orElse(null);
        assertThat(beer).isNotNull();
        assertThat(beer.getTitle()).isEqualTo(Title.valueOf("BEER"));

        // 20 x 10 = 200
        shoppingCart.addProduct(beer, Quantity.valueOf(10));

        //when
        trendyolShoppingCartClient.submitCart(shoppingCart);

        //then
        assertThat(trendyolShoppingCartClient.getRuleDiscountProvider().getChildDiscountProviders()).isNotEmpty();
        assertThat(shoppingCart.getDiscountMap()).hasSize(1);
        assertThat(shoppingCart.getCartAmountWithoutDiscount()).isEqualTo(Amount.valueOf(200D));
        assertThat(shoppingCart.getTotalDiscount()).isEqualTo(Amount.valueOf(15D));
        assertThat(shoppingCart.getCartAmount()).isEqualTo(Amount.valueOf(185D));
        assertThat(shoppingCart.getCartAmount()).isNotEqualTo(shoppingCart.getCartAmountWithoutDiscount());
        assertThat(shoppingCart.getDeliveryCost()).isEqualTo(Amount.valueOf(5.99D));
        assertThat(shoppingCart.getTotalAmount()).isEqualTo(Amount.valueOf(190.99D));
        assertShoppingCartConsoleOutput();
    }

    @Test
    public void givenCampaign4_whenSubmitCartWithZeroProductsInAlcoholicBeverageCategory_thenDoNotApplyDiscountAmount() {
        //given
        trendyolShoppingCartClient.addCampaign(CampaignName.valueOf("CAMPAIGN-4"));
        //IfQuantityOfProductsInAlcoholicBeverageCategoryGreaterEquals5ThenApplyDiscountAmountOf15

        Product water = productService.get(Title.valueOf("WATER")).orElse(null);
        assertThat(water).isNotNull();
        assertThat(water.getTitle()).isEqualTo(Title.valueOf("WATER"));

        // 2 x 10 = 20
        shoppingCart.addProduct(water, Quantity.valueOf(50));

        //when
        trendyolShoppingCartClient.submitCart(shoppingCart);

        //then
        assertThat(trendyolShoppingCartClient.getRuleDiscountProvider().getChildDiscountProviders()).isNotEmpty();
        assertThat(shoppingCart.getDiscountMap()).hasSize(0);
        assertThat(shoppingCart.getCartAmountWithoutDiscount()).isEqualTo(Amount.valueOf(100D));
        assertThat(shoppingCart.getTotalDiscount()).isEqualTo(Amount.valueOf(0D));
        assertThat(shoppingCart.getCartAmount()).isEqualTo(Amount.valueOf(100D));
        assertThat(shoppingCart.getCartAmount()).isEqualTo(shoppingCart.getCartAmountWithoutDiscount());
        assertThat(shoppingCart.getDeliveryCost()).isEqualTo(Amount.valueOf(5.99D));
        assertThat(shoppingCart.getTotalAmount()).isEqualTo(Amount.valueOf(105.99D));
        assertShoppingCartConsoleOutput();
    }

    @Test
    public void givenCampaign4_whenSubmitCartWithQuantityOfProductsLessThen5_thenDoNotApplyDiscountAmount() {
        //given
        trendyolShoppingCartClient.addCampaign(CampaignName.valueOf("CAMPAIGN-4"));
        //IfQuantityOfProductsInAlcoholicBeverageCategoryGreaterEquals5ThenApplyDiscountAmountOf15

        Product beer = productService.get(Title.valueOf("BEER")).orElse(null);
        assertThat(beer).isNotNull();
        assertThat(beer.getTitle()).isEqualTo(Title.valueOf("BEER"));

        // 20 x 2 = 40
        shoppingCart.addProduct(beer, Quantity.valueOf(2));

        //when
        trendyolShoppingCartClient.submitCart(shoppingCart);

        //then
        assertThat(trendyolShoppingCartClient.getRuleDiscountProvider().getChildDiscountProviders()).isNotEmpty();
        assertThat(shoppingCart.getDiscountMap()).hasSize(0);
        assertThat(shoppingCart.getCartAmountWithoutDiscount()).isEqualTo(Amount.valueOf(40D));
        assertThat(shoppingCart.getTotalDiscount()).isEqualTo(Amount.valueOf(0D));
        assertThat(shoppingCart.getCartAmount()).isEqualTo(Amount.valueOf(40D));
        assertThat(shoppingCart.getCartAmount()).isEqualTo(shoppingCart.getCartAmountWithoutDiscount());
        assertThat(shoppingCart.getDeliveryCost()).isEqualTo(Amount.valueOf(5.99D));
        assertThat(shoppingCart.getTotalAmount()).isEqualTo(Amount.valueOf(45.99D));
        assertShoppingCartConsoleOutput();
    }

    @Test
    public void givenCoupon1AndCampaign1_whenSubmitCart_thenApplyCampaignDiscountsBeforeThanCouponDiscounts() {
        //given
        trendyolShoppingCartClient.addCoupon(CouponCode.valueOf("COUPON-1"));
        //IfCartAmountGreaterEquals50ThenApplyDiscountAmountOf5
        trendyolShoppingCartClient.addCampaign(CampaignName.valueOf("CAMPAIGN-1"));
        //IfQuantityOfProductsInFoodCategoryGreaterEquals5ThenApplyDiscountRateOf10


        Product apple = productService.get(Title.valueOf("APPLE")).orElse(null);
        assertThat(apple).isNotNull();
        assertThat(apple.getTitle()).isEqualTo(Title.valueOf("APPLE"));
        Product banana = productService.get(Title.valueOf("BANANA")).orElse(null);
        assertThat(banana).isNotNull();
        assertThat(banana.getTitle()).isEqualTo(Title.valueOf("BANANA"));

        // 10 x 4 = 40
        shoppingCart.addProduct(apple, Quantity.valueOf(4));
        // 20 x 1 = 20
        shoppingCart.addProduct(banana, Quantity.valueOf(1));

        //when
        trendyolShoppingCartClient.submitCart(shoppingCart);

        //then
        /*
        If campaign1 is applied first, then 10% discount rate should be applied to cart amount 60: discount amount=6
        After discount of campaign1 the current cart amount: 60 - 6 = 54
        If coupon1 is applied last, then discount amount of 5 should be applied: discount amount=5
        After discount of coupon1 the current cart amount: 54 - 5 = 49
        Total discount should be 11 : 6 + 5 = 11
         */

        /*
        If coupon1 is applied first, then discount amount of 5 should be applied: discount amount=5
        After discount of coupon1 the current cart amount: 60 - 5 = 55
        If campaign1 is applied last, then 10% discount rate should be applied to cart amount 55: discount amount=5.5
        After discount of campaign1 the current cart amount: 55 - 5.5 = 49.5
        Total discount should be 10.5 : 6 + 5 = 10.5
         */

        assertThat(trendyolShoppingCartClient.getRuleDiscountProvider().getChildDiscountProviders()).isNotEmpty();
        assertThat(shoppingCart.getDiscountMap()).hasSize(2);
        assertThat(shoppingCart.getCartAmountWithoutDiscount()).isEqualTo(Amount.valueOf(60D));
        assertThat(shoppingCart.getTotalDiscount()).isEqualTo(Amount.valueOf(11D));
        assertThat(shoppingCart.getCartAmount()).isEqualTo(Amount.valueOf(49D));
        assertThat(shoppingCart.getCartAmount()).isNotEqualTo(shoppingCart.getCartAmountWithoutDiscount());
        assertThat(shoppingCart.getDeliveryCost()).isEqualTo(Amount.valueOf(7.99D));
        assertThat(shoppingCart.getTotalAmount()).isEqualTo(Amount.valueOf(56.99D));
        assertShoppingCartConsoleOutput();
    }
}
