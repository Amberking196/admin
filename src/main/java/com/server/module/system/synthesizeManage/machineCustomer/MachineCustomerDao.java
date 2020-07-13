package com.server.module.system.synthesizeManage.machineCustomer;

import java.util.Date;
import java.util.List;

import com.server.module.system.userManage.CustomerBean;
import com.server.util.ReturnDataUtil;

public interface MachineCustomerDao {

	
	/**
	 * 根据条件查询在售货机上注册的用户信息
	 * @param timeForm
	 * @return ReturnDataUtil
	 */
	ReturnDataUtil findCustomerByForm(TimeForm timeForm);
}
