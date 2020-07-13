package com.server.module.system.machineManage.machineTemperature;

import com.server.common.persistence.NotField;

import lombok.Data;

/**
 * 
 * author name: why create time: 2018-07-06 14:07:20
 */
@Data
public class MachineTemperatureBean {

	private Integer id;
	//售货机基础id
	private Integer baseMachineId;
	//售货机编号
	private String vmCode;
	//出厂编号
	private String factoryNumber;
	//售货机温度
	private String temperaturn;
	//公司名字
	private String companyName;
	//负责人
	private String principal;
	//主控程序版本
	private String mainProgramVersion;
	//是否可在线升级 1：可在线升级 0：不能
	private Integer canOnlineUpdate;
	//机器地址
	private String machinesAddress;
	//机器主板类型
	private String mainboardType;
	private Integer machineType;
	@NotField
	private Integer versionId;


}
