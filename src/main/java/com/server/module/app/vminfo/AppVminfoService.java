package com.server.module.app.vminfo;

import java.util.List;

import com.server.module.app.replenish.ReplenishForm;


public interface AppVminfoService {

	/**
	 * 根据条件查询售货机信息
	 * @author hebiting
	 * @date 2018年5月27日下午8:44:15
	 * @param condition
	 * @param companyIds
	 * @param machineType
	 * @param state
	 * @return
	 */
	List<VminfoDto> queryVMList(String condition,String companyIds,Integer machineType ,Integer state);
	
	/**
	 * 根据条件查询售货机信息(补货公司人员使用)
	 * @author hjc
	 * @date 2018年11月20日下午4:44:15
	 * @param condition
	 * @param companyIds
	 * @param machineType
	 * @param state
	 * @return
	 */
	List<VminfoDto> queryVMListForReplenishMan(String condition,String companyIds,Integer state);

	/**
	 * 根据线路负责人，查询所管辖的所有机器
	 * @author hebiting
	 * @date 2018年5月27日下午8:44:18
	 * @param condition
	 * @param dutyId
	 * @param machineType
	 * @param state
	 * @return
	 */
	List<VminfoDto> queryOwnVMList(String condition,Long dutyId,Integer machineType ,Integer state);
	/**
	 * 查询补货机器
	 * @author hebiting
	 * @date 2018年6月4日下午3:34:34
	 * @param companyIds
	 * @param dutyId
	 * @param rate
	 * @return
	 */
	public List<VminfoDto> queryReplenishVMList(ReplenishForm replenishForm);
	
	/**
	 * 查询所有机器总数
	 * @author hebiting
	 * @date 2018年6月4日下午3:36:26
	 * @return
	 */
	Integer queryAllMachinesNum(Integer companyId,String companyIds,Integer state);
	/**
	 * 查询所有机器总数(补货公司)
	 * @author hjc
	 * @date 2018年11月20日下午3:36:26
	 * @return
	 */
	Integer queryAllMachinesNumForReplenishMan(Integer companyId,String companyIds,Integer state);
	
	/**
	 * 根据vmCode查询机器信息
	 * @author hebiting
	 * @date 2018年6月5日上午8:33:54
	 * @param vmCode
	 * @return
	 */
	VminfoDto queryByVmCode(String vmCode);

    List<WayDto1> queryWayItem(List<String> listCode,int version);
}
