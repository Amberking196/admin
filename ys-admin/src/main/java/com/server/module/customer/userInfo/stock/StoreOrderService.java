package com.server.module.customer.userInfo.stock;

import java.util.List;

public interface StoreOrderService {

	/**
	 * 获取用户 拥有当前商品的存量
	 * @author hebiting
	 * @date 2018年7月16日下午6:13:19
	 * @param itemId
	 * @return
	 */
	public CustomerStockBean getStock(Integer itemId,Long customerId);
	
	/**
	 * 插入库存
	 * @author hebiting
	 * @date 2018年7月17日上午10:27:12
	 * @param cusStock
	 * @return
	 */
	public CustomerStockBean insert(CustomerStockBean cusStock);
	
	/**
	 * 更新顾客库存
	 * @author hebiting
	 * @date 2018年7月17日上午10:27:55
	 * @param cusStock
	 * @return
	 */
	public boolean update(CustomerStockBean cusStock);
	
	/**
	 * 获取订单详情
	 * @author hebiting
	 * @date 2018年7月17日上午11:00:37
	 * @param ptCode
	 * @return
	 */
	public List<OrderDetailBean> getOrderDetail(String payCode);
	
	/**
	 * 根据机器商品id获取商城商品id
	 * @author hebiting
	 * @date 2018年7月20日上午8:46:25
	 * @param storeItem
	 * @return
	 */
	public Integer getStoreItem(Integer itemId);
	
	/**
	 * 插入优惠券使用日志，并返回id
	 * @author hebiting
	 * @date 2018年7月20日上午9:22:00
	 * @param coupouLog
	 * @return
	 */
	public Integer insertCouponLog(CouponLog coupouLog);
	
	/**
	 * 增加取水记录
	 * @author hebiting
	 * @date 2018年7月24日下午4:24:22
	 * @param pickRecord
	 * @return
	 */
	public Integer insertPickRecord(CustomerPickRecord pickRecord);
}
