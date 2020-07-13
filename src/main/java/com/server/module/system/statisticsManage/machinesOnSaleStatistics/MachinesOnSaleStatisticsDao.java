package com.server.module.system.statisticsManage.machinesOnSaleStatistics;

import java.util.List;

import com.server.module.system.statisticsManage.payRecord.PayRecordForm;
import com.server.util.ReturnDataUtil;

public interface MachinesOnSaleStatisticsDao {

	

	/**
	 * 统计在售商品信息
	 * @param machinesOnSaleStatisticsForm
	 * @return ReturnDataUtil
	 */
	ReturnDataUtil machinesOnSaleList(MachinesOnSaleStatisticsForm machinesOnSaleStatisticsForm);
	
	/**
	 * 在售商品信息详情
	 * @param machinesOnSaleStatisticsForm
	 * @return List<machinesOnSaleDto>
	 */
	ReturnDataUtil machinesOnSaleListDetail(MachinesOnSaleStatisticsForm machinesOnSaleStatisticsForm);

}
