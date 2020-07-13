package com.server.module.system.machineManage.machineInit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.server.module.system.machineManage.machineCode.VendingMachinesCodeServiceImpl;
import com.server.util.ReturnDataUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 
 * author name: why
 * create time: 2018-04-03 09:57:10
 *
 */
@Api(value = "VendingMachineInitController", description = "售货机初始化")
@RestController
@RequestMapping("/VendingMachineInit")
public class VendingMachineInitController {

	public static Logger log = LogManager.getLogger(VendingMachineInitController.class); 	 
	@SuppressWarnings("unused")
	@Autowired
	private VendingMachineInitService vendingMachineInitServiceImpl;
	
	/**
	 *  查询售货机初始化
	 * @param condition
	 * @return
	 */
	@ApiOperation(value = "售货机初始化", notes = "售货机初始化", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@GetMapping(value = "/listPage", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnDataUtil listPage(VendingMachineInitCondition condition) {
		return vendingMachineInitServiceImpl.listPage(condition);
	} 
}
