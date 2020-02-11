package com.server.module.system.statisticsManage.payRecord;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.server.common.persistence.NotField;
import com.server.module.commonBean.PageAssist;

public class PayRecordForm extends PageAssist {

	private Long id;
	//商品id
	private Long itemId;
	//当前登录者公司id
	private Integer companyId;
	//公司及其子公司id
	private String companyIds;
	//售货机标识
	private String vendingMachinesCode;
	//可见的售货机标识
	private String vmCodes;
	//状态
	private Integer state;
	//支付方式
	private Integer payType;
	//支付编码
	private String payCode;
	//内容
	private String remark;
	//内部支付编码
	private String ptCode;
	//开始时间
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date startDate;
	//结束时间
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date endDate;
	//客户电话
	private String phone;
	//区域ID
	@NotField
	private Integer areaId;
	//货道号
	private Integer wayNumber; 
	//订单类型
	private Integer orderType;
	
	public Integer getOrderType() {
		return orderType;
	}
	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getAreaId() {
		return areaId;
	}
	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}
	public String getCompanyIds() {
		return companyIds;
	}
	public void setCompanyIds(String companyIds) {
		this.companyIds = companyIds;
	}
	public Long getItemId() {
		return itemId;
	}
	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}
	public String getVendingMachinesCode() {
		return vendingMachinesCode;
	}
	public void setVendingMachinesCode(String vendingMachinesCode) {
		this.vendingMachinesCode = vendingMachinesCode;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public Integer getPayType() {
		return payType;
	}
	public void setPayType(Integer payType) {
		this.payType = payType;
	}
	public String getPayCode() {
		return payCode;
	}
	public void setPayCode(String payCode) {
		this.payCode = payCode;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getPtCode() {
		return ptCode;
	}
	public void setPtCode(String ptCode) {
		this.ptCode = ptCode;
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
	public String getVmCodes() {
		return vmCodes;
	}
	public void setVmCodes(String vmCodes) {
		this.vmCodes = vmCodes;
	}
	public Integer getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public Integer getWayNumber() {
		return wayNumber;
	}
	public void setWayNumber(Integer wayNumber) {
		this.wayNumber = wayNumber;
	}
	
	
	
	
}
