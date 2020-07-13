package com.server.module.system.replenishManage.machinesReplenishManage;

import com.server.module.commonBean.PageAssist;

import lombok.Data;

/**
 * author name: why create time: 2018-04-23 16:19:47
 */
@Data
public class MachinesReplenishCondition extends PageAssist {

	Integer companyId; // 所属公司ID
	Integer areaId; // 所属地区ID
	Integer lineId; // 线路ID
	String vmCode;
	Integer otherCompanyId;//补水公司id
	int version=1;//  1 版本  2 版本

	Integer rate;// 缺货占比

	// 要打印记录的可以勾选多个
	String[] codes;//机器编码

	String address;

}
