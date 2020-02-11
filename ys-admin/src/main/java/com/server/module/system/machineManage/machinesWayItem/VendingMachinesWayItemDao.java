package com.server.module.system.machineManage.machinesWayItem;

import java.util.List;

import com.server.module.system.machineManage.machinesWay.WayItem;
import com.server.util.ReturnDataUtil;

/**
 * author name: yjr create time: 2018-08-31 14:03:10
 */
public interface VendingMachinesWayItemDao {

	public ReturnDataUtil listPage(VendingMachinesWayItemCondition condition);

	public List<VendingMachinesWayItemBean> list(VendingMachinesWayItemCondition condition);

	public boolean update(VendingMachinesWayItemBean entity);

	public boolean delete(Long id);
	
	public boolean deleteSQL(Long id);

	public VendingMachinesWayItemBean get(Object id);

	public VendingMachinesWayItemBean insert(VendingMachinesWayItemBean entity);

	public List<WayItem> listWayItem(Long wayId);

	public List<VendingMachinesWayItemBean> list(Long wayId);
	
    /**
     * 货道商品缺货比例>rate的机器
     * @param condition
     * @return ReturnDataUtil
     */
	public ReturnDataUtil listPageForReplenish(VendingMachinesWayItemCondition condition);
	
	//public List<ReplenishItemVo> listForReplenish(List<String> codes);
    /**
     * 货道商品缺货比例
     * @param List<String> codes,Integer type
     * @return List<ReplenishItemVo>
     */
	public List<ReplenishItemVo> listForReplenish(List<String> codes,Integer type);

	VendingMachinesWayItemBean findItemBean( String vmCode );

	//修改视觉机器的最大容量
	ReturnDataUtil edit( VendingMachinesWayItemBean bean );
}
