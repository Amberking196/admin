package com.server.module.system.purchase.purchaseBill;

import com.server.module.commonBean.PageAssist;

import lombok.Data;
/**
 * table name:  purchase_bill
 * author name: yjr
 * create time: 2018-09-03 16:23:53
 */ 
@Data
public class PurchaseBillForm extends PageAssist{
	
	String number;
	Integer warehouseId;


}

