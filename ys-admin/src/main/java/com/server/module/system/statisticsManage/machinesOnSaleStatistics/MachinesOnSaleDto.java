package com.server.module.system.statisticsManage.machinesOnSaleStatistics;

import java.util.Date;

import com.server.common.persistence.NotField;

import lombok.Data;
@Data
public class MachinesOnSaleDto {

	//商品名称
	private String goodsName;
	
	//商品数量
	private Integer num;
	
	//商品id
	private Integer basicItemId;
	
	//公司id
	private Integer companyId;
	
	//售货机编码
	private String vmCode;
	
}
