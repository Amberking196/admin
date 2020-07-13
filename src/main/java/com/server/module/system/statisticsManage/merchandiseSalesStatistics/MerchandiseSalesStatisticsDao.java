package com.server.module.system.statisticsManage.merchandiseSalesStatistics;

import com.server.util.ReturnDataUtil;

/**
 * author name: why create time: 2018-05-09 21:15:27
 */
public interface MerchandiseSalesStatisticsDao {

	/**
	 * 商品销售统计列表 查询
	 * @param condition
	 * @return
	 */
	public ReturnDataUtil listPage(MerchandiseSalesStatisticsCondition condition);

	
}
