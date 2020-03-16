package com.server.module.customer.product;

import com.server.module.system.couponManager.coupon.CouponForm;
import com.server.module.system.machineManage.machineList.VendingMachinesInfoBean;
import com.server.util.ReturnDataUtil;

import java.util.List;

/**
 * author name: why create time: 2018-06-29 11:17:55
 */
public interface ShoppingGoodsMealDao {


	public ShoppingGoodsMeal insert(ShoppingGoodsMeal entity);

	public boolean update(ShoppingGoodsMeal entity);

	public boolean delete(Object id);

	public ShoppingGoodsMeal get(Object id);


}
