package com.server.module.customer.order.storeGroupOrderManage;

import java.util.List;

public interface GroupOrderService {

	/**
	 * 通过payCode 查询商城拼团订单
	 * @author why
	 * @date 2019年2月18日 下午3:13:28 
	 * @param outTradeNo
	 * @return
	 */
	public GroupOrderBean getStoreOrderbyOutTradeNo(String outTradeNo);
	
	/**
	 * 根据用户拼团id 查询订单信息
	 * @author why
	 * @date 2019年2月18日 下午3:39:38 
	 * @param customerGroupId
	 * @return
	 */
	public List<GroupOrderBean> getStoreOrderbyCustomerGroupId(Long customerGroupId);
	
	/**
	 * 修改拼团订单状态
	 * @author why
	 * @date 2019年2月18日 下午3:48:13 
	 * @param outTradeNo
	 * @param type
	 */
	public int paySpellgroupStroeOrder(String outTradeNo,Integer type);
}
