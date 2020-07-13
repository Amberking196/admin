package com.server.module.system.machineManage.machineInit;

import com.server.util.ReturnDataUtil;

/**
 * 
 * author name: why
 * create time: 2018-04-03 09:49:50 
 */
public interface VendingMachineInitDao {

	/**
	 * 查询售货机初始化
	 */
	public ReturnDataUtil listPage(VendingMachineInitCondition condition);
}
