package com.server.module.system.machineManage.machineAccountLibrary;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.server.util.ReturnDataUtil;

/**
 * 
 * author name: why
 * create time: 2018-04-02 11:14:33
 *
 */
@Service
public class VendingMachineAccountLibraryServiceImpl implements VendingMachineAccountLibraryService {

	public static Logger log = LogManager.getLogger(VendingMachineAccountLibraryServiceImpl.class);  
	@Autowired
	private VendingMachineAccountLibraryDao vendingMachineAccountLibraryDaoImpl;
	
	/**
	 * 售货机商品库 查询
	 */
	@Override
	public ReturnDataUtil listPage(VendingMachineAccountLibraryCondition condition) {
		// TODO Auto-generated method stub
		return vendingMachineAccountLibraryDaoImpl.listPage(condition);
	}

}
