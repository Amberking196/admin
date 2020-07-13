package com.server.module.system.purchase.purchaseApply;

import com.server.module.commonBean.PageAssist;
/**
 * table name:  purchase_apply_bill
 * author name: yjr
 * create time: 2018-08-31 17:27:57
 */ 

public class PurchaseApplyBillCondition extends PageAssist{
	private String warehouseName;
	private Integer state;
	public String getWarehouseName() {
		return warehouseName;
	}
	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	
}

