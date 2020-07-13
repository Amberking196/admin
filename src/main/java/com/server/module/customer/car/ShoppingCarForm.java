package com.server.module.customer.car;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.server.module.commonBean.PageAssist;

import lombok.Data;
/**
 * table name:  shopping_car
 * author name: hjc
 * create time: 2018-06-29 11:03:25
 */ 
@Data
public class ShoppingCarForm extends PageAssist{
	// id
    Long id;
	// ids
    String ids;
	// 顾客id
	Long customerId;
	// 类型 直接下单 1  加入购物车null
	Integer type;
	//开始时间
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	Date startDate;
	// 结束时间
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	Date endDate;
	//团购活动id
	private Long spellgroupId;
}

