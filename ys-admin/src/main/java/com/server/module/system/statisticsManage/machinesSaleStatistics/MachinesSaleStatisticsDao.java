package com.server.module.system.statisticsManage.machinesSaleStatistics;

import java.util.List;

import com.server.module.system.statisticsManage.payRecord.PayRecordForm;
import com.server.util.ReturnDataUtil;

public interface MachinesSaleStatisticsDao {

	

	/**
	 * 统计每台机器销售信息
	 * @param payRecordForm
	 * @return
	 */
	List<PerMachinesSaleDto> findPerMachinesSale(MachinesSaleStatisticsForm machinesStatisticsForm);
	
	/**
	 * 统计每台机器销售详情信息
	 * @param machinesStatisticsForm
	 * @return Page<PerMachinesSaleDto>
	 */
	public List<PerMachinesSaleDto> findPerMachinesSaleDetail(MachinesSaleStatisticsForm machinesStatisticsForm); 
	
	/**
	 * 查询机器销售总记录数
	 * @param machinesStatisticsForm
	 * @return
	 */
	Long findMachinesSaleNumRecord(MachinesSaleStatisticsForm machinesStatisticsForm);
	
	/**
	 * 统计每台机器用户消费记录
	 * @param payRecordForm
	 * @return
	 */

	ReturnDataUtil findPerMachinesCustomerConsume(MachinesSaleStatisticsForm machinesStatisticsForm);
	
	/**
	 * 统计所有机器的销售总数和平均销售量
	 * @param machinesStatisticsForm
	 * @return
	 */
	
	SumMachinesSaleDto findSumMachinesSale(MachinesSaleStatisticsForm machinesStatisticsForm);

}
