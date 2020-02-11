package com.server.module.system.statisticsManage.notSaleMachines;

import com.server.util.ReturnDataUtil;

public interface NotSaleMachinesService {

	/**
	 * 根据条件查询未销售机器信息
	 * @author hebiting
	 * @date 2018年6月1日上午10:15:44
	 * @param form
	 * @return
	 */
	public ReturnDataUtil findNotSaleMachinesByForm(NotSaleMachinesForm form);
}
