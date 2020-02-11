package com.server.module.system.statisticsManage.itemDistribution;

import java.util.List;

public interface ItemDistributionService {

	/**
	 * 查询商品分布信息
	 * @author hebiting
	 * @date 2018年9月13日下午6:12:12
	 * @param form
	 * @return
	 */
	List<ItemDistributionDto> queryDistribution(ItemDistributionForm form);
	
	/**
	 * 根据条件的总记录数
	 * @author hebiting
	 * @date 2018年9月15日下午3:35:53
	 * @param form
	 * @return
	 */
	Long queryDistributionNum(ItemDistributionForm form);
}
