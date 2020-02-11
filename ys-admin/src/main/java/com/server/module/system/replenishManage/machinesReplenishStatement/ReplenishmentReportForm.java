package com.server.module.system.replenishManage.machinesReplenishStatement;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.server.module.commonBean.PageAssist;

import lombok.Data;
/**
 * author name: why
 * create time: 2018-04-24 11:53:01
 */ 
@Data
public class ReplenishmentReportForm extends PageAssist{

	Integer companyId; //公司编号
	Integer areaId;  //区域编号
	Integer  lineId; //线路编号
	String  code; //编号
	String operator;//负责人
	Integer  userId; //补水人员编号
	@DateTimeFormat(pattern = "yyyy-MM-dd") //开始时间
	Date startDate;
	@DateTimeFormat(pattern = "yyyy-MM-dd") //结束时间
	Date endDate;
	
	Integer orderType;
}

