package com.server.module.system.statisticsManage.userState;

import java.util.List;
import java.util.Map;

import com.server.module.system.statisticsManage.payRecordPerDay.PayRecordPerDayForm;

public interface UserStateService {
	//用户转换状态数量统计(无用)
	public UserStateBean stateCompare(PayRecordPerDayForm payDayForm);
	
	/**
	 * 用户状态对比
	 * @param payRecordForm
	 * @return List<UserStateBean>
	 */
	public List<UserStateVo> userStateNum(PayRecordPerDayForm payDayForm);
	
	
	/**
	 * 用户状态对比
	 * @param payRecordForm
	 * @return List<UserStateBean>
	 */
	public List<UserStateVo> userStateNum2(PayRecordPerDayForm payDayForm);
	
	/**
	 * 用户状态日统计(图表)
	 * @param payRecordForm
	 * @return Map<String,Object>
	 */
	public Map<String,Object> userStateNumChart(PayRecordPerDayForm payDayForm);
}
