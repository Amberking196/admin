package com.server.module.system.refund.principal;

import com.server.module.commonBean.PageAssist;

public class RefundPrincipalForm extends PageAssist{

	private String phone;
	private Integer companyId;
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public Integer getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}
	
	
}
