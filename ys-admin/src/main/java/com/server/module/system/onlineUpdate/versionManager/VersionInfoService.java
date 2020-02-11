package com.server.module.system.onlineUpdate.versionManager;

import java.util.List;

import com.server.util.ReturnDataUtil;

public interface VersionInfoService {

	/**
	 * 插入版本管理信息
	 * @author hebiting
	 * @date 2019年1月17日上午9:15:13
	 * @param versionInfo
	 * @return
	 */
	public ReturnDataUtil insert(VersionInfoBean versionInfo);
	
	/**
	 * 更新版本管理信息
	 * @author hebiting
	 * @date 2019年1月17日上午9:15:27
	 * @param versionInfo
	 * @return
	 */
	public ReturnDataUtil update(VersionInfoBean versionInfo);
	
	/**
	 * 根据当前版本信息获取可升级的版本
	 * @author hebiting
	 * @date 2019年1月16日下午5:58:10
	 * @param versionInfo
	 * @return
	 */
	public List<VersionInfoBean> getCanUpdateVersion(VersionInfoForm v);
	
	/**
	 * 根据条件查询版本信息及分页
	 * @author hebiting
	 * @date 2019年1月21日上午9:50:20
	 * @param form
	 * @return
	 */
	public ReturnDataUtil getByForm(VersionInfoForm form);
}
