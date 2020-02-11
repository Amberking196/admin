package com.server.module.customer.product;

import com.server.module.system.couponManager.coupon.CouponAddVo;
import lombok.Data;
@Data
public class Param {
	//商城商品对象
	public ShoppingGoodsBean shoppingGoodsBean;
	//优惠券对象
	public CouponAddVo couponBean;  
	
}
