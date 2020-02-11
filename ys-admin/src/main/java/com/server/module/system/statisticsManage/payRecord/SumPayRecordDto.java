package com.server.module.system.statisticsManage.payRecord;

public class SumPayRecordDto {

	//总记录数
	private Long total;
	//销售总价
	private Double sumPrice;
	//进货总价
	private Double sumCostPrice;
	//支付失败总价
	private Double failurePrice;
	public Long getTotal() {
		return total;
	}
	public void setTotal(Long total) {
		this.total = total;
	}
	public Double getSumPrice() {
		return sumPrice;
	}
	public void setSumPrice(Double sumPrice) {
		this.sumPrice = sumPrice;
	}
	public Double getSumCostPrice() {
		return sumCostPrice;
	}
	public void setSumCostPrice(Double sumCostPrice) {
		this.sumCostPrice = sumCostPrice;
	}
	public Double getFailurePrice() {
		return failurePrice;
	}
	public void setFailurePrice(Double failurePrice) {
		this.failurePrice = failurePrice;
	}
	
}
