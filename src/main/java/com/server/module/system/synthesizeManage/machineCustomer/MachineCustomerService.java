package com.server.module.system.synthesizeManage.machineCustomer;

import java.util.List;

import com.server.module.system.synthesizeManage.vendingAreaManage.VendingAreaForm;
import com.server.module.system.userManage.CustomerBean;
import com.server.util.ReturnDataUtil;

public interface MachineCustomerService {

	/**
	 * 根据表单条件查询售货机所有注册用户信息
	 * @return
	 */
	ReturnDataUtil findCustomerByForm(TimeForm timeForm);
	
}
