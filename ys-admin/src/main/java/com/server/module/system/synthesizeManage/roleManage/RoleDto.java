package com.server.module.system.synthesizeManage.roleManage;

import java.util.List;

import com.server.module.system.companyManage.CompanyBean;

public class RoleDto {

	private CompanyBean company;
	private List<RoleBean> roleList;
	public CompanyBean getCompany() {
		return company;
	}
	public void setCompany(CompanyBean company) {
		this.company = company;
	}
	public List<RoleBean> getRoleList() {
		return roleList;
	}
	public void setRoleList(List<RoleBean> roleList) {
		this.roleList = roleList;
	}
	
} 
