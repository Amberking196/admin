package com.server.module.system.statisticsManage.userActiveDegree;

import java.util.List;

import com.server.module.commonBean.TotalResultBean;

public interface UserActiveDegreeDao {
	
	/**
	 * 根据条件查询用户活跃度统计数据
	 * @author hebiting
	 * @date 2018年12月11日上午11:13:54
	 * @param form
	 * @return
	 */
	public TotalResultBean<List<UserActiveDegreeBean>> calculateUserActiveGegree(UserActiveDegreeForm form);

}
