package com.server.module.system.machineManage.machineCode;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
 * author name: why
 * create time: 2018-04-10 15:07:28
 */ 
@Service
public class  VendingMachinesCodeServiceImpl  implements VendingMachinesCodeService{


	public static Logger log = LogManager.getLogger(VendingMachinesCodeServiceImpl.class);	
	@Autowired
	private VendingMachinesCodeDao VendingMachinesCodeDaoImpl;
	
	@Override
	public VendingMachinesCodeBean getByUnique(String areaNumber, Long machinesTypeId) {
		// TODO Auto-generated method stub
		return VendingMachinesCodeDaoImpl.getByUnique(areaNumber, machinesTypeId);
	}

	@Override
	public VendingMachinesCodeBean insert(VendingMachinesCodeBean bean) {
		// TODO Auto-generated method stub
		return VendingMachinesCodeDaoImpl.insert(bean);
	}

	@Override
	public boolean update(VendingMachinesCodeBean bean) {
		// TODO Auto-generated method stub
		return VendingMachinesCodeDaoImpl.update(bean);
	}

	@Override
	public String getFactoryNumByVmCode(String vmCode) {
		
		return VendingMachinesCodeDaoImpl.getFactoryNumByVmCode(vmCode);
	}
	
	
}

