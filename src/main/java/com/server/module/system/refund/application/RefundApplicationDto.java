package com.server.module.system.refund.application;

import java.math.BigDecimal;
import java.util.Date;

public class RefundApplicationDto {

	private Long id;
	private Integer orderType;//订单类型(1:机器订单,2:商城订单,3:团购订单)
	private String orderTypeName;//订单类型名称
	private String payCode;//内部订单号
	private String ptCode;//平台订单号
	private String phone ;//手机号
	private String reason;//申请退款原因
	private Integer state;//申请状态:1:待审核，2:退款成功，3:审核不通过
	private String stateName;//申请状态名称
	private BigDecimal refundPrice;//申请退款金额
	private Date createTime;//申请时间
	private Date updateTime;//处理时间
	private Long updateUser;
	private String updateUserName;
	
	public Long getUpdateUser() {
		return updateUser;
	}
	public void setUpdateUser(Long updateUser) {
		this.updateUser = updateUser;
	}
	public String getUpdateUserName() {
		return updateUserName;
	}
	public void setUpdateUserName(String updateUserName) {
		this.updateUserName = updateUserName;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getPtCode() {
		return ptCode;
	}
	public void setPtCode(String ptCode) {
		this.ptCode = ptCode;
	}
	public String getOrderTypeName() {
		return orderTypeName;
	}
	public void setOrderTypeName(String orderTypeName) {
		this.orderTypeName = orderTypeName;
	}
	public String getStateName() {
		return stateName;
	}
	public void setStateName(String stateName) {
		this.stateName = stateName;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getOrderType() {
		return orderType;
	}
	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
		if(orderType != null){
			if(orderType == 1){
				this.orderTypeName ="机器订单";
			}else if (orderType == 2){
				this.orderTypeName = "商城订单";
			}else if (orderType == 3){
				this.orderTypeName = "团购订单";
			}else if (orderType == 6){
				this.orderTypeName = "视觉订单";
			}
		}
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
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
		if(state != null){
			if(state == 1){
				this.stateName ="待审核";
			}else if (state == 2){
				this.stateName = "退款成功";
			}else if (state == 3){
				this.stateName = "审核不通过";
			}
		}
	}
	public BigDecimal getRefundPrice() {
		return refundPrice;
	}
	public void setRefundPrice(BigDecimal refundPrice) {
		this.refundPrice = refundPrice;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
