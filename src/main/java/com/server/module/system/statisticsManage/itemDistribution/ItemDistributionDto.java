package com.server.module.system.statisticsManage.itemDistribution;

import java.math.BigDecimal;

public class ItemDistributionDto {

	//商品名称
	private String itemName;
	//机器编码
	private String vmCode;
	//机器位置
	private String address;
	//商品余量
	private Integer itemNum;
	//最大容量
	private Integer fullNum;
	//商品当前价格
	private Double price;
	//月销售数量
	private Integer monthNum;
	//日均销量
	private BigDecimal dayNum;
	//紧迫时间（天）
	private BigDecimal day;
	
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getVmCode() {
		return vmCode;
	}
	public void setVmCode(String vmCode) {
		this.vmCode = vmCode;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Integer getItemNum() {
		return itemNum;
	}
	public void setItemNum(Integer itemNum) {
		this.itemNum = itemNum;
	}
	public Integer getFullNum() {
		return fullNum;
	}
	public void setFullNum(Integer fullNum) {
		this.fullNum = fullNum;
	}
	public Integer getMonthNum() {
		return monthNum;
	}
	public void setMonthNum(Integer monthNum) {
		this.monthNum = monthNum;
	}
	public BigDecimal getDayNum() {
		return dayNum;
	}
	public void setDayNum(BigDecimal dayNum) {
		this.dayNum = dayNum;
	}
	public BigDecimal getDay() {
		return day;
	}
	public void setDay(BigDecimal day) {
		this.day = day;
	}
	
}
