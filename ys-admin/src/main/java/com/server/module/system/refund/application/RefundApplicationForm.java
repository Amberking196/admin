package com.server.module.system.refund.application;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.server.module.commonBean.PageAssist;

public class RefundApplicationForm extends PageAssist{

	private Integer companyId;//公司id
	private String payCode;//内部订单号
	private String phone;//手机号
	private Integer state;//状态
	private Integer orderType;//订单类型
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date startTime;//开始时间
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date endTime;//结束时间
	
	public Integer getOrderType() {
		return orderType;
	}
	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public Integer getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}
	public String getPayCode() {
		return payCode;
	}
	public void setPayCode(String payCode) {
		this.payCode = payCode;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
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
	
}
