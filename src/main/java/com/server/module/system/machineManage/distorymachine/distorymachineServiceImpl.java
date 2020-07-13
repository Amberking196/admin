package com.server.module.system.machineManage.distorymachine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.server.module.system.userManage.CustomerDao;
import com.server.util.ReturnDataUtil;
@Service("distorymachineService")
public class distorymachineServiceImpl implements distorymachineService {
	
	@Autowired
	distorymachineDao distorymachineDao;

	@Override
	public ReturnDataUtil findDistorymachine(distorymachineForm distorymachineForm) {
		
		return distorymachineDao.findDistorymachine(distorymachineForm);
	}

	@Override
	public ReturnDataUtil perMachineCount(PerMachineForm perMachineForm) {
		return distorymachineDao.perMachineCount(perMachineForm);
	}
}
