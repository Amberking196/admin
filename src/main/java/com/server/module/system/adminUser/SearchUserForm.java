package com.server.module.system.adminUser;

import com.server.module.commonBean.PageAssist;

public class SearchUserForm extends PageAssist{

	private String name;
	private Integer companyId;
	private Integer departMent;
	private String companyIds;
	public String getCompanyIds() {
		return companyIds;
	}
	public void setCompanyIds(String companyIds) {
		this.companyIds = companyIds;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}
	public Integer getDepartMent() {
		return departMent;
	}
	public void setDepartMent(Integer departMent) {
		this.departMent = departMent;
	}
	
}
