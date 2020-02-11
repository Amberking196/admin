package com.server.module.system.statisticsManage.payRecordPerDay;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.server.common.persistence.NotField;
import com.server.module.commonBean.PageAssist;


public class PayRecordPerDayForm extends PageAssist{

	//公司id
	private Integer companyId;
	//公司及其子公司id
	private String companyIds;
	//起始日期
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date startDate;
	//结束日期
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date endDate;
	
	private String vmCode;

	@NotField
	private Integer areaId;
	@NotField
	private String level;
	
	
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public Integer getAreaId() {
		return areaId;
	}
	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}
	public String getVmCode() {
		return vmCode;
	}
	public void setVmCode(String vmCode) {
		this.vmCode = vmCode;
	}
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
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	
}
