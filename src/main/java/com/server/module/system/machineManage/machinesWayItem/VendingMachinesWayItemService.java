package com.server.module.system.machineManage.machinesWayItem;

import java.util.List;

import com.server.util.ReturnDataUtil;

/**
 * author name: yjr
 * create time: 2018-08-31 14:03:10
 */
public interface VendingMachinesWayItemService {


    public ReturnDataUtil listPage(VendingMachinesWayItemCondition condition);

    public List<VendingMachinesWayItemBean> list(VendingMachinesWayItemCondition condition);

    public ReturnDataUtil update(WayItemForUpdateVo vo);

    public ReturnDataUtil del(Long id);

    public VendingMachinesWayItemBean get(Object id);

    public ReturnDataUtil add(VendingMachinesWayItemBean entity);
    
    /**
     * 货道商品缺货比例>rate的机器
     * @param condition
     * @return ReturnDataUtil
     */
    public ReturnDataUtil listPageForReplenish(VendingMachinesWayItemCondition condition);

    public VendingMachinesWayItemBean findItemBean( String vmCode );

    //修改视觉机器的最大容量
    public ReturnDataUtil edit( VendingMachinesWayItemBean bean );
}

