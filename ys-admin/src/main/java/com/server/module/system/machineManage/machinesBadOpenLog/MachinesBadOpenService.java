package com.server.module.system.machineManage.machinesBadOpenLog;

public interface MachinesBadOpenService {


	/**
	 * 根据手机号查询未关门机器
	 * @author hebiting
	 * @date 2019年3月19日下午6:07:26
	 * @param phone
	 * @return
	 */
	public BadOpenLogBean getByPhone(String phone);
}
