package com.server.module.system.statisticsManage.userActiveDegree;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.server.module.commonBean.PageAssist;

public class UserActiveDegreeForm extends PageAssist{

	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date startTime;
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date endTime;
	private String vmCode;
	private Integer companyId;
	private Integer areaId;
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public String getVmCode() {
		return vmCode;
	}
	public void setVmCode(String vmCode) {
		this.vmCode = vmCode;
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
	
	
}
