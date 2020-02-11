package com.server.module.system.statisticsManage.itemDistribution;

import com.server.module.commonBean.PageAssist;

public class ItemDistributionForm extends PageAssist{

	private String itemName;
	private Long basicItemId;
	private Integer companyId;
	private Integer areaId;
	private Integer lineId;
	private String vmCode;
	private Integer version;
	private Double daySaleStart;
	private Double daySaleEnd;

	public Double getDaySaleStart() {
		return daySaleStart;
	}
	public void setDaySaleStart(Double daySaleStart) {
		this.daySaleStart = daySaleStart;
	}
	public Double getDaySaleEnd() {
		return daySaleEnd;
	}
	public void setDaySaleEnd(Double daySaleEnd) {
		this.daySaleEnd = daySaleEnd;
	}
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public Long getBasicItemId() {
		return basicItemId;
	}
	public void setBasicItemId(Long basicItemId) {
		this.basicItemId = basicItemId;
	}
	public Integer getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}
	public Integer getAreaId() {
		return areaId;
	}
	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}
	public Integer getLineId() {
		return lineId;
	}
	public void setLineId(Integer lineId) {
		this.lineId = lineId;
	}
	public String getVmCode() {
		return vmCode;
	}
	public void setVmCode(String vmCode) {
		this.vmCode = vmCode;
	}
	
}
