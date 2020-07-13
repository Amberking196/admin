package com.server.module.system.replenishManage.replenishCorrect;

import java.util.List;

public class ReplenishCollectDto {

	//机器编码
	private String vmCode;
	//货道号
	private Integer wayNumber;
	//商品基础Id
	private Long basicItemId;
	//商品名称
	private String itemName;
	//现补货数量
	private Integer replenishNum;
	//可能补货数量
	private Integer probableNum;
	//商品图片
	private String pic;
	//补货时间
	private String replenishTime;
	//如果需要修正，调用修正的接口，须传这个参数的值
	private String needFrontValue;
	//补货的详情数据
	private List<ReplenishDto> replenishList;

	private String state;
	private String stateName;
	




	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getNeedFrontValue() {
		return needFrontValue;
	}

	public void setNeedFrontValue(String needFrontValue) {
		this.needFrontValue = needFrontValue;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getReplenishTime() {
		return replenishTime;
	}

	public void setReplenishTime(String replenishTime) {
		this.replenishTime = replenishTime;
	}

	public String getVmCode() {
		return vmCode;
	}

	public void setVmCode(String vmCode) {
		this.vmCode = vmCode;
	}

	public Integer getWayNumber() {
		return wayNumber;
	}

	public void setWayNumber(Integer wayNumber) {
		this.wayNumber = wayNumber;
	}

	
	public Long getBasicItemId() {
		return basicItemId;
	}

	public void setBasicItemId(Long basicItemId) {
		this.basicItemId = basicItemId;
	}

	public Integer getReplenishNum() {
		return replenishNum;
	}

	public void setReplenishNum(Integer replenishNum) {
		this.replenishNum = replenishNum;
	}

	public Integer getProbableNum() {
		return probableNum;
	}

	public void setProbableNum(Integer probableNum) {
		this.probableNum = probableNum;
	}

	public List<ReplenishDto> getReplenishList() {
		return replenishList;
	}

	public void setReplenishList(List<ReplenishDto> replenishList) {
		this.replenishList = replenishList;
	}
	
	
	
}
