package com.server.module.system.statisticsManage.payRecord;

import java.util.Date;

import com.server.common.persistence.NotField;

public class PayRecordItemBean {

	private Long id;
	private Long payRecordId;
	private Long itemId;
	private Long basicItemId;
	private String itemName;
	private Long itemType;
	private Double costPrice;
	private Double price;
	private Integer num;
	private Date createTime;
	private Double realTotalPrice;
	
	@NotField
	private Double totalPrice;
	
	public Double getRealTotalPrice() {
		return realTotalPrice;
	}
	public void setRealTotalPrice(Double realTotalPrice) {
		this.realTotalPrice = realTotalPrice;
	}
	public Double getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getPayRecordId() {
		return payRecordId;
	}
	public void setPayRecordId(Long payRecordId) {
		this.payRecordId = payRecordId;
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
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public Long getItemType() {
		return itemType;
	}
	public void setItemType(Long itemType) {
		this.itemType = itemType;
	}
	public Double getCostPrice() {
		return costPrice;
	}
	public void setCostPrice(Double costPrice) {
		this.costPrice = costPrice;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
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
	
	
}
