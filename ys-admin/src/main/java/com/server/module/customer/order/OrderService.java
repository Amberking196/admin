package com.server.module.customer.order;

import java.util.List;
import java.util.Map;

import com.server.module.system.statisticsManage.payRecord.PayRecordItemDto;
import com.server.util.ReturnDataUtil;
import com.server.util.stateEnum.PayStateEnum;

public interface OrderService {

	/**
	 * 根据openId查询用户所有订单
	 * @author hebiting
	 * @date 2018年6月20日上午9:22:29
	 * @return
	 */
	public List<OrderDto> findOrderByOpenId(String openId);

	/**
	 * 根据客户id查询订单
	 * @author hebiting
	 * @date 2018年7月9日下午7:27:04
	 * @param customerId
	 * @return
	 */
	public List<OrderDto> findOrderById(Long customerId,Integer type);

	/**
	 * 根据product查找商品详情
	 * @return
	 */
	public List<ShoppingBean> findCustomerByProduce(String product);
   
	
	/**
	 * 添加商城订单
	 * @return
	 */
	public ReturnDataUtil add(OrderForm orderform);



	/**
	 *  批量删除商城订单
	 * @return
	 */

	public ReturnDataUtil del(String orderIdList);


	/**
	 *  查询用户订单
	 * @return
	 */


	public ReturnDataUtil storeOrderFind(OrderForm orderform);

	/**
	 * 查询订单详情   pic返回完整路径
	 * @author HHH
	 * @date 2018年11月14日 下午4:20:48
	 * @param orderId
	 * @param orderType
	 * @return
	 */
	public List<ShoppingBean> findShoppingBeandByOrderId(Long orderId,Integer orderType);
   

	/**
	 *  更新用户订单
	 * @return
	 */



	public boolean update(OrderForm orderform);

	/**
	 *  根据product找orderId
	 * @return
	 */
	public int findOrderIdByProduct(OrderForm orderform);


	/**
	 *  根据orderId找nowprice,product,payCode
	 * @return
	 */
	public Map<String, Object> findSomeMessByOrderId(OrderForm orderForm);


	/**
	 *  根据登陆id查找客户地址
	 * @return
	 */

	public List<String> addressFind(OrderForm orderform);
    
	
	/**
	 *  根据登陆id查找客户优惠券
	 * @return
	 */
	public ReturnDataUtil customerCoupon(OrderForm orderform);
	
	/**
	 * 拼团订单增加
	 * @author why
	 * @date 2018年10月17日 下午3:55:39 
	 * @param orderForm
	 * @return
	 */
	public ReturnDataUtil addSpellGroup(OrderForm orderForm);
     
	/**
	 * 根据openID 查询用户id
	 * @author why
	 * @date 2018年10月23日 下午10:33:28
	 * @param openId
	 * @return
	 */
	public Long findCustomerIdByOpenId(String openId);

	/**
	 * 根据orderId 查询订单paycode
	 * @author HHH
	 * @date 2020年2月28日00:28:45
	 * @param orderId
	 * @return
	 */
	public String findPayCodeByOrderId(Long orderId);
	
	/**
	 * 获取订单商品详情
	 * @author hebiting
	 * @date 2018年11月28日下午5:08:09
	 * @param payCode
	 * @return
	 */
	public List<PayRecordItemDto> getPayRecordItemList(String payCode);
	
	/**
	 * 更新订单状态(仅在订单状态为待支付时，更改订单状态state及updateTime)
	 * @author hebiting
	 * @date 2018年12月25日下午4:39:08
	 * @param orderId
	 * @param state
	 * @return
	 */
	public boolean updateOrderStateById(Long orderId,PayStateEnum state);
	
	/**
	 * 我的拼团订单
	 * @author why
	 * @date 2019年1月14日 上午11:46:53 
	 * @param orderForm
	 * @return
	 */
	public ReturnDataUtil mySpellgroupOrder(OrderForm orderForm);
	
	/**
	 * 查询团购商品取货地址
	 * @author why
	 * @date 2019年1月14日 下午4:38:05 
	 * @param spellgroupId
	 * @return
	 */
	public ReturnDataUtil pickUpAddress(Long spellgroupId);
	
	/**
	 * 拼团订单详情
	 * @author why
	 * @date 2019年1月14日 下午5:29:30 
	 * @param orderId
	 * @return
	 */
	public ShoppingDto orderParticulars(Long orderId);
	
	/**
	 * 发起支付判断该团是否拼成功
	 * @author why
	 * @date 2019年1月25日 下午6:39:57 
	 * @param orderId
	 * @return
	 */
	public boolean isPaymentAllowed(Long orderId);
	
	
	/**
	 * 根据payCode查询公司id
	 * @author why
	 * @date 2019年3月16日 上午11:31:36 
	 * @param payCode
	 * @return
	 */
	public Integer getCompanyIdByPayCode(String payCode);

	/**
	 * 根据payCode查询订单的配送方式
	 * @author why
	 * @date 2019年3月16日 上午11:31:36
	 * @param payCode
	 * @return
	 */
	public Integer getDistributionModelByPayCode(String payCode);

	/**
	 * 根据payCode查询订单的部分信息
	 * @author hhh
	 * @date 2019年3月16日 上午11:31:36
	 * @param payCode
	 * @return
	 */
	public OrderBean getMessageByPayCode(String payCode);
	
	
	/**
	 * 微信完成商城订单后的回调，用以确认订单是否完成支付，并更新状态
	 * 
	 * @author why
	 * @date 2019年2月15日 下午5:39:34
	 * @param outTradeNo
	 * @param transactionId
	 * @param type
	 *            0 普通订单 其他为团购订单
	 */
	public int paySuccessStroeOrder(Integer distributionModel,String outTradeNo, String transactionId, Integer type);


	/**
	 *  更新用户订单配送状态
	 * @return
	 */
	public boolean delivering(Long orderId);
}
