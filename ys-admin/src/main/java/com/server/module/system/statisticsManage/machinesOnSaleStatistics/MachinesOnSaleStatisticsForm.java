package com.server.module.system.statisticsManage.machinesOnSaleStatistics;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.server.module.commonBean.PageAssist;

import lombok.Data;

@Data
public class MachinesOnSaleStatisticsForm extends PageAssist{


	//公司id
	private Integer companyId;
	
	private String companyIds;
	//公司id
	private Integer areaId;
	//商品名称
	private String goodsName;
	
	//商品名称
	private Integer basicItemId;
	
	private String vmCode;
	
	
}
