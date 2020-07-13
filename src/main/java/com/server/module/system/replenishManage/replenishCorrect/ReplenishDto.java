package com.server.module.system.replenishManage.replenishCorrect;

import java.util.Date;

public class ReplenishDto {

	//补货id
	private Long replenishId;
	//机器编码
	private String vmCode;
	//货道号
	private Integer wayNumber;
	//商品基础id
	private Long basicItemId;
	//商品名称
	private String itemName;
	//图片
	private String pic;
	//补货前数量
	private Integer preNum;
	//补货后数量
	private Integer num;
	//补货类型(2:补货数据，4：校准时补充数据)
	private Integer opType;
	//补货时间
	private Date replenishTime;
	
	private Integer changeNum;
	private Integer finalNum;
	private String opTypeName;
	
	private Integer state;
	private String stateName;

	
	public String getStateName() {
		return stateName;
	}
	public void setStateName(String stateName) {
		this.stateName = stateName;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public String getOpTypeName() {
		return opTypeName;
	}
	public void setOpTypeName(String opTypeName) {
		this.opTypeName = opTypeName;
	}
	public Integer getChangeNum() {
		return changeNum;
	}
	public void setChangeNum(Integer changeNum) {
		this.changeNum = changeNum;
	}
	public Integer getFinalNum() {
		return finalNum;
	}
	public void setFinalNum(Integer finalNum) {
		this.finalNum = finalNum;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public Integer getOpType() {
		return opType;
	}
	public void setOpType(Integer opType) {
		this.opType = opType;
	}
	public Long getReplenishId() {
		return replenishId;
	}
	public void setReplenishId(Long replenishId) {
		this.replenishId = replenishId;
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
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public Integer getPreNum() {
		return preNum;
	}
	public void setPreNum(Integer preNum) {
		this.preNum = preNum;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	public Date getReplenishTime() {
		return replenishTime;
	}
	public void setReplenishTime(Date replenishTime) {
		this.replenishTime = replenishTime;
	}
	
	
}
