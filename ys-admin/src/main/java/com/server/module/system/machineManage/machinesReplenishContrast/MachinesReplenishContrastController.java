package com.server.module.system.machineManage.machinesReplenishContrast;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.server.util.ReturnDataUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 
 * @author why
 * @date: 2019年5月14日 下午5:47:34
 */
@Api(value = "MachinesReplenishContrastController", description = "销售补货预警管理")
@RestController
@RequestMapping("/machinesReplenishContrast")
public class MachinesReplenishContrastController {

	private static Logger log=LogManager.getLogger(MachinesReplenishContrastController.class);
	@Autowired
	private MachinesReplenishContrastService machinesReplenishContrastServiceImpl;

	@ApiOperation(value = "销售补货预警管理列表", notes = "listPage", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/listPage", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil listPage(@RequestBody(required=false) MachinesReplenishContrastForm machinesReplenishContrastForm) {
		log.info("<MachinesReplenishContrastController>----------<listPage>--------start");
		if(machinesReplenishContrastForm==null) {
			machinesReplenishContrastForm=new MachinesReplenishContrastForm();
		}
		ReturnDataUtil listPage = machinesReplenishContrastServiceImpl.listPage(machinesReplenishContrastForm);
		log.info("<MachinesReplenishContrastController>----------<listPage>--------end");
		return listPage;
	}

	@ApiOperation(value = "销售补货预警管理添加", notes = "add", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/add", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil add(@RequestBody MachinesReplenishContrastBean entity) {
		return new ReturnDataUtil(machinesReplenishContrastServiceImpl.add(entity));
	}

	@ApiOperation(value = "销售补货预警管理修改", notes = "update", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/update", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil update(@RequestBody MachinesReplenishContrastBean entity) {
		return new ReturnDataUtil(machinesReplenishContrastServiceImpl.update(entity));
	}

	@ApiOperation(value = "销售补货预警管理删除", notes = "del", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/del", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil del(Object id) {
		return new ReturnDataUtil(machinesReplenishContrastServiceImpl.del(id));
	}
}
