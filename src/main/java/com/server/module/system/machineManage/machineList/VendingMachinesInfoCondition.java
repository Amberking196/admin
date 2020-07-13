package com.server.module.system.machineManage.machineList;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.server.module.commonBean.PageAssist;

import lombok.Data;

/**
 * table name: vending_machines_info author name: yjr create time: 2018-03-28
 * 10:02:33
 */
@Data
public class VendingMachinesInfoCondition extends PageAssist {

	String code; // 售货机编号
	Integer companyId; // 公司标识
	Integer state; // 状态
	Integer lineId;//线路Id
	Integer areaId;//区域Id
	String  factoryNumber;//出厂编号
	String  locatoinName;//地址

	Integer district;
	Integer city;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	Date startDate;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	Date endDate;
	
	//刷选半径为1km的售货机
	double minlat ;
	double maxlat ;
	double minlon ;
	double maxlon ;
	
	Integer machineVersion;//机器版本
	String mainProgramVersion;//主控版本
}
