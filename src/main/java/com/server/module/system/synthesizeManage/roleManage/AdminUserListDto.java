package com.server.module.system.synthesizeManage.roleManage;

import java.util.List;

import com.server.module.system.adminUser.AdminUserBean;

public class AdminUserListDto {

	List<AdminUserBean> adminUserList;
	List<AdminUserBean> adminUserAllList;
	List<AdminUserBean> adminUserNotBindList;

	public List<AdminUserBean> getAdminUserNotBindList() {
		return adminUserNotBindList;
	}
	public void setAdminUserNotBindList(List<AdminUserBean> adminUserNotBindList) {
		this.adminUserNotBindList = adminUserNotBindList;
	}
	public List<AdminUserBean> getAdminUserList() {
		return adminUserList;
	}
	public void setAdminUserList(List<AdminUserBean> adminUserList) {
		this.adminUserList = adminUserList;
	}
	public List<AdminUserBean> getAdminUserAllList() {
		return adminUserAllList;
	}
	public void setAdminUserAllList(List<AdminUserBean> adminUserAllList) {
		this.adminUserAllList = adminUserAllList;
	}


}
