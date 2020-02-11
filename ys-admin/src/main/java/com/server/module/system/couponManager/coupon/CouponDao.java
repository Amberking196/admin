package com.server.module.system.couponManager.coupon;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.server.util.ReturnDataUtil;

/**
 * author name: yjr create time: 2018-06-28 09:01:06
 */
public interface CouponDao {

	public ReturnDataUtil listPage(CouponForm condition);

	public List<CouponBean> list(CouponForm condition);

	public boolean update(CouponBean entity);

	public boolean delete(Object id);

	public CouponBean get(Object id);

	public CouponBean insert(CouponBean entity);

	/**
	 * 获取优惠券信息
	 * @author why
	 * @date 2018年10月9日 上午10:35:34
	 * @param couponForm
	 * @return
	 */
	public List<CouponBean> getPresentCoupon(CouponForm couponForm);
	
	/**
	 * 获取亚运城活动 5折优惠券信息
	 * @author why
	 * @date 2018年11月23日 上午9:47:20 
	 * @return
	 */
	public List<CouponBean> getAsianCoupon();
	
	/**
	 * 用户是否已经领取
	 * @author why
	 * @date 2018年11月27日 上午11:28:21 
	 * @param customerId
	 * @param couponId
	 * @return
	 */
	public boolean isReceive(Long customerId, Long couponId);
	
}
