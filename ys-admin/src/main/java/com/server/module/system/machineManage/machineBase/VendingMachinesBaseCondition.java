package com.server.module.system.machineManage.machineBase;

import com.server.module.commonBean.PageAssist;

import lombok.Data;
/**
 * table name:  vending_machines_base
 * author name: yjr
 * create time: 2018-03-29 17:51:22
 */ 

@Data
public class VendingMachinesBaseCondition extends PageAssist{
	
	private Integer id;
	private Integer machinesTypeId;
	private String factoryNumber;

	private String simNumber;
	private String simExpireDate;
	private String vmCode;
}

