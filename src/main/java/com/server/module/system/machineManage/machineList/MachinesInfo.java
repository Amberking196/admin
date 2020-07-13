package com.server.module.system.machineManage.machineList;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public class MachinesInfo {

	//机器返回参数
	private String params;
	//出厂编号
	private String factoryNumber;
	//机器编码
	private String vmCode;
	//机器状态
	//	  0：机器空闲  
	//    1：正在打开门
	//    2：门已完全打开
	//    3：门已关上
	//	  4: 打开失败
	//	  5: 称重故障
	//	  6: 补货结束
	private String state;
	//供电状态 1:外接，0：备用 当机器为0使用备用电源时要报警
	private String power;
	//温度
	private String temperature;
	//机器版本
	private Integer version;
	//总货道数
	private Integer totalWayNum;
	//总金额
	private BigDecimal totalPrice;
	//支付金额
	private BigDecimal payPrice;
	//商品总数
	private Integer totalItemNum;
	//机器所有货道商品信息
	List<ItemChangeDto> itemChangeList = new ArrayList<ItemChangeDto>();

	//开门id
	private String orderId;
	private Integer currWayNum;
	
	private String address;
	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getCurrWayNum() {
		return currWayNum;
	}

	public void setCurrWayNum(Integer currWayNum) {
		this.currWayNum = currWayNum;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getPower() {
		return power;
	}

	public void setPower(String power) {
		this.power = power;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

	public BigDecimal getPayPrice() {
		return payPrice;
	}

	public void setPayPrice(BigDecimal payPrice) {
		this.payPrice = payPrice;
	}

	public Integer getTotalItemNum() {
		return totalItemNum;
	}

	public void setTotalItemNum(Integer totalItemNum) {
		this.totalItemNum = totalItemNum;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public String getFactoryNumber() {
		return factoryNumber;
	}

	public void setFactoryNumber(String factoryNumber) {
		this.factoryNumber = factoryNumber;
	}

	public String getVmCode() {
		return vmCode;
	}

	public void setVmCode(String vmCode) {
		for(ItemChangeDto itemChange : itemChangeList){
			itemChange.setVmCode(vmCode);
		}
		this.vmCode = vmCode;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getTemperature() {
		return temperature;
	}

	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Integer getTotalWayNum() {
		return totalWayNum;
	}

	public void setTotalWayNum(Integer totalWayNum) {
		this.totalWayNum = totalWayNum;
	}

	public List<ItemChangeDto> getItemChangeList() {
		return itemChangeList;
	}

	public void setItemChangeList(List<ItemChangeDto> itemChangeList) {
		this.itemChangeList = itemChangeList;
	}
	
	
}
