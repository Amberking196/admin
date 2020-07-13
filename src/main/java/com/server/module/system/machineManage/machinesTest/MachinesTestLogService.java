package com.server.module.system.machineManage.machinesTest;

import com.server.util.ReturnDataUtil;


public interface MachinesTestLogService {


	/**
	 * 获取所有测试数据(分页)
	 * @author hebiting
	 * @date 2019年2月27日下午5:37:26
	 * @param form
	 * @return
	 */
	public ReturnDataUtil getMachinesTest(MachinesTestForm form);

	/**
	 * 获取当前测试记录相关信息
	 * @author hebiting
	 * @date 2019年2月27日上午11:56:38
	 * @param id
	 * @return
	 */
	public ReturnDataUtil geteTestRecord(Long id);
}
