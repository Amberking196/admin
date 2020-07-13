package com.server.module.system.synthesizeManage.vendingLineManage;

import java.util.List;
import com.server.module.system.machineManage.machineList.VendingMachinesInfoBean;

/**
 * table : vending_line 
 * @author hebiting
 * @date 2018年4月9日上午9:46:18
 */
public class VendingLineBean  {

	//线路id
	private Integer id;
	//线路名称
	private String name;
	//责任人名称
	private String dutyName;
	//责任人id
	private String dutyId;
	//公司id
	private Integer companyId;
	//区域id
	private Integer areaId;
	//是否删除
	private Integer deleteFlag;
	
	//给前端使用 属性
	private Integer isShow;
	
	private String ifShow;
	
	private boolean showin;
	
	//机器集合
	private List<VendingMachinesInfoBean> list;
	
	
	//是否启用（1：启用，0：禁用）
	private Integer isUsing;
	
	public Integer getIsUsing() {
		return isUsing;
	}
	public void setIsUsing(Integer isUsing) {
		this.isUsing = isUsing;
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
	public Integer getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}
	public Integer getAreaId() {
		return areaId;
	}
	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}
	public List<VendingMachinesInfoBean> getList() {
		return list;
	}
	public void setList(List<VendingMachinesInfoBean> list) {
		this.list = list;
	}
	public Integer getIsShow() {
		return isShow;
	}
	public void setIsShow(Integer isShow) {
		this.isShow = isShow;
	}
	public String getIfShow() {
		return ifShow;
	}
	public void setIfShow(String ifShow) {
		this.ifShow = ifShow;
	}
	public boolean isShowin() {
		return showin;
	}
	public void setShowin(boolean showin) {
		this.showin = showin;
	}
	
	
	
}
