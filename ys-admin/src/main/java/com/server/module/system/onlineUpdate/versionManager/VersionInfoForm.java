package com.server.module.system.onlineUpdate.versionManager;

import com.server.module.commonBean.PageAssist;


public class VersionInfoForm extends PageAssist{

	private Integer type;//0降级 1升级
	private Integer versionId;//0降级 1升级
	
	
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getVersionId() {
		return versionId;
	}

	public void setVersionId(Integer versionId) {
		this.versionId = versionId;
	}

	private String pversion;

	public String getPversion() {
		return pversion;
	}

	public void setPversion(String pversion) {
		this.pversion = pversion;
	}
	
	
}
