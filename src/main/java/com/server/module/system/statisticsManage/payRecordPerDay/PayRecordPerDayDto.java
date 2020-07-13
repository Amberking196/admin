package com.server.module.system.statisticsManage.payRecordPerDay;

import java.math.BigDecimal;
import java.util.Date;


public class PayRecordPerDayDto {

	//日期
	private Date createTime;
	//公司名称
	private String companyName;
	//售卖机编码
	private String vmCode;
	//商品名称
	private String itemName;
	//基础商品id
	private Long basicItemId;
	//公司id
	private Long companyId;
	//销售额
	private Double finishedMoney;
	//销售商品量
	private BigDecimal finishedItemNum;
	//销售订单数
	private BigDecimal finishedOrderNum;
	//退款金额
	private Double refundMoney;
	//退货数量
	private BigDecimal refundItemNum;
	//退款订单数
	private BigDecimal refundOrderNum;
	//成本
	private Double finishedCost;
	//毛利
	private Double profit;
	//产生订单机器数
	private Integer machinesNum;
	//平均每台销售机器成交量
	private Double average;
	//平均每台机器成交量
	private Double averageAll;
	//正常机器数
	private Integer normalNum;
	//平均每台机器成交额
	private BigDecimal averageMoney;
	private Double maxPrice;
	private Double minPrice;
	private String day;
	
	public Double getMaxPrice() {
		return maxPrice;
	}
	public void setMaxPrice(Double maxPrice) {
		this.maxPrice = maxPrice;
	}
	public Double getMinPrice() {
		return minPrice;
	}
	public void setMinPrice(Double minPrice) {
		this.minPrice = minPrice;
	}

	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	public Double getAverageAll() {
		return averageAll;
	}
	public void setAverageAll(Double averageAll) {
		this.averageAll = averageAll;
	}
	public BigDecimal getAverageMoney() {
		return averageMoney;
	}
	public void setAverageMoney(BigDecimal averageMoney) {
		this.averageMoney = averageMoney;
	}
	public Integer getNormalNum() {
		return normalNum;
	}
	public void setNormalNum(Integer normalNum) {
		this.normalNum = normalNum;
	}
	public Integer getMachinesNum() {
		return machinesNum;
	}
	public void setMachinesNum(Integer machinesNum) {
		this.machinesNum = machinesNum;
	}
	public Double getAverage() {
		return average;
	}
	public void setAverage(Double average) {
		this.average = average;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getVmCode() {
		return vmCode;
	}
	public void setVmCode(String vmCode) {
		this.vmCode = vmCode;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public Long getBasicItemId() {
		return basicItemId;
	}
	public void setBasicItemId(Long basicItemId) {
		this.basicItemId = basicItemId;
	}
	public Long getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}
	public Double getFinishedMoney() {
		return finishedMoney;
	}
	public void setFinishedMoney(Double finishedMoney) {
		this.finishedMoney = finishedMoney;
	}
	public BigDecimal getFinishedItemNum() {
		return finishedItemNum;
	}
	public void setFinishedItemNum(BigDecimal finishedItemNum) {
		this.finishedItemNum = finishedItemNum;
	}
	public BigDecimal getFinishedOrderNum() {
		return finishedOrderNum;
	}
	public void setFinishedOrderNum(BigDecimal finishedOrderNum) {
		this.finishedOrderNum = finishedOrderNum;
	}
	public Double getRefundMoney() {
		return refundMoney;
	}
	public void setRefundMoney(Double refundMoney) {
		this.refundMoney = refundMoney;
	}
	public BigDecimal getRefundItemNum() {
		return refundItemNum;
	}
	public void setRefundItemNum(BigDecimal refundItemNum) {
		this.refundItemNum = refundItemNum;
	}
	public BigDecimal getRefundOrderNum() {
		return refundOrderNum;
	}
	public void setRefundOrderNum(BigDecimal refundOrderNum) {
		this.refundOrderNum = refundOrderNum;
	}
	public Double getFinishedCost() {
		return finishedCost;
	}
	public void setFinishedCost(Double finishedCost) {
		this.finishedCost = finishedCost;
	}
	public Double getProfit() {
		return profit;
	}
	public void setProfit(Double profit) {
		this.profit = profit;
	}
	
	
	
	
	
}
