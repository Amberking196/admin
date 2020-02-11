package com.server.module.customer.userInfo.stock;

import java.math.BigDecimal;
import java.util.Date;

public class OrderDetailBean {

	private Long id;
	private Long orderId;
	private Integer itemId;
	private String itemName;
	private BigDecimal price;
	private Integer num;
	private Date createTime;
	private Long customerId; 
	private Integer pickNum;
	//是否为机器自取  0 非自取  1 自取
	private Integer distributionModel;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	public Integer getItemId() {
		return itemId;
	}
	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	public Integer getPickNum() {
		return pickNum;
	}
	public void setPickNum(Integer pickNum) {
		this.pickNum = pickNum;
	}
	public Integer getDistributionModel() {
		return distributionModel;
	}
	public void setDistributionModel(Integer distributionModel) {
		this.distributionModel = distributionModel;
	}
	
}
