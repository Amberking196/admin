package com.server.module.customer.order;

import java.util.Date;

public class OrderDetile {
    //自增标识
	private Long id;
	//订单id
	private Long orderId;
	//商品Id
	private Integer itemId;
	//商品名称
	private String itenName;
	//商品价格
	private Double price;
	//商品数量
	private Integer num;
	//创建时间
	private Date createTime;
	//客户id
	private Long customerId;
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
	public String getItenName() {
		return itenName;
	}
	public void setItenName(String itenName) {
		this.itenName = itenName;
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
	public Long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	
}
