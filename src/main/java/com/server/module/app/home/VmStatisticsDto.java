package com.server.module.app.home;

import java.util.List;

import com.server.module.app.payRecord.SumPayRecordDto;
import com.server.module.app.vminfo.VminfoDto;


public class VmStatisticsDto{

	//须补货机器数
	private Integer replenishNum;
	//机器故障数
	private Integer troubleNum;
	//机器总数
	private Integer machinesNum;
	//补货机器集合
	private List<VminfoDto> replenishList;
	//故障机器集合
	private List<VminfoDto> troubleList;
	//当天销售统计
	private SumPayRecordDto sumPayRecordDto;
	
	public SumPayRecordDto getSumPayRecordDto() {
		return sumPayRecordDto;
	}
	public void setSumPayRecordDto(SumPayRecordDto sumPayRecordDto) {
		this.sumPayRecordDto = sumPayRecordDto;
	}
	public Integer getReplenishNum() {
		return replenishNum;
	}
	public void setReplenishNum(Integer replenishNum) {
		this.replenishNum = replenishNum;
	}
	public Integer getTroubleNum() {
		return troubleNum;
	}
	public void setTroubleNum(Integer troubleNum) {
		this.troubleNum = troubleNum;
	}
	public Integer getMachinesNum() {
		return machinesNum;
	}
	public void setMachinesNum(Integer machinesNum) {
		this.machinesNum = machinesNum;
	}
	public List<VminfoDto> getReplenishList() {
		return replenishList;
	}
	public void setReplenishList(List<VminfoDto> replenishList) {
		this.replenishList = replenishList;
	}
	public List<VminfoDto> getTroubleList() {
		return troubleList;
	}
	public void setTroubleList(List<VminfoDto> troubleList) {
		this.troubleList = troubleList;
	}
	
	
}
