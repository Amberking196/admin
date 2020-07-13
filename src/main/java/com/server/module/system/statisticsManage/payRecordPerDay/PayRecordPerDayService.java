package com.server.module.system.statisticsManage.payRecordPerDay;

import java.util.Date;
import java.util.List;

import com.server.common.persistence.Page;
import com.server.module.system.statisticsManage.payRecord.PayRecordForm;
import com.server.util.ReturnDataUtil;

public interface PayRecordPerDayService {

	
	/**
	 * 查询每天销售相关信息统计
	 * @param payRecordForm
	 * @return List<PayRecordPerDayDto>
	 */
	ReturnDataUtil findPayRecordPerDay(PayRecordPerDayForm payDayForm);
	
	
	List<PayRecordPerDayDto> exportPayRecordPerDay(PayRecordPerDayForm payDayForm);
	
	
	/**
	 * 查询正常销售机器总数
	 * @author hebiting
	 * @date 2018年7月23日下午5:58:03
	 * @param payDayForm
	 * @return
	 */
	Integer findNormalMachines(PayRecordPerDayForm payDayForm, Date reportDate);
	
	public List<PayRecordPerDayDto> userBuyStation(PayRecordPerDayForm payDayForm);


	public ReturnDataUtil userBuyStationNum(PayRecordPerDayForm payDayForm);
}
