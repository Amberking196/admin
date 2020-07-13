package com.server.module.system.machineManage.machineReplenish;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.server.util.ReturnDataUtil;
@Service
public class MachineReplenishServiceImpl implements MachineReplenishService{
    
	@Autowired
	MachineReplenishDao machineReplenishDao;
	@Override
	public ReturnDataUtil findMachineReplenishSum(MachineReplenishForm machineReplenishForm) {
		
		return machineReplenishDao.findMachineReplenishSum(machineReplenishForm);
	}
	@Override
	public ReturnDataUtil findMachineReplenishDetile(MachineReplenishForm machineReplenishForm) {
		return machineReplenishDao.findMachineReplenishDetile(machineReplenishForm);
	}

	@Override
	public ReturnDataUtil exportExcel(MachineReplenishForm machineReplenishForm) {
		return machineReplenishDao.exportExcel(machineReplenishForm);
	}

}
