package com.server.module.customer.order;

import java.util.List;
import java.util.Map;

import com.server.module.system.statisticsManage.payRecord.PayRecordItemDto;
import com.server.util.ReturnDataUtil;
import com.server.util.stateEnum.PayStateEnum;

public interface OrderDao {

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
	 * 根据product查询购物车中用户购买商品总价格
	 * @return
	 */
	public double findSumPriceByProduct(String product);


	

	/**
	 * 根据用户id查询用户的openId
	 * @return
	 */
	
	public String findOpenIdByCustomerId(Long customerId);

	/**
	 * 根据优惠券id查询减扣金额
	 * @return
	 */
	public double findDeductionMoneyByCoupon(Integer integer);
	
	/**
	 * 增加商城订单
	 * @param entity
	 * @return
	 */
	public OrderBean insert(OrderBean entity);

   
	/**
	 *  批量删除商城订单
	 * @param  orderIdList
	 * @return
	 */
	public boolean del(String orderIdList);

	/**
	 *  查询用户订单
	 * @param  orderform
	 * @return
	 */
	public ReturnDataUtil storeOrderFind(OrderForm orderform);
    
	/**
	 *  用户订单修改
	 * @param  orderform
	 * @return
	 */

	public boolean update(OrderBean ob);

	/**
	 *  根据product寻找orderId
	 * @param  product
	 * @return
	 */
	public int findOrderIdByProduct(String product);

    
	
	/**
	 *  根据orderId找nowprice,product,payCode
	 * @return
	 */
	public Map<String, Object> findSomeMessByOrderId(Integer orderId);
    
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
	 *  根据登陆id查找客户订单
	 * @return
	 */
	public ReturnDataUtil storeOrderFind(Integer customerId);
    
	

    
	
	/**
	 * 根据product查找商品详情
	 * @return
	 */
	public List<ShoppingBean> findCustomerByProduce(String product);
    
	
	/**
	 * 根据itemId查找couponId
	 * @return
	 */
	public int findCouponIdByItemId(Integer itemId);
    
	
	/**
	 * 增加订单详情
	 * @return
	 */
	public boolean insert(OrderDetile ob);
    
	
	/**
	 * 通过couponId查找商品Id
	 * @return
	 */
	public List<Integer> findItemIdByCouponId(Integer integer);
    
	
	/**
	 * 通过登陆id查找优惠券id
	 * @return
	 */
	public List<Integer> findCouponByCustomerId(Integer customerId);
    
	/**
	 * 更新优惠券的状态
	 * @return
	 */
	public void updateState(Long customerId , Integer couponId);
    
	
	/**
	 * 新增优惠券使用日志
	 * @return
	 */
	public void insert(CouponLog couponLog);

	
	/**
	 * 根据openID 查询用户id
	 * @author why
	 * @date 2018年10月17日 下午4:19:53 
	 * @param openId
	 * @return
	 */
	public Long findCustomerIdByOpenId(String openId);
	
	/**
	 * 查询订单详情
	 * @author why
	 * @date 2018年11月14日 下午4:20:48 
	 * @param orderId
	 * @param orderType
	 * @return
	 */
   public List<ShoppingBean> findOrderIdByShoppingBean(Long orderId,Integer orderType);
	
	/**
	 * 获取订单商品详情
	 * @author hebiting
	 * @date 2018年11月28日下午5:08:09
	 * @param payCode
	 * @return
	 */
	public List<PayRecordItemDto> getPayRecordItemList(String payCode);
	
	/**
	 * 更新订单状态(仅在订单状态为待支付时，更改订单状态state)
	 * @author hebiting
	 * @date 2018年12月25日下午4:39:08
	 * @param orderId
	 * @param state
	 * @return
	 */
	public boolean updateOrderStateById(Long orderId,PayStateEnum state);
	
	/**
	 * 获取商品售卖数量
	 * @author hebiting
	 * @date 2018年12月27日下午3:12:15
	 * @param itemId
	 * @return
	 */
	public Long getItemSalesNum(Long itemId);
	
	/**
	 * 我的拼团订单
	 * @author why
	 * @date 2019年1月14日 上午9:51:46 
	 * @param orderForm
	 * @return
	 */
	public ReturnDataUtil mySpellgroupOrder(OrderForm orderForm);
	
	/**
	 * 查询团购商品取货地址
	 * @author why
	 * @date 2019年1月14日 下午4:32:03 
	 * @param spellgroupId
	 * @return
	 */
	public ReturnDataUtil pickUpAddress(Long spellgroupId);
	
	/**
	 * 拼团订单详情
	 * @author why
	 * @date 2019年1月14日 下午5:21:47 
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
	 * @date 2019年3月16日 上午11:25:44 
	 * @param payCode
	 * @return
	 */
	public Integer getCompanyIdByPayCode(String payCode);

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
	public int paySuccessStroeOrder(String outTradeNo, String transactionId, Integer type);
	
	/**
	 * 查询用户购买商品次数
	 * @author why
	 * @date 2019年5月25日 上午10:19:58 
	 * @param customerId
	 * @param goodsId
	 * @return
	 */
	public int buyCount(Long customerId,Long goodsId);
     
}
