package com.server.module.system.machineManage.machineTemperature;

import com.server.module.commonBean.PageAssist;

import lombok.Data;

@Data
public class MachineTemperatureForm extends PageAssist{

	String vmCode;
	String versionInfo;
}
