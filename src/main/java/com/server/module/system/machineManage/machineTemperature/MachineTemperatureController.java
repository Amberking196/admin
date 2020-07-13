package com.server.module.system.machineManage.machineTemperature;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.discovery.converters.Auto;
import com.server.util.ReturnDataUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "MachineTemperatureController", description = "售货机温度")
@RestController
@RequestMapping("/machineTemperature")
public class MachineTemperatureController {
	
	public static Logger log=LogManager.getLogger(MachineTemperatureController.class);
	
	@Autowired
	private ReturnDataUtil returnDataUtil; 
	
	@Autowired 
	private MachineTemperatureService machineTemperatureServiceImpl;
	
	
	@ApiOperation(value = "机器温度列表", notes = "listPage", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/listPage", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil listPage(@RequestBody(required=false) MachineTemperatureForm machineTemperatureForm) {
		log.info("<MachineTemperatureController>----<listPage>----start");
		if(machineTemperatureForm==null) {
			machineTemperatureForm=new MachineTemperatureForm();
		}
		returnDataUtil = machineTemperatureServiceImpl.listPage(machineTemperatureForm);
		log.info("<MachineTemperatureController>----<listPage>----end");
		return returnDataUtil;
	}

}
