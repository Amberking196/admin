package com.server.module.system.machineManage.machineAccountLibrary;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.server.module.system.adminUser.AdminUserServiceImp;
import com.server.module.system.machineManage.machineType.MachinesTypeCondition;
import com.server.util.ReturnDataUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 
 * author name: why
 * create time: 2018-04-02 11:17:23
 *
 */
@Api(value = "VendingMachineAccountLibraryController", description = "售货机商品库")
@RestController
@RequestMapping("/VendingMachineAccountLibrary")
public class VendingMachineAccountLibraryController {

	public static Logger log = LogManager.getLogger(VendingMachineAccountLibraryController.class);  
	@Autowired
	private VendingMachineAccountLibraryService vendingMachineAccountLibraryServiceImpl;

	@ApiOperation(value = "售货机商品库列表", notes = "售货机商品库列表", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@RequestMapping(value="/listPage",produces = "application/json;charset=UTF-8")
	public ReturnDataUtil listPage(VendingMachineAccountLibraryCondition condition){
		return vendingMachineAccountLibraryServiceImpl.listPage(condition);
	}
}
