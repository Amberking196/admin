package com.server.module.customer.coupon;

import java.util.List;

import com.server.util.ReturnDataUtil;

/**
 * author name: yjr
 * create time: 2018-07-07 14:49:52
 */
public interface CouponCustomerDao {

	/**
	 * 用户优惠券查询列表
	 * @author why
	 * @date 2019年2月15日 上午10:58:20 
	 * @param condition
	 * @return
	 */
    public ReturnDataUtil listPage(CouponCustomerCondition condition);

    /**
     * 用户优惠券查询列表
     * @author why
     * @date 2019年2月15日 上午10:58:59 
     * @param condition
     * @return
     */
    public List<CouponCustomerBean> list(CouponCustomerCondition condition);

    /**
     * 用户优惠券修改
     * @author why
     * @date 2019年2月15日 上午10:59:19 
     * @param entity
     * @return
     */
    public boolean update(CouponCustomerBean entity);

    /**
     * 用户优惠券删除
     * @author why
     * @date 2019年2月15日 上午10:59:30 
     * @param id
     * @return
     */
    public boolean delete(Object id);

    /**
     * 用户优惠券查询
     * @author why
     * @date 2019年2月15日 上午11:00:05 
     * @param id
     * @return
     */
    public CouponCustomerBean get(Object id);

    /**
     * 下发用户优惠券
     * @author why
     * @date 2019年2月15日 上午11:00:14 
     * @param entity
     * @return
     */
    public CouponCustomerBean insert(CouponCustomerBean entity);

    /**
     * 用户可用优惠劵列表
     * @author why
     * @date 2019年2月15日 上午11:02:24 
     * @param productIds
     * @param prices
     * @return
     */
    List<CouponVo> usableCoupons(Long[] productIds, Double[] prices);
}

