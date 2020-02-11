package com.server.module.system.replenishManage.machinesReplenishManage;

import lombok.Data;

/**
 * 
 * author name: why
 * create time: 2018-04-23 16:19:47
 */ 
@Data
public class ReplenishmentDetailsBean {

	//货道号
	private Integer wayNumber;
	//商品名称
	private String itemName;
	//可容纳数量
	private Integer acceptableQuantity;
	//当前货物量
	private Integer currentQuantity;
	//建议补货数
	private Integer suggestedReplenishment;
	//线路名
	private String lineName;
	//商品条形码
	private String barCode;
	//商品单位
	private String unitName;
	//商品id
	private Long itemId;
}
