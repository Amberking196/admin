package com.server.module.system.adminUser;

import java.util.List;

import com.server.module.system.companyManage.CompanyBean;
import com.server.module.system.synthesizeManage.roleManage.RoleBean;

public class CompanyRoleDto extends CompanyBean{

	private List<RoleBean> roleList;
	private List<CompanyBean> companyList;
	
	public List<CompanyBean> getCompanyList() {
		return companyList;
	}

	public void setCompanyList(List<CompanyBean> companyList) {
		this.companyList = companyList;
	}

	public List<RoleBean> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<RoleBean> roleList) {
		this.roleList = roleList;
	}

	public void setComapanyValue(CompanyBean company){
		setAreaId(company.getAreaId());
		setCreateTime(company.getCreateTime());
		setId(company.getId());
		setLocation(company.getLocation());
		setLogoPic(company.getLogoPic());
		setMail(company.getMail());
		setName(company.getName());
		setParentId(company.getParentId());
		setPhone(company.getPhone());
		setPrincipal(company.getPrincipal());
		setShortName(company.getShortName());
		setState(company.getState());
	}
	
}
