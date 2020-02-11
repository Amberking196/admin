package com.server.module.system.synthesizeManage.roleManage;

import com.server.module.commonBean.PageAssist;

public class RoleForm extends PageAssist{

	private Integer companyId;
	
	private String companyIds;//公司ID的字符集合
	

	public String getCompanyIds() {
		return companyIds;
	}

	public void setCompanyIds(String companyIds) {
		this.companyIds = companyIds;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}
	
}
