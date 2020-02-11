package com.server.module.system.statisticsManage.payRecordPerMonth;

import java.util.List;

import com.server.module.system.statisticsManage.payRecordPerWeek.PayRecordPerWeekForm;
import com.server.util.ReturnDataUtil;


public interface PayRecordPerMonthDao {

	/**
	 * 查询销售月统计信息
	 * @param payMonthForm
	 * @return
	 */
	public ReturnDataUtil listPage(PayRecordPerMonthForm PayRecordPerMonthForm);
	
	/**
	 * 查询最近7天销售记录条数
	 * @param payMonthForm
	 * @return
	 */
	public Long findPayRecordPerMonthNum(PayRecordPerMonthForm payMonthForm);
	/**
	 * 查询销售月记录统计信息总记录数
	 * @param payMonthForm
	 * @return
	 */
	public Long findPayRecordPerMonthNum2(PayRecordPerMonthForm payMonthForm);
}
