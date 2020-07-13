package com.server.module.system.statisticsManage.payRecordPerMonth;

import com.server.util.ReturnDataUtil;

public interface PayRecordPerMonthService {

	

	/**
	 * 查询销售月统计信息
	 * @param payMonthForm
	 * @return
	 */
	ReturnDataUtil findPayRecordPerMonth(PayRecordPerMonthForm payMonthForm);
}
