package com.server.module.system.statisticsManage.payRecord;

import com.server.module.commonBean.PageAssist;

public class CustomerMachineForm extends PageAssist {

	//商品订单号
	private String ptCode;
	//内部平台订单号
	private String payCode;
	
	private Integer orderType;
	
	public Integer getOrderType() {
		return orderType;
	}

	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}

	public String getPtCode() {
		return ptCode;
	}

	public void setPtCode(String ptCode) {
		this.ptCode = ptCode;
	}

	public String getPayCode() {
		return payCode;
	}

	public void setPayCode(String payCode) {
		this.payCode = payCode;
	}
	
}
