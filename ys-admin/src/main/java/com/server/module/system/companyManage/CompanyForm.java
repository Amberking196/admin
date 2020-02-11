package com.server.module.system.companyManage;

import com.server.module.commonBean.PageAssist;

public class CompanyForm extends PageAssist{

	private String companyName;
	
	private String companyIds;
	
	private Integer state;
	//公司id	
	private Integer companyId;
	//除去本公司id
	private Integer thisCompanyId;
	//父公司id
	private Integer parentId;
	
	private Integer otherType;//第三方补水公司     3
	
	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getCompanyIds() {
		return companyIds;
	}

	public void setCompanyIds(String companyIds) {
		this.companyIds = companyIds;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public Integer getThisCompanyId() {
		return thisCompanyId;
	}

	public void setThisCompanyId(Integer thisCompanyId) {
		this.thisCompanyId = thisCompanyId;
	}

	public Integer getOtherType() {
		return otherType;
	}

	public void setOtherType(Integer otherType) {
		this.otherType = otherType;
	}
	
	
}
