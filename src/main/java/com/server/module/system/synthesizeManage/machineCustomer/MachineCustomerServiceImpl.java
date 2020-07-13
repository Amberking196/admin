package com.server.module.system.synthesizeManage.machineCustomer;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.server.module.system.synthesizeManage.roleManage.RoleBean;
import com.server.module.system.synthesizeManage.roleManage.RoleManageDao;
import com.server.module.system.synthesizeManage.vendingAreaManage.VendingAreaDto;
import com.server.module.system.userManage.CustomerBean;
import com.server.util.ReturnDataUtil;

@Service
public class MachineCustomerServiceImpl implements MachineCustomerService{

	@Autowired
	MachineCustomerDao machineCustomerDao;
	
	@Override
	public ReturnDataUtil findCustomerByForm(TimeForm timeForm) {
		ReturnDataUtil returnData=  machineCustomerDao.findCustomerByForm(timeForm);
		return returnData;
	}
	
}
