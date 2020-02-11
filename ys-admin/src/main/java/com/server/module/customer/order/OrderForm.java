package com.server.module.customer.order;

import com.server.module.commonBean.PageAssist;

public class OrderForm extends PageAssist{
	//商品名称
	private String product;
	//收货地址
	private String location;
	//配送方式
	private Integer distributionModel;
	//优惠券id
	private Integer coupon;
	//我的订单查询方式
	private Integer findType;
	//拼接用的
	private String ss;
	//支付地址
	private String url;
	//支付方式
	private Integer payType;
	//订单id
	private Integer orderId;
	//用户Id
	private Long customerId;
	//商城订单列表
	private String orderIdList;
	//订单金额
	private Double price;
	//购买数量
	private Integer quantity;
	//商品名称
	private String itemName;
	//参与拼团的id
	private Long customerGroupId;
	//拼团活动的id
	private Long spellGroupId;
	//用户地址id
	private Long addressId;
	///订单类型  1.商城订单  2.机器订单 3.商城订单
	private Integer orderType;
	
	public Long getSpellGroupId() {
		return spellGroupId;
	}
	public void setSpellGroupId(Long spellGroupId) {
		this.spellGroupId = spellGroupId;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public Integer getDistributionModel() {
		return distributionModel;
	}
	public void setDistributionModel(Integer distributionModel) {
		this.distributionModel = distributionModel;
	}
	public Integer getCoupon() {
		return coupon;
	}
	public void setCoupon(Integer coupon) {
		this.coupon = coupon;
	}
	public Integer getFindType() {
		return findType;
	}
	public void setFindType(Integer findType) {
		this.findType = findType;
	}
	public String getSs() {
		return ss;
	}
	public void setSs(String ss) {
		this.ss = ss;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Integer getPayType() {
		return payType;
	}
	public void setPayType(Integer payType) {
		this.payType = payType;
	}
	public Integer getOrderId() {
		return orderId;
	}
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	public Long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	public String getOrderIdList() {
		return orderIdList;
	}
	public void setOrderIdList(String orderIdList) {
		this.orderIdList = orderIdList;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public Long getCustomerGroupId() {
		return customerGroupId;
	}
	public void setCustomerGroupId(Long customerGroupId) {
		this.customerGroupId = customerGroupId;
	}
	public Long getAddressId() {
		return addressId;
	}
	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}
	public Integer getOrderType() {
		return orderType;
	}
	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}
	

}
