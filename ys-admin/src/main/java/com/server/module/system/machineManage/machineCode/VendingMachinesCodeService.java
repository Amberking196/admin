package com.server.module.system.machineManage.machineCode;

/**
 * author name: why
 * create time: 2018-04-10 15:07:28
 */ 
public interface  VendingMachinesCodeService{

	/**
	 * 根据区域id和类型编号查询生成的code
	 * @param areaNumbe,machinesTypeId
	 * @return VendingMachinesCodeBean
	 */
	public VendingMachinesCodeBean getByUnique(String areaNumber, Long machinesTypeId);
	
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

