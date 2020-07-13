package com.server.module.system.statisticsManage.machinesSaleStatistics;

import com.server.util.ReturnDataUtil;

import java.util.List;

public interface MachinesSaleStatisticsService {

	
	
	/**
	 * 统计每台机器销售信息
	 * @param payRecordForm
	 * @return Page<PerMachinesSaleDto>
	 */
	ReturnDataUtil findPerMachinesSale(MachinesSaleStatisticsForm machinesStatisticsForm);
	/**
	 * 统计每台机器销售详情信息
	 * @param machinesStatisticsForm
	 * @return Page<PerMachinesSaleDto>
	 */
	public ReturnDataUtil findPerMachinesSaleDetail(MachinesSaleStatisticsForm machinesStatisticsForm); 

	/**
	 * 统计每台机器用户消费记录
	 * @param machinesStatisticsForm
	 * @return ReturnDataUtil
	 */
	ReturnDataUtil findPerMachinesCustomerConsume(MachinesSaleStatisticsForm machinesStatisticsForm);
	
	/**
	 * 统计机器总销售信息
	 * @param machinesStatisticsForm
	 * @return Page<SumMachinesSaleDto>
	 */
	ReturnDataUtil findSumMachinesSale(MachinesSaleStatisticsForm machinesStatisticsForm);

}
