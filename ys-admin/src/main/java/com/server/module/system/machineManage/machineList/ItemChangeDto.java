package com.server.module.system.machineManage.machineList;

import java.math.BigDecimal;

import org.springframework.cloud.client.discovery.event.InstanceRegisteredEvent;


public class ItemChangeDto {

	//机器编码
	private String vmCode;
	//货道号
	private Integer wayNum;
	//同一货道的序号
	private Integer orderNum;
	//商品改变数量，拿取为负数，添加为正数
	private Integer changeNum;
	//基础商品id
	private Long basicItemId;
	//商品名称
	private String itemName;
	//图片地址
	private String pic;
	//单价
	private BigDecimal unitPrice;
	//该商品规格
	private Integer unitWeight;
	//该商品数量
	private Integer num;
	//实际支付金额
	private BigDecimal realPayPrice;
	//changeNum的绝对值
	private Integer absChangeNum;
	//该货道总重量
	private Integer wayWeight;
	//待支付数量(初始值为absChangeNum)
	private Integer waitPayNum;
	//商品类型id
	private Integer itemTypeId;
	
	private Integer sumNum;
	
	private String state;
	
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public Integer getSumNum() {
		return sumNum;
	}
	public void setSumNum(Integer sumNum) {
		this.sumNum = sumNum;
	}
	public Integer getItemTypeId() {
		return itemTypeId;
	}
	public void setItemTypeId(Integer itemTypeId) {
		this.itemTypeId = itemTypeId;
	}
	public String getVmCode() {
		return vmCode;
	}
	public void setVmCode(String vmCode) {
		this.vmCode = vmCode;
	}
	public Integer getWayNum() {
		return wayNum;
	}
	public void setWayNum(Integer wayNum) {
		this.wayNum = wayNum;
	}
	public Integer getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}
	public Integer getChangeNum() {
		return changeNum;
	}
	public void setChangeNum(Integer changeNum) {
		this.changeNum = changeNum;
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
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public BigDecimal getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}
	public Integer getUnitWeight() {
		return unitWeight;
	}
	public void setUnitWeight(Integer unitWeight) {
		this.unitWeight = unitWeight;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	public BigDecimal getRealPayPrice() {
		return realPayPrice;
	}
	public void setRealPayPrice(BigDecimal realPayPrice) {
		this.realPayPrice = realPayPrice;
	}
	public Integer getAbsChangeNum() {
		return absChangeNum;
	}
	public void setAbsChangeNum(Integer absChangeNum) {
		this.absChangeNum = absChangeNum;
	}
	public Integer getWayWeight() {
		return wayWeight;
	}
	public void setWayWeight(Integer wayWeight) {
		this.wayWeight = wayWeight;
	}
	public Integer getWaitPayNum() {
		return waitPayNum;
	}
	public void setWaitPayNum(Integer waitPayNum) {
		this.waitPayNum = waitPayNum;
	}


}
