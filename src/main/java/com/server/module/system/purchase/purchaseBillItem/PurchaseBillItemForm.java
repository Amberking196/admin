package com.server.module.system.purchase.purchaseBillItem;

import com.server.module.commonBean.PageAssist;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * table name:  purchase_bill_item
 * author name: yjr
 * create time: 2018-09-03 16:27:30
 */ 
@Data
public class PurchaseBillItemForm extends PageAssist{
	public Long supplierId;
	public Long itemId;

	// 开始时间
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	Date startTime;
	// 结束时间
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	Date endTime;
}

