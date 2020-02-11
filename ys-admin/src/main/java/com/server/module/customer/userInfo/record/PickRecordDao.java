package com.server.module.customer.userInfo.record;

import java.util.List;

import com.server.module.customer.userInfo.stock.CustomerStockBean;

public interface PickRecordDao {

	/**
	 * 根据用户id获取用户取水记录
	 * @author hebiting
	 * @date 2018年7月24日下午4:48:43
	 * @return
	 */
	List<PickRecordBean> getPickRecord(Long customerId);
	
	/**
	 * 根据用户id及商品id获取用户取水记录
	 * @author hebiting
	 * @date 2018年7月24日下午4:48:43
	 * @return
	 */
	List<PickRecordBean> getPickRecord(Long customerId,Long itemId);
	/**
	 * 根据用户id获取用户存水
	 * @author hebiting
	 * @date 2018年7月24日下午5:05:36
	 * @param customerId
	 * @return
	 */
	List<CustomerStockBean> getCustomerStock(Long customerId);
}
