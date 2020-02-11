package com.server.module.system.machineManage.machineTemperature;

import com.server.util.ReturnDataUtil;

public interface MachineTemperatureDao {

	/**
	 * 查询所有售货机 温度
	 * @return
	 */
	public ReturnDataUtil  listPage(MachineTemperatureForm machineTemperatureForm);

	/**
	 * 更新机器主控版本信息
	 * @author hebiting
	 * @date 2019年1月10日上午10:41:42
	 * @param factoryNum
	 * @param versionInfo
	 * @return
	 */
	public boolean updateMachinesProgramVersion(String factoryNum,String versionInfo);
}
