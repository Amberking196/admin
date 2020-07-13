package com.server.module.system.machineManage.machineReplenish;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.server.module.commonBean.PageAssist;

public class MachineReplenishForm extends PageAssist {
     
	    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
		private Date startDate;  //查询起始时间
	    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
		private Date endDate;    //查询结束时间
	    //机器编号 
	    private String vmCode;
	    //货道号
	    private Integer wayNumber;
	
	    //当前用户所属公司id
	    private Integer companyId;
	    //当前公司及其子公司id组成的字符串
	    private String company;
	    //区域id
	    private Integer areaId;
	    
		public Integer getAreaId() {
			return areaId;
		}
		public void setAreaId(Integer areaId) {
			this.areaId = areaId;
		}
		public String getCompany() {
			return company;
		}
		public void setCompany(String company) {
			this.company = company;
		}
		public Integer getCompanyId() {
			return companyId;
		}
		public void setCompanyId(Integer companyId) {
			this.companyId = companyId;
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
	    
}
