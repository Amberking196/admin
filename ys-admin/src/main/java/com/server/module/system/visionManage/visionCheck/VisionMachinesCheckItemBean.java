package com.server.module.system.visionManage.visionCheck;

public class VisionMachinesCheckItemBean {
	public String  name;
	public Integer id;
	public Integer visionId;
	public Integer number;
	public String checkId;
	
	public String getCheckId() {
		return checkId;
	}
	public void setCheckId(String checkId) {
		this.checkId = checkId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getVisionId() {
		return visionId;
	}
	public void setVisionId(Integer visionId) {
		this.visionId = visionId;
	}
	public Integer getNumber() {
		return number;
	}
	public void setNumber(Integer number) {
		this.number = number;
	}
}
