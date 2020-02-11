package com.server.module.system.statisticsManage.itemSaleStatistics;


import java.util.List;

import javax.servlet.http.HttpSession;

import com.server.module.commonBean.IdNameBean;
import com.server.util.ReturnDataUtil;

public interface ItemSaleStatisticsService {

	
	/**
	 * 统计商品销售相关信息
	 * @param itemSaleStatisticsForm
	 * @return List<ItemSaleStatisticsDto>
	 */
	ReturnDataUtil findItemSaleStatistics(ItemSaleStatisticsForm itemSaleStatisticsForm);
	
	List<IdNameBean> findItemInfo();
	
	/**
	 * 根据公司id 查询在售商品信息
	 * @author why
	 * @date 2019年1月7日 上午11:05:26 
	 * @param itemSaleStatisticsForm
	 * @return
	 */
	List<IdNameBean> findItemInfoByCompanyId(ItemSaleStatisticsForm itemSaleStatisticsForm);
}
