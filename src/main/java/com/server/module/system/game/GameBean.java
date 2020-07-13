package com.server.module.system.game;

import java.util.Date;


import com.fasterxml.jackson.annotation.JsonFormat;

public class GameBean {
	
	private Integer id;
	private String name;
	private Integer type;//1：转盘 2：其他类型
	private Integer times;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date startTime;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date endTime;
	private Integer canReceive;
	private Long createUser;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	private Long updateUser;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updateTime;
	private Integer deleteFlag;
	private Integer target;
	private Integer companyId;
	private Integer areaId;
	private String vmCode;
	private Integer needGo;
	private Integer integral;
	private String gameTypeName;
	private String needGoName;
	private String companyName;
	private String areaName;
	private Integer prizeNum;
	

	public Integer getPrizeNum() {
		return prizeNum;
	}
	public void setPrizeNum(Integer prizeNum) {
		this.prizeNum = prizeNum;
	}
	public String getGameTypeName() {
		return gameTypeName;
	}
	public void setGameTypeName(String gameTypeName) {
		this.gameTypeName = gameTypeName;
	}
	public String getNeedGoName() {
		return needGoName;
	}
	public void setNeedGoName(String needGoName) {
		this.needGoName = needGoName;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public Integer getTarget() {
		return target;
	}
	public void setTarget(Integer target) {
		this.target = target;
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
	public String getVmCode() {
		return vmCode;
	}
	public void setVmCode(String vmCode) {
		this.vmCode = vmCode;
	}
	public Integer getNeedGo() {
		return needGo;
	}
	public void setNeedGo(Integer needGo) {
		this.needGo = needGo;
		if(needGo == 1){
			this.needGoName = "购物后参与";
		}else {
			this.needGoName = "均可参与";
		}
	}
	public Integer getTimes() {
		return times;
	}
	public void setTimes(Integer times) {
		this.times = times;
	}
	public Integer getDeleteFlag() {
		return deleteFlag;
	}
	public void setDeleteFlag(Integer deleteFlag) {
		this.deleteFlag = deleteFlag;
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
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
		if(type == 1){
			this.gameTypeName = "转盘";
		}else{
			this.gameTypeName = "其他类型";
		}
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public Integer getCanReceive() {
		return canReceive;
	}
	public void setCanReceive(Integer canReceive) {
		this.canReceive = canReceive;
	}
	public Long getCreateUser() {
		return createUser;
	}
	public void setCreateUser(Long createUser) {
		this.createUser = createUser;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Long getUpdateUser() {
		return updateUser;
	}
	public void setUpdateUser(Long updateUser) {
		this.updateUser = updateUser;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public Integer getIntegral() {
		return integral;
	}
	public void setIntegral(Integer integral) {
		this.integral = integral;
	}
	

}
