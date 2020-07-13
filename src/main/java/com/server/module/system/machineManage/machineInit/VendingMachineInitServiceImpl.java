package com.server.module.system.machineManage.machineInit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.server.util.ReturnDataUtil;
/**
 * 
 * author name: why
 * create time: 2018-04-03 09:56:18
 *
 */
@Service
public class VendingMachineInitServiceImpl implements VendingMachineInitService {

	public static Logger log = LogManager.getLogger(VendingMachineInitServiceImpl.class);   
	@Autowired
	private VendingMachineInitDao VendingMachineInitDaoImpl;
	
	/**
	 * 查询售货机初始化
	 */
	@Override
	public ReturnDataUtil listPage(VendingMachineInitCondition condition) {
		// TODO Auto-generated method stub
		return VendingMachineInitDaoImpl.listPage(condition);
	}

}
