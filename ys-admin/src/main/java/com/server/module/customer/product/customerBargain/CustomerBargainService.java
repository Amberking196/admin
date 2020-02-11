package com.server.module.customer.product.customerBargain;


import java.math.BigDecimal;
import java.util.Map;

import com.server.module.customer.product.bargainDetail.BargainDto;
import com.server.module.sys.model.User;

public interface CustomerBargainService {

	/**
	 * 判断该砍价是否还在进行中
	 * @author hebiting
	 * @date 2018年12月22日上午9:46:27
	 * @param id
	 * @return 进行中ture
	 */
	boolean isBargaining(Long id);
	
	/**
	 *  帮朋友砍价，返回砍价金额（如果砍至最低价，则自动生成订单）
	 * @author hebiting
	 * @date 2018年12月26日下午5:08:04
	 * @param customerBargainId
	 * @param user
	 * @param barginDto
	 * @param doneTimes
	 * @return
	 */
	BigDecimal cutPirce(Long customerBargainId,User user,BargainDto barginDto,Integer doneTimes);
	
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
	 * 砍价增加
	 * @author why
	 * @date 2018年12月24日 下午3:37:57 
	 * @param customerBargainBean
	 * @return
	 */
	public Map<String,Object> insert(CustomerBargainBean customerBargainBean);
}
