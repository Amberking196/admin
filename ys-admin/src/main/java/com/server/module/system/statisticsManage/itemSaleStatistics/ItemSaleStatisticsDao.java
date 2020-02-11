package com.server.module.system.statisticsManage.itemSaleStatistics;

import java.util.List;
import java.util.Map;

import com.server.module.commonBean.IdNameBean;

public interface ItemSaleStatisticsDao {


	/**
	 * 统计商品销售相关信息
	 * @param itemSaleStatisticsForm
	 * @return List<ItemSaleStatisticsDto>
	 */
	List<ItemSaleStatisticsDto> findItemSaleStatistics(ItemSaleStatisticsForm itemSaleStatisticsForm);
	
	/**
	 * 统计商品销售相关信息(去掉退款商品后)
	 * @param itemSaleStatisticsForm
	 * @return List<ItemSaleStatisticsDto>
	 */
	List<ItemSaleStatisticsDto> findItemSaleStatisticsAfterRefund(ItemSaleStatisticsForm itemSaleStatisticsForm);
	
	/**
	 * 查询售卖商品的总页数
	 * @return
	 */
	Long findItemSalesStatisticsNum(ItemSaleStatisticsForm itemSaleStatisticsForm);
	
	/**
	 * 查询售卖商品的总页数(去掉退款商品后)
	 * @return
	 */
	Long findItemSalesStatisticsNumAfterRefund(ItemSaleStatisticsForm itemSaleStatisticsForm);
	
	List<IdNameBean> findItemInfo();
	
	/**
	 * 根据公司id 查询在售商品信息
	 * @author why
	 * @date 2019年1月7日 上午10:56:33 
	 * @param companyId
	 * @return
	 */
	List<IdNameBean> findItemInfoByCompanyId(ItemSaleStatisticsForm itemSaleStatisticsForm);
}
