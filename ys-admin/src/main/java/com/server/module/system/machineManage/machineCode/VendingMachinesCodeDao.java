package com.server.module.system.machineManage.machineCode;

/**
 * author name: why
 * create time: 2018-04-10 15:07:28
 */ 
public interface  VendingMachinesCodeDao{

	/**
	 *  查询生成的code 是否存在
	 * @param areaNumber
	 * @param machinesTypeId
	 * @return
	 */
	public VendingMachinesCodeBean getByUnique(String areaNumber,Long machinesTypeId);
	
	public VendingMachinesCodeBean insert(VendingMachinesCodeBean bean);
	
	public boolean update(VendingMachinesCodeBean bean);
	
	/**
	 * 根据vmCode获取出厂编号
	 * @author hebiting
	 * @date 2018年10月26日上午11:35:28
	 * @param vmCode
	 * @return
	 */
	public String getFactoryNumByVmCode(String vmCode);
}

