package com.server.module.system.statisticsManage.merchandiseSalesStatistics;

import lombok.Data;
/**
 * author name: why
 * create time: 2018-05-09 21:15:27
 */ 
@Data
public class MerchandiseSalesStatisticsBean{
	
	//序号
	private Integer id;
	//商品编号
	private Integer basicItemId;
	//商品名称
	private String itemName;
	//条形码
	private String barCode;
	//机器编码
	private String vmCode;
	//销售数量
	private Integer salesQuantity;
	//平均售价
	private double avgPrice;
	//总价
	private double allPrice;
	//日期
	private String time;
}

