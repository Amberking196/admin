package com.server.module.system.machineManage.distorymachine;

import java.util.List;

import com.server.util.ReturnDataUtil;

public interface distorymachineDao {
	
	/**
	 * 查询掉线机器
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
    
	/**
	 * 查询机器的故障信息
	 * @param codes
	 * @return List<distorymachineBean>
	 */
    List<distorymachineBean> listDistoryBean(List<String> codes);
}
