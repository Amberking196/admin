package com.server.module.system.onlineUpdate.versionManager;

import java.util.List;

public interface VersionInfoDao {

	/**
	 * 插入版本管理信息
	 * @author hebiting
	 * @date 2019年1月16日下午5:47:43
	 * @param versionInfo
	 * @return
	 */
	public Integer insert(VersionInfoBean versionInfo);
	/**
	 * 更新版本管理信息
	 * @author hebiting
	 * @date 2019年1月16日下午5:48:05
	 * @param versionInfo
	 * @return
	 */
	public boolean update(VersionInfoBean versionInfo);
	/**
	 * 根据当前版本信息获取可升级的版本
	 * @author hebiting
	 * @date 2019年1月16日下午5:58:10
	 * @param versionInfo
	 * @return
	 */
	public List<VersionInfoBean> getCanUpdateVersion(VersionInfoForm v);
	/**
	 * 根据id获取版本信息
	 * @author hebiting
	 * @date 2019年1月16日下午6:27:13
	 * @param id
	 * @return
	 */
	public VersionInfoBean getById(Integer id);
	
	/**
	 * 根据版本信息获取版本详情
	 * @author hebiting
	 * @date 2019年1月16日下午6:27:13
	 * @param id
	 * @return
	 */
	public VersionInfoBean getByVersionInfo(String versionInfo);
	/**
	 * 根据条件查询版本信息及分页
	 * @author hebiting
	 * @date 2019年1月21日上午9:50:20
	 * @param form
	 * @return
	 */
	public List<VersionInfoBean> getByForm(VersionInfoForm form);
	/**
	 * 获取总数
	 * @author hebiting
	 * @date 2019年1月21日上午10:35:58
	 * @return
	 */
	public Long getTotal();
}
