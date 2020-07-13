package com.server.module.system.warehouseManage.checkLog;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ItemInWayNumVo {
	//商品机器内数量
	private Integer basicItemId;
	private String itemName;
	private BigDecimal num;

	private BigDecimal preNum;//上次盘点数量

	//private BigDecimal time;//次数

}
