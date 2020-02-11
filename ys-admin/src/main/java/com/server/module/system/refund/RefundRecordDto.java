package com.server.module.system.refund;

import java.util.Date;

public class RefundRecordDto {

	private Long id;//退款记录id
	private Integer state;//退款状态(1:成功;2:失败)
	private Integer type;//订单产生类型(1:机器订单退款;2:商城普通订单退款;3:团购订单退款)
	private Integer refundPlatform;//退款平台1:微信2：支付宝
	private String outRefundNo;//系统内部退款编码
	private String platformNo;//平台退款编码，支付宝无
	private String payCode;//系统支付编码
	private String ptCode;//平台支付编码
	private Double price;//订单总金额
	private Double refundPrice;//退款金额
	private String reason;//退款原因
	private Long createUser;//退款操作人
	private Date createTime;//退款记录时间
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getRefundPlatform() {
		return refundPlatform;
	}
	public void setRefundPlatform(Integer refundPlatform) {
		this.refundPlatform = refundPlatform;
	}
	public String getOutRefundNo() {
		return outRefundNo;
	}
	public void setOutRefundNo(String outRefundNo) {
		this.outRefundNo = outRefundNo;
	}
	public String getPlatformNo() {
		return platformNo;
	}
	public void setPlatformNo(String platformNo) {
		this.platformNo = platformNo;
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
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public Double getRefundPrice() {
		return refundPrice;
	}
	public void setRefundPrice(Double refundPrice) {
		this.refundPrice = refundPrice;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public Long getCreateUser() {
		return createUser;
	}
	public void setCreateUser(Long createUser) {
		this.createUser = createUser;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	
}
