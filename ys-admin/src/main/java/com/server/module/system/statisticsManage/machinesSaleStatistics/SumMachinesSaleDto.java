package com.server.module.system.statisticsManage.machinesSaleStatistics;

public class SumMachinesSaleDto {
	//机器平均销售金额
	private Double avgPerMachinesSalePrice;
	//机器平均销售数量
	private Integer avgPerMachinesSaleNum;
	//销售总数
	private Integer sumMachinesSaleNum;
	
	public Double getAvgPerMachinesSalePrice() {
		return avgPerMachinesSalePrice;
	}
	public void setAvgPerMachinesSalePrice(Double avgPerMachinesSalePrice) {
		this.avgPerMachinesSalePrice = avgPerMachinesSalePrice;
	}
	public Integer getAvgPerMachinesSaleNum() {
		return avgPerMachinesSaleNum;
	}
	public void setAvgPerMachinesSaleNum(Integer avgPerMachinesSaleNum) {
		this.avgPerMachinesSaleNum = avgPerMachinesSaleNum;
	}
	public Integer getSumMachinesSaleNum() {
		return sumMachinesSaleNum;
	}
	public void setSumMachinesSaleNum(Integer sumMachinesSaleNum) {
		this.sumMachinesSaleNum = sumMachinesSaleNum;
	}

}
