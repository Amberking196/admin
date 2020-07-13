package com.server.module.system.refund;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.server.module.commonBean.PageAssist;

public class RefundRecordForm extends PageAssist{

	private String payCode;
	private String ptCode;
	private Integer state;
	private Integer type;
	private Integer refundPlatform;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date startTime;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date endTime;
	private Integer companyId;
	
	
	public Integer getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}
	public Integer getRefundPlatform() {
		return refundPlatform;
	}
	public void setRefundPlatform(Integer refundPlatform) {
		this.refundPlatform = refundPlatform;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getPayCode() {
		return payCode;
	}
	public void setPayCode(String payCode) {
		this.payCode = payCode;
	}
	public String getPtCode() {
		return ptCode;
	}
	public void setPtCode(String ptCode) {
		this.ptCode = ptCode;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
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
