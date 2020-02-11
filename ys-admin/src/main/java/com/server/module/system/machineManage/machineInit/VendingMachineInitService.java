package com.server.module.system.machineManage.machineInit;

import com.server.util.ReturnDataUtil;

/**
 * author name: why
 * create time: 2018-04-03 09:55:12
 */
public interface VendingMachineInitService {

	/**
	 * 查询售货机初始化
	 */
	public ReturnDataUtil listPage(VendingMachineInitCondition condition);
}
