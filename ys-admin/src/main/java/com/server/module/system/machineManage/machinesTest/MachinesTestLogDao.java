package com.server.module.system.machineManage.machinesTest;

import java.util.List;

import com.server.module.commonBean.TotalResultBean;


public interface MachinesTestLogDao {

	/**
	 * 获取所有测试数据(分页)
	 * @author hebiting
	 * @date 2019年2月27日下午5:37:26
	 * @param form
	 * @return
	 */
	public TotalResultBean<List<MachinesTestLogDto>> getMachinesTest(MachinesTestForm form);
	
	/**
	 * 获取测试期间的购买测试数据
	 * @author hebiting
	 * @date 2019年2月27日上午11:30:13
	 * @param testLog
	 * @return
	 */
	public List<MachinesTestResultDto> getPurchaseTestResult(MachinesTestLogBean testLog);
	/**
	 * 获取测试期间的补货测试数据
	 * @author hebiting
	 * @date 2019年2月27日上午11:30:27
	 * @param testLog
	 * @return
	 */
	public List<MachinesTestResultDto> getReplenishTestResult(MachinesTestLogBean testLog);

	/**
	 * 根据id获取测试记录数据
	 * @author hebiting
	 * @date 2019年2月27日上午11:30:02
	 * @param id
	 * @return
	 */
	public MachinesTestLogBean getTestLog(Long id);
}
