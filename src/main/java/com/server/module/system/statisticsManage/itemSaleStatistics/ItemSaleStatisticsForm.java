package com.server.module.system.statisticsManage.itemSaleStatistics;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.server.module.commonBean.PageAssist;

public class ItemSaleStatisticsForm extends PageAssist{

	//商品id
	private Long itemId;
	//起始日期
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date startDate;
	//结束日期
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date endDate;
	//排序所用列名
	private String orderByType;
	//排序（升 asc 降 desc）
	private String sortType;
	//售卖机编码集
	private String vmCodes;
	//当前登录者公司
	private Integer companyId;
	//公司及其子公司id
	private String companyIds;
	
	private Integer areaId;
	
	
	public Integer getAreaId() {
		return areaId;
	}
	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}
	public Integer getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
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
	public String getOrderByType() {
		return orderByType;
	}
	public void setOrderByType(String orderByType) {
		this.orderByType = orderByType;
	}
	public String getSortType() {
		return sortType;
	}
	public void setSortType(String sortType) {
		this.sortType = sortType;
	}
	public String getVmCodes() {
		return vmCodes;
	}
	public void setVmCodes(String vmCodes) {
		this.vmCodes = vmCodes;
	}
	
}
