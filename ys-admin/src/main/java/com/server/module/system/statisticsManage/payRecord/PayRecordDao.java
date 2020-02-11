package com.server.module.system.statisticsManage.payRecord;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.server.util.ReturnDataUtil;

public interface PayRecordDao {

	/**
	 * 查询销售记录
	 * @param payRecordForm
	 * @return List<PayRecordBean> 销售记录集合
	 */
	List<PayRecordDto> findPayRecord(PayRecordForm payRecordForm);
	
	List<PayRecordDto> findVisionPayRecord(PayRecordForm payRecordForm);
	/**
	 * 查询记录总条数
	 * @param payRecordForm
	 * @return SumPayRecordDto记录数
	 */
	SumPayRecordDto findPayRecordNum(PayRecordForm payRecordForm);
	
	SumPayRecordDto findVisionPayRecordNum(PayRecordForm payRecordForm);

	/**
	 * 删除销售记录PayRecord
	 * @param payRecordIds
	 * @return boolean 
	 */
	boolean deletePayRecord(List<Integer> payRecordIds);
	
	/**
	 * 更新销售的订单状态
	 * @return
	 */
	boolean updateOrderState(Integer state,Integer id,Integer orderType);
	
	public ReturnDataUtil listSellNumStatisticsPage(PayRecordForm1 condition);
	

	/**
	 * 查询支付失败总价
	 * @return
	 */
	public Double findFailurePrice(PayRecordForm payRecordForm);
	public Double findVisionFailurePrice(PayRecordForm payRecordForm);

	/**
	 * 查询售货机开关门记录
	 * @param customerMachineForm
	 * @return ReturnDataUtil
	 */
	ReturnDataUtil findMachineHistory(CustomerMachineForm customerMachineForm);

	public List<Map<String,Object>> getBefore7Day(String companySqlStr, String before7Day );

	public List<Map<String,Object>> getToday(String companySqlStr,String todayStr );
	public List<Map<String,Object>> getYesterday(String companySqlStr,String yesterdayStr );
	public List<Map<String,Object>> getMonth(String companySqlStr,String monthStr );

	/**
	 * 销售纪录详情
	 * @return
	 */
	public ReturnDataUtil payRecordDetail(PayRecordForm payRecordForm);
	/**
	 * 销售记录 导出展开
	 * @return
	 */
	public ReturnDataUtil findPayRecordForExport(PayRecordForm payRecordForm);

	public ReturnDataUtil listSaleNum(ListSaleNumCondition condition);
	
	public List<PayRecordItemDto> getPayRecordItemList(String payCode);
	
	public WeightNumDto getOrderWeightNum(String payCode);

	public NumDto getOrderNumChange(NumDto numInfo,String vmCode,Integer wayNumber,String createTime); 
	
	public WeightNumDto getOrderWeightNumVision(String payCode);

	public NumDto getOrderNumChangeVision(NumDto numInfo,String vmCode,Integer wayNumber,String createTime); 
}
