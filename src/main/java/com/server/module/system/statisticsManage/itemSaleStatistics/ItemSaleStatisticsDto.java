package com.server.module.system.statisticsManage.itemSaleStatistics;

import java.math.BigDecimal;

import com.server.module.commonBean.PageAssist;

public class ItemSaleStatisticsDto{

	//商品id
	private Long basicItemId; 
	//商品名称
	private String itemName;
	//条形码
	private String barCode;
	//规格
	private String standard;
	//单位
	private String unit;
	//上架机器数
	private String machinesNum;
	//商品类型id
	private Integer itemTypeId;
	//总销售额
	private Double sumFinishedMoney;
	//总订单数
	private Integer sumFinishedOrderNum;
	//总销售商品数
	private Integer sumFinishedItemNum;
	//总成本
	private Double sumFinishedCost;
	//总毛利
	private Double sumProfit;
	//销售单价
	private Double unitPrice;
	//成本单价
	private Double costUnitPrice;
	//单品毛利
	private Double unitProfit;
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
	public String getBarCode() {
		return barCode;
	}
	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}
	public String getStandard() {
		return standard;
	}
	public void setStandard(String standard) {
		this.standard = standard;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getMachinesNum() {
		return machinesNum;
	}
	public void setMachinesNum(String machinesNum) {
		this.machinesNum = machinesNum;
	}
	public Integer getItemTypeId() {
		return itemTypeId;
	}
	public void setItemTypeId(Integer itemTypeId) {
		this.itemTypeId = itemTypeId;
	}
	public Double getSumFinishedMoney() {
		return sumFinishedMoney;
	}
	public void setSumFinishedMoney(Double sumFinishedMoney) {
		this.sumFinishedMoney = sumFinishedMoney;
	}
	public Integer getSumFinishedOrderNum() {
		return sumFinishedOrderNum;
	}
	public void setSumFinishedOrderNum(Integer sumFinishedOrderNum) {
		this.sumFinishedOrderNum = sumFinishedOrderNum;
	}
	public Integer getSumFinishedItemNum() {
		return sumFinishedItemNum;
	}
	public void setSumFinishedItemNum(Integer sumFinishedItemNum) {
		this.sumFinishedItemNum = sumFinishedItemNum;
	}
	public Double getSumFinishedCost() {
		return sumFinishedCost;
	}
	public void setSumFinishedCost(Double sumFinishedCost) {
		this.sumFinishedCost = sumFinishedCost;
	}
	public Double getSumProfit() {
		return sumProfit;
	}
	public void setSumProfit(Double sumProfit) {
		this.sumProfit = sumProfit;
	}
	public Double getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}
	public Double getCostUnitPrice() {
		return costUnitPrice;
	}
	public void setCostUnitPrice(Double costUnitPrice) {
		this.costUnitPrice = costUnitPrice;
	}
	public Double getUnitProfit() {
		return unitProfit;
	}
	public void setUnitProfit(Double unitProfit) {
		this.unitProfit = unitProfit;
	}
	
	
	
	
}
