package com.server.module.system.statisticsManage.payRecordPerMonth;

import java.math.BigDecimal;

public class PayRecordPerMonthDto {

	//日期
	private String reportDate;
	//总订单数
	private Long sumOrderNum;
	//总销售额
	private Double sumFinishedMoney;
	//总毛利
	private Double sumProfit;
	//总订单数量
	private BigDecimal sumFinishedOrderNum;
	//总退款商品数
	private BigDecimal sumRefundItemNum;
	//总退款额
	private Double sumRefundMoney;
	//总退单数
	private BigDecimal sumRefundOrderNum;
	//总成本
	private Double sumFinishedCost;
	public String getReportDate() {
		return reportDate;
	}
	public void setReportDate(String reportDate) {
		this.reportDate = reportDate;
	}
	public Long getSumOrderNum() {
		return sumOrderNum;
	}
	public void setSumOrderNum(Long sumOrderNum) {
		this.sumOrderNum = sumOrderNum;
	}
	public Double getSumFinishedMoney() {
		return sumFinishedMoney;
	}
	public void setSumFinishedMoney(Double sumFinishedMoney) {
		this.sumFinishedMoney = sumFinishedMoney;
	}
	public Double getSumProfit() {
		return sumProfit;
	}
	public void setSumProfit(Double sumProfit) {
		this.sumProfit = sumProfit;
	}
	public BigDecimal getSumFinishedOrderNum() {
		return sumFinishedOrderNum;
	}
	public void setSumFinishedOrderNum(BigDecimal sumFinishedOrderNum) {
		this.sumFinishedOrderNum = sumFinishedOrderNum;
	}
	public BigDecimal getSumRefundItemNum() {
		return sumRefundItemNum;
	}
	public void setSumRefundItemNum(BigDecimal sumRefundItemNum) {
		this.sumRefundItemNum = sumRefundItemNum;
	}
	public Double getSumRefundMoney() {
		return sumRefundMoney;
	}
	public void setSumRefundMoney(Double sumRefundMoney) {
		this.sumRefundMoney = sumRefundMoney;
	}
	public BigDecimal getSumRefundOrderNum() {
		return sumRefundOrderNum;
	}
	public void setSumRefundOrderNum(BigDecimal sumRefundOrderNum) {
		this.sumRefundOrderNum = sumRefundOrderNum;
	}
	public Double getSumFinishedCost() {
		return sumFinishedCost;
	}
	public void setSumFinishedCost(Double sumFinishedCost) {
		this.sumFinishedCost = sumFinishedCost;
	}
	
	
}
