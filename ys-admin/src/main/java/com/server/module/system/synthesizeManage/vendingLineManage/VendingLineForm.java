package com.server.module.system.synthesizeManage.vendingLineManage;

import com.server.module.commonBean.PageAssist;

public class VendingLineForm extends PageAssist{

	//线路名称
	private String lineName;
	//区域id
	private String areaId;
	//负责人名称
	private String dutyName;
	//公司id
	private Integer companyId;
	//公司及其子公司id
	private String companyIds;

	// 区域名称
	private String areaName;
	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getLineName() {
		return lineName;
	}
	public void setLineName(String lineName) {
		this.lineName = lineName;
	}
	public String getAreaId() {
		return areaId;
	}
	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}
	public String getDutyName() {
		return dutyName;
	}
	public void setDutyName(String dutyName) {
		this.dutyName = dutyName;
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
