package com.server.module.customer.coupon;

import java.util.List;

import com.server.module.customer.product.ShoppingGoodsBean;
import com.server.util.ReturnDataUtil;

/**
 * author name: yjr create time: 2018-07-10 19:12:50
 */
public interface CouponProduct1Dao {

	/**
	 * 优惠券绑定商品信息列表
	 * @author why
	 * @date 2019年2月15日 下午2:19:09 
	 * @param condition
	 * @return
	 */
	public ReturnDataUtil listPage(CouponProductCondition condition);

	/**
	 * 查询优惠券下绑定的商品信息
	 * @author why
	 * @date 2019年2月15日 下午2:19:28 
	 * @param couponId
	 * @return
	 */
	public List<CouponProduct1Bean> list(int couponId);

	/**
	 * 修改优惠券绑定商品信息
	 * @author why
	 * @date 2019年2月15日 下午2:20:28 
	 * @param entity
	 * @return
	 */
	public boolean update(CouponProduct1Bean entity);

	/**
	 * 删除优惠券绑定商品信息
	 * @author why
	 * @date 2019年2月15日 下午2:20:51 
	 * @param id
	 * @return
	 */
	public boolean delete(Object id);

	/**
	 * 查询优惠券绑定商品信息
	 * @author why
	 * @date 2019年2月15日 下午2:21:12 
	 * @param id
	 * @return
	 */
	public CouponProduct1Bean get(Object id);

	/**
	 * 增加优惠券绑定商品信息
	 * @author why
	 * @date 2019年2月15日 下午2:21:31 
	 * @param entity
	 * @return
	 */
	public CouponProduct1Bean insert(CouponProduct1Bean entity);

	/**
	 * 查询优惠券下绑定的商品
	 * 
	 * @param couponId
	 * @return
	 */
	public List<ShoppingGoodsBean> findShoppingGoodsBean(int couponId);
}
