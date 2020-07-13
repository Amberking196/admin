package com.server.module.system.machineManage.machinesWay;

import java.util.List;

import com.server.module.system.machineManage.machineItem.VendingMachinesItemBean;
import com.server.util.ReturnDataUtil;

/**
 * author name: yjr create time: 2018-04-12 14:04:38
 */
public interface VendingMachinesWayDao {

	public ReturnDataUtil listPage(VendingMachinesWayCondition condition);

	public List<VendingMachinesWayBean> list(VendingMachinesWayCondition condition);

	public boolean update(VendingMachinesWayBean entity);

	public boolean delete(Object id);

	public VendingMachinesWayBean get(Object id);

	public VendingMachinesWayBean insert(VendingMachinesWayBean entity);

    public List<WayDto> listAll(String vmCode);
    
	public boolean bindItem(BindItemDto dto, VendingMachinesItemBean newItem);

    /**
     * 编辑货道及商品
     * @param dto
     */
	public boolean editWayAndItem(BindItemDto dto);
	
    /**
     * 统计货道的商品数量
     * @param condition
	 * @return ReturnDataUtil
     */
	public ReturnDataUtil statisticsWayNum(StatisticsWayNumCondition condition);

	public long selectCount(String sql, List<Object> list);



	/**
	 * 通过机器编号和货道号查询有没有具体的记录
	 */

	VendingMachinesWayBean findByVmCodeAndWayNumber(String vmCode, Integer wayNum);


	/**
	 * 查询该售货机有多少个货道
	 * @param vmCode
	 * @return
	 */
    Integer findwayNumberByVmCode(String vmCode);
    
	/**
	 * 查询机器的所有货道
	 * @param vmCode
	 * @return List<WayDto1> 
	 */
	public List<WayDto1> listAll1(String vmCode);
	
	/**
	 * 根据机器编码和货道编码查货道
	 * @param vmCode,wayNumber
	 * @return List<VendingMachinesWayBean>
	 */
	public List<VendingMachinesWayBean> listWay(String vmCode,Integer wayNumber);
	 
	/**
	 * 查询该机器商品种类
	 * @author why
	 * @date 2018年12月13日 上午11:06:49 
	 * @param vmCode
	 * @param versions
	 * @return
	 */
	 public Integer  findItemCoutItem(String vmCode,Integer versions);
}
