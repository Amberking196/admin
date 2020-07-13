package com.server.module.app.payRecord;

import java.util.List;


public interface AppPayRecordDao {

	/**
	 * 查询是否有未支付订单
	 * @author hebiting
	 * @date 2018年4月26日下午3:30:28
	 * @param alipayUserId
	 * @return
	 */
	public boolean booleanIsArrearage(Long customerId);
	
	/**
	 * 根据支付编码查询交易记录
	 * @author hebiting
	 * @date 2018年4月27日下午5:43:48
	 * @param payCode
	 * @return
	 */
	public PayRecordBean findPayRecordByPayCode(String payCode);
	/**
	 * 更新交易记录
	 * @author hebiting
	 * @date 2018年4月27日下午5:44:43
	 * @param payRecord
	 * @return
	 */
	boolean updatePayRecord(PayRecordBean payRecord);
	
	/**
	 * 查询订单信息
	 * @author hebiting
	 * @date 2018年4月28日下午1:58:49
	 * @param vmCode
	 * @param customerId
	 * @param state
	 * @return
	 */
	List<PayRecordDto> findPayRecord(PayRecordForm payRecordForm);

	List<PayRecordDto> findPayRecord(String vmCode,int day);
	/**
	 * 根据id查询订单信息
	 * @author hebiting
	 * @date 2018年5月8日下午4:19:54
	 * @param payRecordId
	 * @return
	 */
	PayRecordDto findPayRecordById(Long payRecordId);
	
	/**
	 * 修改订单状态
	 * @author hebiting
	 * @date 2018年5月8日下午10:02:53
	 * @param payRecordId
	 * @param state
	 * @return
	 */
	boolean updatePayState(String ptCode,String payCode,Integer state);
	

	/**
	 * 查询记录总条数
	 * @param payRecordForm
	 * @return SumPayRecordDto记录数
	 */
	SumPayRecordDto findPayRecordNum(PayRecordForm payRecordForm);
}
