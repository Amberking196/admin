package com.server.module.system.machineManage.machinesAdvertising;

import com.server.module.commonBean.PageAssist;

import lombok.Data;

/**
 * table name: vending_machines_advertising author name: why create time:
 * 2018-11-02 10:38:21
 */
@Data
public class VendingMachinesAdvertisingForm extends PageAssist {

	//公司id
	private Long companyId;
	//售货机编号
	private String vmCode;
}
