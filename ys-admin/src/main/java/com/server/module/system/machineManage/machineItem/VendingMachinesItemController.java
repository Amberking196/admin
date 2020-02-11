package com.server.module.system.machineManage.machineItem;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import com.server.module.system.machineManage.machineInit.VendingMachineInitServiceImpl;
import com.server.util.ReturnDataUtil;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * author name: yjr create time: 2018-04-13 08:57:26
 */
@Api(value = "VendingMachinesItemController", description = "商品价格库")
@RestController
@RequestMapping("/vendingMachinesItem")
public class VendingMachinesItemController {

	public static Logger log = LogManager.getLogger(VendingMachinesItemController.class); 
	@Autowired
	private VendingMachinesItemService vendingMachinesItemServiceImpl;

	/*@ApiOperation(value = "商品价格库列表", notes = "listPage", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@GetMapping(value = "/listPage", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil listPage(VendingMachinesItemCondition condition) {
		return vendingMachinesItemServiceImpl.listPage(condition);
	}

	@ApiOperation(value = "商品价格库添加", notes = "add", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/add", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil add(@RequestBody VendingMachinesItemBean entity) {
		return new ReturnDataUtil(vendingMachinesItemServiceImpl.add(entity));
	}

	@ApiOperation(value = "商品价格库修改", notes = "update", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/update", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil update(@RequestBody VendingMachinesItemBean entity) {
		return new ReturnDataUtil(vendingMachinesItemServiceImpl.update(entity));
	}

	@ApiOperation(value = "商品价格库删除", notes = "del", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/del", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil del(Object id) {
		return new ReturnDataUtil(vendingMachinesItemServiceImpl.del(id));
	}*/
}
