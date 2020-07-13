package com.server.module.system.refund;

import java.util.Date;

import com.server.common.persistence.NotField;

public class RefundRecordBean {

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
	private String itemName;//退款商品名称
	
	private String createUserName;//退款操作人名称
	private String stateName;//退款状态
	private String refundPlatformName;//退款平台
	private String typeName;//订单产生类型
	private Integer refundNum;//退款数量
	private Date payTime;//退款数量
	@NotField
	private String remark;
	
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Date getPayTime() {
		return payTime;
	}
	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getStateName() {
		return stateName;
	}
	public void setStateName(String stateName) {
		this.stateName = stateName;
	}
	public String getRefundPlatformName() {
		return refundPlatformName;
	}
	public void setRefundPlatformName(String refundPlatformName) {
		this.refundPlatformName = refundPlatformName;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getCreateUserName() {
		return createUserName;
	}
	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}
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
		//退款状态(1:成功;2:失败)
		this.state = state;
		if(state != null){
			if(state == 1){
				this.stateName = "成功";
			} else if(state == 2){
				this.stateName = "失败";
			} else {
				this.stateName = "未知";
			}
		}
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		//1:机器订单退款;2:商城普通订单退款;3:团购订单退款
		this.type = type;
		if(type != null){
			if(type == 1){
				this.typeName = "机器订单退款";
			}else if(type == 2){
				this.typeName = "商城普通订单退款";
			}else if(type == 3){
				this.typeName = "团购订单退款";
			}else if(type == 4){
				this.typeName = "余额充值订单退款";
			}else if(type == 6){
				this.typeName = "视觉机器订单退款";
			}else {
				this.typeName = "未知";
			}
		}
	}
	public Integer getRefundPlatform() {
		return refundPlatform;
	}
	public void setRefundPlatform(Integer refundPlatform) {
		//退款平台1:微信2：支付宝
		this.refundPlatform = refundPlatform;
		if(refundPlatform != null){
			if(refundPlatform == 1){
				this.refundPlatformName = "微信";
			} else if (refundPlatform == 2) {
				this.refundPlatformName = "支付宝";
			} else {
				this.refundPlatformName = "未知";
			}
		}
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
	public Integer getRefundNum() {
		return refundNum;
	}
	public void setRefundNum(Integer refundNum) {
		this.refundNum = refundNum;
	}
	
	
}
