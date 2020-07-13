package com.server.module.customer.coupon;

import java.util.List;

import com.server.module.customer.product.ShoppingGoodsBean;
import com.server.util.ReturnDataUtil;
/**
 * author name: yjr
 * create time: 2018-07-06 14:05:39
 */ 
public interface  CouponDao{

	/**
	 * 手机端 查询用户优惠券
	 * @author why
	 * @date 2018年9月20日 上午11:44:28 
	 * @param condition
	 * @return
	 */
	public ReturnDataUtil listPage(CouponCondition condition);
	public boolean update(CouponBean entity);
	public boolean delete(Object id);
	public CouponBean get(Object id);
	public CouponBean insert(CouponBean entity);

	/**
	 * 根据商品名称获取优惠券信息
	 * @author hjc
	 * @date 2018年8月25日上午10:08:01
	 * @param shoppingGoodsBean
	 * @return
	 */
	public CouponBean getCouponInfoByProduct(ShoppingGoodsBean shoppingGoodsBean);
	
	
	/**
	 * 赠券插入
	 * @author hjc
	 * @date 2018年8月28日上午9:03:50
	 * @param couCusBean
	 * @return
	 */
	public Integer insertCouponCustomer(CouponCustomerBean couCusBean);
	

	/**
	 * 获取游戏可赠送优惠券
	 * @author hebiting
	 * @date 2018年8月24日上午9:59:15
	 * @param couponForm
	 * @return
	 */
	public List<CouponDto> getGameCoupon(CouponForm couponForm);
	
	/**
	 * 获取绑定优惠券信息
	 * @author hjc
	 * @date 2018年8月25日上午10:08:01
	 * @param Long customerId, Long couponId
	 * @return
	 */
	public CouponCustomerBean getCouponCustomerBean(Long customerId, Long couponId);
	
	/**
	 * 更新绑定用户的优惠券数量(仅限于购买优惠券的用户)
	 * @author hjc
	 * @date 2018年8月25日上午10:08:01
	 * @param entity
	 * @return
	 */
	Boolean updateCouponCustomerBean(CouponCustomerBean entity);
	
	/**
	 * 查询优惠券(目前用于关注返券查询)
	 * @author hjc
	 * @date 2018年9月25日上午10:08:01
	 * @param way state
	 * @return
	 */
	public ReturnDataUtil shopListPage(Integer way ,Integer state,Integer companyId);
	
	/**
	 * 获取优惠券信息
	 * @author why
	 * @date 2019年3月16日 下午2:51:00 
	 * @param couponForm
	 * @return
	 */
	public List<CouponBean> getPresentCoupon(CouponForm couponForm);

	  /**
     * 该优惠券是否已领 
     * @author why
     * @date 2019年2月16日 下午2:59:18 
     * @param customerId
     * @param couponId
     * @return
     */
    public boolean isReceive(Long customerId,Long couponId);
}

