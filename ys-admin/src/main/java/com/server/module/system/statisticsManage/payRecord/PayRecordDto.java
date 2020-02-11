package com.server.module.system.statisticsManage.payRecord;

import java.math.BigDecimal;
import java.util.Date;

import com.server.common.persistence.NotField;


public class PayRecordDto {

	// 自增id
	private long id;
	// 客户标识
	private Long customerId;
	// 商品标识
	private Long itemId;
	// 基础商品标识
	private Long basicItemId;
	// 售货机标识
	private String vendingMachinesCode;
	// 支付订单标识
	private String payCode;
	// 支付类型
	private String payType;
	// 支付类型id
	private Integer payTypeId;
	// 价格（元）
	private BigDecimal price;
	// 状态
	private String state;
	// 状体id
	private Integer stateId;
	// 数量
	private int num;
	// 平台返回的订单号
	private String ptCode;
	// 创建时间
	private String createTime;
	// 订单支付时间
	private String payTime;
	// 订单退款时间
	private String refundTime;
	// 订单完成时间
	private String finishTime;
	// 进货价
	private BigDecimal costPrice;
	// 退款原因
	private String refundName;
	// 订单描述
	private String remark;
	// 商品名称
	private String itemName;
	// 商品类型
	private String itemType;
	// 货道
	private Integer wayNumber;
	// 已出货数
	private Integer pickupNum;
	// 公司名称
	private String companyName;
	// 商品类型id
	private Integer itemTypeId;
	// 用户手机号
	private String phone;
	// 公司id
	private Integer companyId;
	// 总价
	private BigDecimal totalPrice;
	// 优惠券
	private String couponIds;
	// 区域id
	private Integer areaId;
	// 区域名称
	private String areaName;
	// 提水券关联id
	private Long orderId;
	// 余额支付金额
	private BigDecimal memberPay;
	// 退款金额
	private BigDecimal refundPrice;
	// 退款商品数量
	private int refundNum;
	//机器地址
	private String locatoinName;

	private Integer isLoginUser;
	
	private String loginUser;

	private BigDecimal realPrice;
	private int realNum;
	
	@NotField
	private Integer orderType;//订单类型 
	
	
	public Integer getOrderType() {
		return orderType;
	}

	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}

	public BigDecimal getRealPrice() {
		return realPrice;
	}

	public void setRealPrice(BigDecimal realPrice) {
		this.realPrice = realPrice;
	}

	public int getRealNum() {
		return realNum;
	}

	public void setRealNum(int realNum) {
		this.realNum = realNum;
	}

	public String getLoginUser() {
		return loginUser;
	}

	public void setLoginUser(String loginUser) {
		this.loginUser = loginUser;
	}

	public Integer getIsLoginUser() {
		return isLoginUser;
	}

	public void setIsLoginUser(Integer isLoginUser) {
		this.isLoginUser = isLoginUser;
	}

	public BigDecimal getMemberPay() {
		return memberPay;
	}

	public void setMemberPay(BigDecimal memberPay) {
		this.memberPay = memberPay;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public Integer getAreaId() {
		return areaId;
	}

	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}

	public String getCouponIds() {
		return couponIds;
	}

	public void setCouponIds(String couponIds) {
		this.couponIds = couponIds;
	}

	public Integer getStateId() {
		return stateId;
	}

	public void setStateId(Integer stateId) {
		this.stateId = stateId;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public Integer getPayTypeId() {
		return payTypeId;
	}

	public void setPayTypeId(Integer payTypeId) {
		this.payTypeId = payTypeId;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public Integer getItemTypeId() {
		return itemTypeId;
	}

	public void setItemTypeId(Integer itemTypeId) {
		this.itemTypeId = itemTypeId;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public Long getBasicItemId() {
		return basicItemId;
	}

	public void setBasicItemId(Long basicItemId) {
		this.basicItemId = basicItemId;
	}

	public String getVendingMachinesCode() {
		return vendingMachinesCode;
	}

	public void setVendingMachinesCode(String vendingMachinesCode) {
		this.vendingMachinesCode = vendingMachinesCode;
	}

	public String getPayCode() {
		return payCode;
	}

	public void setPayCode(String payCode) {
		this.payCode = payCode;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public String getPtCode() {
		return ptCode;
	}

	public void setPtCode(String ptCode) {
		this.ptCode = ptCode;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getPayTime() {
		return payTime;
	}

	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}

	public String getRefundTime() {
		return refundTime;
	}

	public void setRefundTime(String refundTime) {
		this.refundTime = refundTime;
	}

	public String getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(String finishTime) {
		this.finishTime = finishTime;
	}

	public BigDecimal getCostPrice() {
		return costPrice;
	}

	public void setCostPrice(BigDecimal costPrice) {
		this.costPrice = costPrice;
	}

	public String getRefundName() {
		return refundName;
	}

	public void setRefundName(String refundName) {
		this.refundName = refundName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public Integer getWayNumber() {
		return wayNumber;
	}

	public void setWayNumber(Integer wayNumber) {
		this.wayNumber = wayNumber;
	}

	public Integer getPickupNum() {
		return pickupNum;
	}

	public void setPickupNum(Integer pickupNum) {
		this.pickupNum = pickupNum;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public BigDecimal getRefundPrice() {
		return refundPrice;
	}

	public void setRefundPrice(BigDecimal refundPrice) {
		this.refundPrice = refundPrice;
	}

	public int getRefundNum() {
		return refundNum;
	}

	public void setRefundNum(int refundNum) {
		this.refundNum = refundNum;
	}

	public String getLocatoinName() {
		return locatoinName;
	}

	public void setLocatoinName(String locatoinName) {
		this.locatoinName = locatoinName;
	}

	
}
