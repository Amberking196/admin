package com.server.module.system.machineManage.distorymachine;

import com.server.module.commonBean.PageAssist;

public class distorymachineForm extends PageAssist{
     
	    //公司id
		private Integer companyId;
		//当前公司id以及子公司id
		private String companyIds;
		//故障机的编号
		private String code;
		// 前台是一个下拉框，里面有1小时内，1天内，1周内等
		private String date;
		Integer areaId;
		
		public Integer getAreaId() {
			return areaId;
		}
		public void setAreaId(Integer areaId) {
			this.areaId = areaId;
		}
		public String getDate() {
			return date;
		}
		public void setDate(String date) {
			this.date = date;
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
		public String getCode() {
			return code;
		}
		public void setCode(String code) {
			this.code = code;
		}
}
