package com.server.module.system.synthesizeManage.vendingAreaManage;

import java.util.List;

import com.server.module.system.synthesizeManage.vendingLineManage.VendingLineBean;

public class VendingAreaBean {

	//标识id
	private Integer id;
	//区域名称
	private String name;
	//父id
	private Integer pid;
	//是否启用
	private Integer isUsing;
	//区域所属公司id
	private Integer companyId;
	
	//是否删除
	private Integer deleteFlag;
	
	
	public Integer getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
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
	public Integer getPid() {
		return pid;
	}
	public void setPid(Integer pid) {
		this.pid = pid;
	}
	public Integer getIsUsing() {
		return isUsing;
	}
	public void setIsUsing(Integer isUsing) {
		this.isUsing = isUsing;
	}
	public Integer getDeleteFlag() {
		return deleteFlag;
	}
	public void setDeleteFlag(Integer deleteFlag) {
		this.deleteFlag = deleteFlag;
	}
	
	
}
