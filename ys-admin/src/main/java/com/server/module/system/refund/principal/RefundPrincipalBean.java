package com.server.module.system.refund.principal;

import java.util.Date;

public class RefundPrincipalBean {

	private Integer id;//负责人
	private Long loginInfoId;//负责人在login_info表id
	private String phone;//负责人电话
	private Integer deleteFlag;//是否删除1：删除0未删除
	private Long createUser;//创建人
	private Date createTime;//创建时间
	private Long updateUser;//更新人
	private Date updateTime;//更新时间
	
	private String userName;//负责人名称
	private String createUserName;//创建者名称
	private String updateUserName;//更新人名称
	
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getCreateUserName() {
		return createUserName;
	}
	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}
	public String getUpdateUserName() {
		return updateUserName;
	}
	public void setUpdateUserName(String updateUserName) {
		this.updateUserName = updateUserName;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Long getLoginInfoId() {
		return loginInfoId;
	}
	public void setLoginInfoId(Long loginInfoId) {
		this.loginInfoId = loginInfoId;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public Integer getDeleteFlag() {
		return deleteFlag;
	}
	public void setDeleteFlag(Integer deleteFlag) {
		this.deleteFlag = deleteFlag;
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
	
	
}
