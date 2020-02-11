package com.server.module.system.statisticsManage.notSaleMachines;

import java.util.List;

public interface NotSaleMachinesDao {

	/**
	 * 根据条件查询未销售机器信息
	 * @author hebiting
	 * @date 2018年6月1日上午10:15:44
	 * @param form
	 * @return
	 */
	public List<NotSaleMachinesDto> findNotSaleMachinesByForm(NotSaleMachinesForm form);
	
	/**
	 * 根据条件查询未销售机器的记录总条数
	 * @author hebiting
	 * @date 2018年6月1日上午10:15:44
	 * @param form
	 * @return
	 */
	public Long findNumOfNotSaleMachinesByForm(NotSaleMachinesForm form);
}
