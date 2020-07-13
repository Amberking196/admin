package com.server.module.system.machineManage.distorymachine;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class distorymachineBean {
	//序号
  private Integer id;
   
  private String code;//售货机编号
  
  private String machineCode;//机器号
  
  private String locatoinName;//货机地址
  
  
  public String getLocatoinName() {
	return locatoinName;
}

public void setLocatoinName(String locatoinName) {
	this.locatoinName = locatoinName;
}

public Integer getId() {
	return id;
}

public void setId(Integer id) {
	this.id = id;
}

public String getCode() {
	return code;
}

public void setCode(String code) {
	this.code = code;
}

public String getMachineCode() {
	return machineCode;
}

public void setMachineCode(String machineCode) {
	this.machineCode = machineCode;
}

public String getInfo() {
	return info;
}

public void setInfo(String info) {
	this.info = info;
}

public Date getCreateTime() {
	return createTime;
}

public void setCreateTime(Date createTime) {
	this.createTime = createTime;
}

public String getCreateUser() {
	return createUser;
}

public void setCreateUser(String createUser) {
	this.createUser = createUser;
}

public Date getUpdateTime() {
	return updateTime;
}

public void setUpdateTime(Date updateTime) {
	this.updateTime = updateTime;
}

public String getUpdateUser() {
	return updateUser;
}

public void setUpdateUser(String updateUser) {
	this.updateUser = updateUser;
}

private String info;//信息
  
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08")
  private Date createTime; //注册时间/创建时间
  
  private String createUser;//创建用户
  
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08")
  private Date updateTime; //更新时间
  
  private String updateUser;//更新用户
  
  private Integer companyId;//公司id
  
  private String name;//公司名称

	private Integer num;//没个机器故障次数   一段时间内

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public Integer getCompanyId() {
	return companyId;
}

public void setCompanyId(Integer companyId) {
	this.companyId = companyId;
}

public String getName() {
	return name;
}

public void setName(String name) {
	this.name = name;
}
}
