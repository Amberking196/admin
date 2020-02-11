package com.server.module.system.replenishManage.machineHistory;

import java.util.List;

import com.server.util.ReturnDataUtil;

/**
 * author name: yjr
 * create time: 2018-11-06 14:32:26
 */
public interface VendingMachineHistoryService {


    public ReturnDataUtil listPage(VendingMachineHistoryCondition condition);

    public List<VendingMachineHistoryBean> list(VendingMachineHistoryCondition condition);

    public boolean update(VendingMachineHistoryBean entity);

    public boolean del(Object id);

    public VendingMachineHistoryBean get(Object id);

    public VendingMachineHistoryBean add(VendingMachineHistoryBean entity);
    public void  updateBean(String vmCode,Integer wayNumber);
    public void  addBean(String vmCode,Integer wayNumber);

    public void  updateBean(String vmCode,Integer wayNumber,Integer basicItemId,Integer num);

}

