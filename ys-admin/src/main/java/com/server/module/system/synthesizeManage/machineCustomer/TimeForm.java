package com.server.module.system.synthesizeManage.machineCustomer;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.server.module.commonBean.PageAssist;

public class TimeForm extends PageAssist{
	
    private String id;   //售货机编号
    @DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date startDate;  //查询起始时间
    @DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date endDate; //查询结束时间
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
