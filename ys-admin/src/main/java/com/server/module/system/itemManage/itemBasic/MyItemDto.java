package com.server.module.system.itemManage.itemBasic;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class MyItemDto {
	private Long basicItemId;
	private String name;
	private String standard;
	private String pic;
	private Integer wayCapacity;
	//a.price,costPrice,a.id as itemId,a.hot,a.endTime
	
	private BigDecimal price;
	private BigDecimal costPrice;
	private Long itemId;
	private Integer hot;
	private Date endTime;

}
