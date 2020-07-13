package com.server.module.system.statisticsManage.notSaleMachines;

import java.util.Date;

public class NotSaleMachinesDto {

	// 自增标识
	private Integer id;
	// 机器编号
	private String vmCode;
	// 起始时间
	private String startDate;
	// 结束时间
	private String endDate;
	// 创建时间
	private String createTime;
	// 公司名称
	private String companyName;
	// 机器位置
	private String location;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getVmCode() {
		return vmCode;
	}
	public void setVmCode(String vmCode) {
		this.vmCode = vmCode;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	
	
	
}
