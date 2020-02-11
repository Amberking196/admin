package com.server.module.system.machineManage.distorymachine;

import com.server.util.ReturnDataUtil;

public interface distorymachineService {
	
	/**
	 * 查询掉线的机器
	 * @param distorymachineForm
	 * @return ReturnDataUtil
	 */
	ReturnDataUtil findDistorymachine(distorymachineForm distorymachineForm);

	/**
	 * 查询机器的故障次数
	 * @param perMachineForm
	 * @return ReturnDataUtil
	 */
    ReturnDataUtil perMachineCount(PerMachineForm perMachineForm);
}
