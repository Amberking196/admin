package com.server.module.system.synthesizeManage.vendingAreaManage;

import com.server.module.commonBean.PageAssist;

public class VendingAreaForm extends PageAssist{

	private String areaName;
	private Integer companyId;
	private String companyIds;
	private Integer isUsing;
	
	public Integer getIsUsing() {
		return isUsing;
	}
	public void setIsUsing(Integer isUsing) {
		this.isUsing = isUsing;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public Integer getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}
	public String getCompanyIds() {
		return companyIds;
	}
	public void setCompanyIds(String companyIds) {
		this.companyIds = companyIds;
	}
	
	
	
}
