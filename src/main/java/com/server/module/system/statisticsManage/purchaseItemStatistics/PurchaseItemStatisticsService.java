package com.server.module.system.statisticsManage.purchaseItemStatistics;

import java.util.List;

import com.server.module.system.purchase.purchaseBillItem.PurchaseBillItemBean;
import com.server.module.system.warehouseManage.warehouseWarrantDetail.WarehouseBillItemBean;
import com.server.util.ReturnDataUtil;
/**
 * author name: hjc
 * create time: 2018-08-24 11:02:40
 */ 
public interface  PurchaseItemStatisticsService{


	public ReturnDataUtil listPage(PurchaseItemStatisticsCondition condition);
	public List<PurchaseItemStatisticsBean> list(PurchaseItemStatisticsCondition condition);
	public boolean update(PurchaseItemStatisticsBean entity);
	public boolean del(Object id);
	public PurchaseItemStatisticsBean get(Long id);
	public PurchaseItemStatisticsBean add(PurchaseItemStatisticsBean entity);
	
	/**
	 * 更新 采购商品的成本统计
	 * @date 2018年9月4日 下午17:41:15
	 * @param entity
	 * @return
	 */
	public boolean addToStatistics(PurchaseBillItemBean entity);
	
	/**
	 * 获取采购商品的成本统计
	 * @date 2019年7月4日 下午17:41:15
	 * @param itemId
	 * @return
	 */
	public PurchaseItemStatisticsBean getBeanByItemId(Long itemId);
	
	/**
	 * 更新 采购商品的成本统计
	 * @date 2018年9月4日 下午17:41:15
	 * @param entity
	 * @return
	 */
	public boolean addToStatistics2(WarehouseBillItemBean entity);
}

