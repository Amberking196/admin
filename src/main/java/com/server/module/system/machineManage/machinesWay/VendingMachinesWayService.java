package com.server.module.system.machineManage.machinesWay;

import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;

import com.server.util.ReturnDataUtil;

/**
 * author name: yjr create time: 2018-04-12 14:04:38
 */
public interface VendingMachinesWayService {

	public ReturnDataUtil listPage(VendingMachinesWayCondition condition);

	public List<VendingMachinesWayBean> list(VendingMachinesWayCondition condition);

	public boolean update(VendingMachinesWayBean entity);

	public boolean del(Object id);

	public VendingMachinesWayBean get(Object id);

	public VendingMachinesWayBean add(VendingMachinesWayBean entity);
    
	public List<WayDto> listAll(String vmCode);
	
	public List<WayDto> listAllForExport(String vmCode);
	
	public ReturnDataUtil bindItem(BindItemDto dto);
	
	public ReturnDataUtil editWayAndItem(BindItemDto dto);
	
	public ReturnDataUtil statisticsWayNum(StatisticsWayNumCondition condition);

	public ReturnDataUtil checkAdd(VendingMachinesWayBean entity);

	/**
	 * 通过机器编号和货道号查询有没有具体的记录
	 */

	VendingMachinesWayBean findByVmCodeAndWayNumber(String vmCode, Integer wayNum);

	/**
	 * 查询该售货机有多少个货道
	 *
	 * @param vmCode
	 * @return
	 */
	Integer findwayNumberByVmCode(String vmCode);
	
	public List<VendingMachinesWayBean> listWay(String vmCode,Integer wayNumber);//根据vmcode  货道编码查货道

}
