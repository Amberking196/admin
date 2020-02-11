package com.server.module.system.officialManage.officialSection;

import com.server.module.commonBean.PageAssist;
/**
 * table name:  official_section
 * author name: hjc
 * create time: 2018-08-01 10:02:19
 */ 

public class OfficialSectionForm extends PageAssist{
	private Integer id;
	private Integer sonId; //用来查询子栏目
	private Integer thisId;//用来查询除本栏目的所有栏目
	private Integer pid;//用来查询除第一层栏目
	public Integer getPid() {
		return pid;
	}
	public void setPid(Integer pid) {
		this.pid = pid;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getSonId() {
		return sonId;
	}
	public void setSonId(Integer sonId) {
		this.sonId = sonId;
	}
	public Integer getThisId() {
		return thisId;
	}
	public void setThisId(Integer thisId) {
		this.thisId = thisId;
	}
	
	
}

