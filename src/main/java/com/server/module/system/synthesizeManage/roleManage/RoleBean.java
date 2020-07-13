package com.server.module.system.synthesizeManage.roleManage;

import java.util.Date;
import java.util.List;

import com.server.common.persistence.NotField;

public class RoleBean {

	//角色id
	private Integer id;
	//角色名称
	private String name;
	//角色拥有的权限
	private String privilege;
	//公司id
	private Integer companyId;
	//公司名
	private String companyName;
	//创建时间
	private Date createTime;
	//父级id
	private Integer parentId;
	//创建者id
	private Long createUser;
	//创建者姓名
	private String createUserName;
	@NotField
	private List<Integer> adminUserList;
	
	public List<Integer> getAdminUserList() {
		return adminUserList;
	}
	public void setAdminUserList(List<Integer> adminUserList) {
		this.adminUserList = adminUserList;
	}
	public String getCreateUserName() {
		return createUserName;
	}
	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}
	public Long getCreateUser() {
		return createUser;
	}
	public void setCreateUser(Long createUser) {
		this.createUser = createUser;
	}
	public Integer getParentId() {
		return parentId;
	}
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
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
	public String getPrivilege() {
		return privilege;
	}
	public void setPrivilege(String privilege) {
		this.privilege = privilege;
	}
	public Integer getCompanyId() {
		return companyId;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}
	
	
}
