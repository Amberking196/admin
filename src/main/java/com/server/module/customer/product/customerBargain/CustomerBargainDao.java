package com.server.module.customer.product.customerBargain;

import java.math.BigDecimal;

public interface CustomerBargainDao {

	/**
	 * 判断该砍价是否还在进行中
	 * @author hebiting
	 * @date 2018年12月22日上午9:46:27
	 * @param id
	 * @return 
	 */
	boolean isBargaining(Long id);
	
	/**
	 * 砍价后减少当前金额
	 * @author hebiting
	 * @date 2018年12月24日上午9:27:04
	 * @param bargainId
	 * @param cutPrice
	 * @return
	 */
	boolean cutPrice(Long bargainId,BigDecimal cutPrice);
	/**
	 * 获取砍价单信息
	 * @author hebiting
	 * @date 2018年12月24日下午2:36:51
	 * @param id
	 * @return
	 */
	CustomerBargainBean getCustomerBargain(Long id);
	
	/**
	 * 获取砍价商品信息及申请人信息
	 * @author hebiting
	 * @date 2018年12月24日下午7:00:28
	 * @param customerBargainId
	 * @return
	 */
	CustomerBargainDto getBargainsInfo(Long customerBargainId);
	
	/**
	 * 获取砍价订单信息
	 * @author hebiting
	 * @date 2018年12月25日上午9:18:03
	 * @param orderId
	 * @return
	 */
	BargainOrderDto getBargainOrderInfo(Long orderId);

	/**
	 * 更新砍价帮的状态（是否砍价成功 0失败 1成功 2砍价中）
	 * @author hebiting
	 * @date 2018年12月27日上午10:05:49
	 * @param customerBargainId
	 * @param state
	 * @return
	 */
	boolean updateBargainState(Long customerBargainId,Integer state);

	
	/**
	 * 砍价增加
	 * @author why
	 * @date 2018年12月24日 下午3:37:19 
	 * @param customerBargainBean
	 * @return
	 */
	public Long insert(CustomerBargainBean customerBargainBean);

}
