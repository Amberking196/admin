package com.server.module.system.machineManage.machineAccountLibrary;

import com.server.util.ReturnDataUtil;

/***
 * 
 * 
 * author name: why
 * create time: 2018-04-02 11:11:50
 *
 */
public interface VendingMachineAccountLibraryService {

	/**
	 *  售货机商品库 查询
	 * @param condition
	 * @return
	 */
	public ReturnDataUtil listPage(VendingMachineAccountLibraryCondition condition);
}
