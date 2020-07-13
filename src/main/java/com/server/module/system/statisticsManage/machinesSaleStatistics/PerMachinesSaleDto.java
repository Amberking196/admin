package com.server.module.system.statisticsManage.machinesSaleStatistics;

import java.util.Date;

import com.server.common.persistence.NotField;

public class PerMachinesSaleDto {

	//机器编码
	private String vmCode;
	//公司名称
	private String companyName;
	//订单数量
	private Integer sumFinishedOrderNum;
	//总价
	private Double sumFinishedMoney;
	//售卖商品数
	private Double sumFinishedItemNum;
	//总成本价
	private Double sumFinishedCost;
	//客户单价
	private String cusUnitPrice;
	//售卖单价
	private String saleUnitPrice;
	//成本单价
	private String costUnitPrice;
	//单品毛利
	private String profitUnitPrice;
	//总毛利
	private String sumProfit;
	//退款订单总数
	private Integer sumRefundOrderNum;
	//退款金额总数
	private Double sumRefundMoney;
	//退款商品总数
	private Integer sumRefundItemNum;
	//起始日期
	private Date startDate;
	//结束日期
	private Date endDate;
	// 售货机地址
	private String vendingMachinesAddress;
	// 商品原价
	private Double  basicPrice;
	// 优惠价格
	private Double  freePrice;
	//商品名称
	@NotField
	private String name;


	public Double getFreePrice() {
		return freePrice;
	}

	public void setFreePrice(Double freePrice) {
		this.freePrice = freePrice;
	}

	public Double getBasicPrice() {
		return basicPrice;
	}

	public void setBasicPrice(Double basicPrice) {
		this.basicPrice = basicPrice;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVendingMachinesAddress() {
		return vendingMachinesAddress;
	}

	public void setVendingMachinesAddress(String vendingMachinesAddress) {
		this.vendingMachinesAddress = vendingMachinesAddress;
	}

	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Integer getSumRefundOrderNum() {
		return sumRefundOrderNum;
	}
	public void setSumRefundOrderNum(Integer sumRefundOrderNum) {
		this.sumRefundOrderNum = sumRefundOrderNum;
	}
	public Double getSumRefundMoney() {
		return sumRefundMoney;
	}
	public void setSumRefundMoney(Double sumRefundMoney) {
		this.sumRefundMoney = sumRefundMoney;
	}
	public Integer getSumRefundItemNum() {
		return sumRefundItemNum;
	}
	public void setSumRefundItemNum(Integer sumRefundItemNum) {
		this.sumRefundItemNum = sumRefundItemNum;
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
	public Integer getSumFinishedOrderNum() {
		return sumFinishedOrderNum;
	}
	public void setSumFinishedOrderNum(Integer sumFinishedOrderNum) {
		this.sumFinishedOrderNum = sumFinishedOrderNum;
	}
	public Double getSumFinishedMoney() {
		return sumFinishedMoney;
	}
	public void setSumFinishedMoney(Double sumFinishedMoney) {
		this.sumFinishedMoney = sumFinishedMoney;
	}
	public Double getSumFinishedItemNum() {
		return sumFinishedItemNum;
	}
	public void setSumFinishedItemNum(Double sumFinishedItemNum) {
		this.sumFinishedItemNum = sumFinishedItemNum;
	}
	public Double getSumFinishedCost() {
		return sumFinishedCost;
	}
	public void setSumFinishedCost(Double sumFinishedCost) {
		this.sumFinishedCost = sumFinishedCost;
	}
	public String getCusUnitPrice() {
		return cusUnitPrice;
	}
	public void setCusUnitPrice(String cusUnitPrice) {
		this.cusUnitPrice = cusUnitPrice;
	}
	public String getSaleUnitPrice() {
		return saleUnitPrice;
	}
	public void setSaleUnitPrice(String saleUnitPrice) {
		this.saleUnitPrice = saleUnitPrice;
	}
	public String getCostUnitPrice() {
		return costUnitPrice;
	}
	public void setCostUnitPrice(String costUnitPrice) {
		this.costUnitPrice = costUnitPrice;
	}
	public String getProfitUnitPrice() {
		return profitUnitPrice;
	}
	public void setProfitUnitPrice(String profitUnitPrice) {
		this.profitUnitPrice = profitUnitPrice;
	}
	public String getSumProfit() {
		return sumProfit;
	}
	public void setSumProfit(String sumProfit) {
		this.sumProfit = sumProfit;
	}
	

	
}
