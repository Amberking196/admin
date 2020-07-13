package com.server.module.system.synthesizeManage.vendingLineManage;

import java.util.List;

import com.server.module.commonBean.IdNameBean;
import com.server.module.system.adminUser.AdminUserBean;
import com.server.module.system.companyManage.CompanyBean;
import com.server.module.system.synthesizeManage.vendingAreaManage.VendingAreaBean;
import com.server.module.system.synthesizeManage.vendingAreaManage.VendingAreaDto;

public class CompanyAreaUserDto extends CompanyBean{

	//公司下的用户
	private List<AdminUserBean> userList;
	//公司下的区域列表
	private List<VendingAreaDto> areaList;
	//公司下的商品
	private List<IdNameBean> itemList;
	
	public List<IdNameBean> getItemList() {
		return itemList;
	}
	public void setItemList(List<IdNameBean> itemList) {
		this.itemList = itemList;
	}
	public List<AdminUserBean> getUserList() {
		return userList;
	}
	public void setUserList(List<AdminUserBean> userList) {
		this.userList = userList;
	}
	public List<VendingAreaDto> getAreaList() {
		return areaList;
	}
	public void setAreaList(List<VendingAreaDto> areaList) {
		this.areaList = areaList;
	}
	
	public void setComapanyValue(CompanyBean company){
		setAreaId(company.getAreaId());
		setCreateTime(company.getCreateTime());
		setId(company.getId());
		setLocation(company.getLocation());
		setLogoPic(company.getLogoPic());
		setMail(company.getMail());
		setName(company.getName());
		setParentId(company.getParentId());
		setPhone(company.getPhone());
		setPrincipal(company.getPrincipal());
		setShortName(company.getShortName());
		setState(company.getState());
	}
	
}
