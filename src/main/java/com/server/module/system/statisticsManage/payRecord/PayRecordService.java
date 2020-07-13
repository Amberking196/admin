package com.server.module.system.statisticsManage.payRecord;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestBody;

import com.server.util.ReturnDataUtil;

public interface PayRecordService {
	
	/**
	 * 查询销售记录
	 * @param payRecordForm
	 * @return ReturnDataUtil 销售记录集合
	 */
	ReturnDataUtil findPayRecord(PayRecordForm payRecordForm);
	
	/**
	 * 查询销售记录
	 * @param payRecordForm
	 * @return ReturnDataUtil 销售记录集合
	 */
	ReturnDataUtil findVisionPayRecord(PayRecordForm payRecordForm);
	
	/**
	 * 删除销售记录
	 * @param ids
	 * @return
	 */
	ReturnDataUtil deletePayRecord(List<Integer> payRecordIds);
	
	/**
	 * 更新销售的订单状态
	 * @return
	 */
	ReturnDataUtil updateOrderState(Integer state,Integer id,Integer orderType);

	public ReturnDataUtil listSellNumStatisticsPage(PayRecordForm1 condition);

	/**
	 * 查询售货机开关们历史记录
	 * @param customerMachineForm
	 * @return ReturnDataUtil
	 */
	ReturnDataUtil findMachineHistory(CustomerMachineForm customerMachineForm);
	ReturnDataUtil findMachineHistoryVision(CustomerMachineForm customerMachineForm);

	/**
	 * 最近七天销售记录
	 * @return
	 */
	public Map<String,Object> payBefore7Day(String companyId);
	
	/**
	 * 销售纪录详情
	 * @return
	 */
	public ReturnDataUtil payRecordDetail(PayRecordForm payRecordForm);
	
	/**
	 * 查询销售记录 详情展开
	 * @param payRecordForm
	 * @return ReturnDataUtil 销售记录集合
	 */
	ReturnDataUtil findPayRecordForExport(PayRecordForm payRecordForm);

	public ReturnDataUtil listSaleNum(ListSaleNumCondition condition);

	public List<PayRecordItemDto> getPayRecordItemList(String payCode);

}
