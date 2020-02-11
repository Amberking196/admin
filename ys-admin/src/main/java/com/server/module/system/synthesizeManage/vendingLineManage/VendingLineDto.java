package com.server.module.system.synthesizeManage.vendingLineManage;

import java.util.List;
import java.util.Map;

public class VendingLineDto {

		//线路id
		private Integer id;
		//线路名称
		private String name;
		//责任人名称
		private String dutyName;
		//责任人id
		private String dutyId;
		//公司名称
		private String companyName;
		//区域id
		private Integer areaId;
		//区域名称
		private String areaName;
		//公司id
		private Integer companyId;
		
		//负责人集合
		private List<VendingLineBean> dutyList;
		
		
		public Integer getCompanyId() {
			return companyId;
		}
		public void setCompanyId(Integer companyId) {
			this.companyId = companyId;
		}
		public String getAreaName() {
			return areaName;
		}
		public void setAreaName(String areaName) {
			this.areaName = areaName;
		}
		public Integer getId() {
			return id;
		}
		public void setId(Integer id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getDutyName() {
			return dutyName;
		}
		public void setDutyName(String dutyName) {
			this.dutyName = dutyName;
		}
		public String getDutyId() {
			return dutyId;
		}
		public void setDutyId(String dutyId) {
			this.dutyId = dutyId;
		}
		public Integer getAreaId() {
			return areaId;
		}
		public void setAreaId(Integer areaId) {
			this.areaId = areaId;
		}
		public String getCompanyName() {
			return companyName;
		}
		public void setCompanyName(String companyName) {
			this.companyName = companyName;
		}
		public List<VendingLineBean> getDutyList() {
			return dutyList;
		}
		public void setDutyList(List<VendingLineBean> dutyList) {
			this.dutyList = dutyList;
		}
		
		
		
		
}
