package com.server.module.system.statisticsManage.payRecordPerDay;

import java.util.Date;
import java.util.List;

import com.server.common.persistence.Page;
import com.server.module.system.statisticsManage.payRecord.PayRecordForm;

public interface PayRecordPerDayDao {

	
	/**
	 * 查询每天销售相关信息统计
	 * @param payRecordForm
	 * @return List<PayRecordPerDayDto>
	 */
	List<PayRecordPerDayDto> findPayRecordPerDay(PayRecordPerDayForm payDayForm);
	
	/**
	 * 查询每天销售记录总数
	 * @param payRecordForm
	 * @return long
	 */
	Long findPayRecordPerDayNum(PayRecordPerDayForm payDayForm);
	
	/**
	 * 查询正常销售机器总数
	 * @author hebiting
	 * @date 2018年7月23日下午5:58:03
	 * @param payDayForm date
	 * @return
	 */
	Integer findNormalMachines(PayRecordPerDayForm payDayForm,Date date);
	
	/**
	 * 查询用户购买情况
	 * @author hjc
	 * @date 2018年12月13日下午5:58:03
	 * @param payDayForm 
	 * @return
	 */
	List<PayRecordPerDayDto> userBuyStation(PayRecordPerDayForm payDayForm);
	
	public Long userBuyStationNum(PayRecordPerDayForm payDayForm) ;

}
