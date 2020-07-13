package com.server.module.system.synthesizeManage.vendingAreaManage;

import java.util.List;

import com.server.module.system.synthesizeManage.vendingLineManage.VendingLineBean;
import com.server.module.system.synthesizeManage.vendingLineManage.VendingLineDto;

public class VendingAreaDto {

	// 标识id
	private Integer id;
	// 区域名称
	private String name;
	// 父id
	private Integer pid;
	// 是否启用
	private Integer isUsing;
	// 区域所属公司id
	private Integer companyId;
	// 公司名称
	private String companyName;
	// 子区域
	private List<VendingAreaDto> sonArea;
	// 父区域
	private VendingAreaDto faArea;

	// 区域下的路线
	private List<VendingLineBean> lineList;
	
	// 区域下的路线
	private List<VendingLineDto> list;

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public VendingAreaDto getFaArea() {
		return faArea;
	}

	public void setFaArea(VendingAreaDto faArea) {
		this.faArea = faArea;
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

	public List<VendingAreaDto> getSonArea() {
		return sonArea;
	}

	public void setSonArea(List<VendingAreaDto> sonArea) {
		this.sonArea = sonArea;
	}

	
	public List<VendingLineBean> getLineList() {
		return lineList;
	}
	public void setLineList(List<VendingLineBean> lineList) {
		this.lineList = lineList;
	}

	public List<VendingLineDto> getList() {
		return list;
	}

	public void setList(List<VendingLineDto> list) {
		this.list = list;
	}
	
	
}
