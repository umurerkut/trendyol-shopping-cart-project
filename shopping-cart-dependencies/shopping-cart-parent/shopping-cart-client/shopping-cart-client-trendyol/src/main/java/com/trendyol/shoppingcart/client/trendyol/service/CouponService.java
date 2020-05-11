package com.trendyol.shoppingcart.client.trendyol.service;

import com.trendyol.shoppingcart.client.trendyol.discountprovider.coupon.Coupon;
import com.trendyol.shoppingcart.client.trendyol.domain.value.CouponCode;
import com.trendyol.shoppingcart.core.service.CRUDService;

public interface CouponService extends CRUDService<Coupon, CouponCode> {
}
