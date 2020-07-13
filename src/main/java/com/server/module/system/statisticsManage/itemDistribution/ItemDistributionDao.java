package com.server.module.system.statisticsManage.itemDistribution;

import java.util.List;

public interface ItemDistributionDao {

	/**
	 * 查询版本1机器商品分布信息
	 * @author hebiting
	 * @date 2018年9月13日下午6:12:12
	 * @param form
	 * @return
	 */
	List<ItemDistributionDto> queryDistribution(ItemDistributionForm form);
	/**
	 * 查询版本2机器商品分布信息
	 * @author hebiting
	 * @date 2018年11月13日下午2:54:34
	 * @param form
	 * @return
	 */
	List<ItemDistributionDto> queryDisTributionVer2(ItemDistributionForm form);
	
	/**
	 * 版本1根据条件的总记录数
	 * @author hebiting
	 * @date 2018年9月15日下午3:35:53
	 * @param form
	 * @return
	 */
	Long queryDistributionNum(ItemDistributionForm form);
	
	/**
	 * 版本2 根据条件的总记录数
	 * @author hebiting
	 * @date 2018年9月15日下午3:35:53
	 * @param form
	 * @return
	 */
	Long queryDistributionNumVer2(ItemDistributionForm form);
	
}
