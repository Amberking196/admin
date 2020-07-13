package com.server.module.system.machineManage.machineReplenish;

import com.server.util.ReturnDataUtil;


public interface MachineReplenishService {
	/**
	 * 查询机器补货信息
	 * @param machineReplenishForm
	 * @return ReturnDataUtil
	 */

	ReturnDataUtil findMachineReplenishSum(MachineReplenishForm machineReplenishForm);


	/**
	 * 查询机器补货详情
	 *
	 * @param machineReplenishForm
	 * @return ReturnDataUtil
	 */
	ReturnDataUtil findMachineReplenishDetile(MachineReplenishForm machineReplenishForm);

	/**
	 * 将数据全部输出到Excel中
	 *
	 * @param machineReplenishForm
	 * @return
	 */
	ReturnDataUtil exportExcel(MachineReplenishForm machineReplenishForm);
}
