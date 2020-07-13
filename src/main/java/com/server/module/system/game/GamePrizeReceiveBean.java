package com.server.module.system.game;

import java.util.Date;

public class GamePrizeReceiveBean {

	private Long id;
	private Long gamePrizeId;
	private Long customerId;
	private Date createTime;
	private Date endTime;
	private Date receiveTime;
	private Long addressId;
	
	public Long getAddressId() {
		return addressId;
	}
	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getGamePrizeId() {
		return gamePrizeId;
	}
	public void setGamePrizeId(Long gamePrizeId) {
		this.gamePrizeId = gamePrizeId;
	}
	public Long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public Date getReceiveTime() {
		return receiveTime;
	}
	public void setReceiveTime(Date receiveTime) {
		this.receiveTime = receiveTime;
	}
	
	
}
