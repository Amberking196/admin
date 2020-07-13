package com.server.module.system.replenishManage.replenishCorrect;

import java.util.List;

public interface ReplenishCorrectDao {

	/**
	 * 获取补货信息
	 * @author hebiting
	 * @date 2019年1月3日上午11:16:24
	 * @param form
	 * @return
	 */
	public List<ReplenishDto> getReplenishInfo(ReplenishForm form);
	/**
	 * 将校准补货记录改为正常补货记录
	 * @author hebiting
	 * @date 2019年1月3日下午4:55:33
	 * @param ids
	 * @return
	 */
	public boolean updateReplenishInfo(String ids);
	
	/**
	 * 获取补货信息
	 * @author hjc
	 * @date 2019年5月23日上午11:16:24
	 * @param form
	 * @return
	 */
	public List<ReplenishDto> getReplenishProcess(ReplenishForm form);
}
