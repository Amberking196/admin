package com.server.module.customer.coupon;

import java.util.List;

import com.server.module.customer.product.ShoppingGoodsBean;
import com.server.util.ReturnDataUtil;

/**
 * author name: yjr
 * create time: 2018-07-06 14:05:39
 */
public interface CouponService {


	/**
	 * 手机端 查询用户优惠券
	 * @author why
	 * @date 2018年9月20日 上午11:43:48 
	 * @param condition
	 * @return
	 */
    public ReturnDataUtil listPage(CouponCondition condition);

    /**
     * 修改优惠券信息
     * @author why
     * @date 2019年2月15日 下午2:25:14 
     * @param entity
     * @return
     */
    public boolean update(CouponBean entity);

    /**
     * 删除优惠券信息
     * @author why
     * @date 2019年2月15日 下午2:25:28 
     * @param id
     * @return
     */
    public boolean del(Object id);

    /**
     * 查询优惠券信息
     * @author why
     * @date 2019年2月15日 下午2:25:38 
     * @param id
     * @return
     */
    public CouponBean get(Object id);

    /**
     * 增加优惠券信息
     * @author why
     * @date 2019年2月15日 下午2:25:50 
     * @param entity
     * @return
     */
    public CouponBean add(CouponBean entity);

    /**
     * 优惠券绑定用户
     * @author why
     * @date 2019年2月15日 下午2:23:45 
     * @param couponId
     * @return
     */
    ReturnDataUtil addCouponToCustomer(Integer couponId);

    /**
     * 用户可用优惠券列表
     * @author why
     * @date 2019年2月15日 下午2:23:10 
     * @param productIds
     * @param prices
     * @return
     */
    List<CouponVo> usableCoupons(Long[] productIds, Double[] prices);
    
    /**
     *  查询优惠券下绑定的商品
     * @param couponId
     * @return
     */
     public List<ShoppingGoodsBean> findShoppingGoodsBean(int couponId);

     
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
 	 * 根据商品名称获取优惠券信息
 	 * @author hjc
 	 * @date 2018年8月25日上午10:08:01
 	 * @param shoppingGoodsBean
 	 * @return
 	 */
 	public CouponBean getCouponInfoByProduct(ShoppingGoodsBean shoppingGoodsBean);
 	
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
     * 获取优惠券信息
     * @author why
     * @date 2019年2月16日 下午2:49:51 
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

