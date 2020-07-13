package com.server.module.system.replenishManage.machineHistory;

import java.util.List;

import com.server.util.ReturnDataUtil;

/**
 * author name: yjr
 * create time: 2018-11-06 14:32:26
 */
public interface VendingMachineHistoryDao {

    public ReturnDataUtil listPage(VendingMachineHistoryCondition condition);
    public ReturnDataUtil listPageGroupByItemId(VendingMachineHistoryCondition condition);

    
    public List<VendingMachineHistoryBean> list(VendingMachineHistoryCondition condition);

    public boolean update(VendingMachineHistoryBean entity);

    public boolean delete(Object id);

    public VendingMachineHistoryBean get(Object id);

    public VendingMachineHistoryBean insert(VendingMachineHistoryBean entity);

    public void  updateBean(String vmCode,Integer wayNumber);
    public void  addBean(String vmCode,Integer wayNumber);

    public void  updateBean(String vmCode,Integer wayNumber,Integer basicItemId,Integer num);
    
    
    public List<VendingMachineHistoryBean> saleNumNowGroupByItem(VendingMachineHistoryCondition condition);
    public List<VendingMachineHistoryBean> replenishNumNowGroupByItem(VendingMachineHistoryCondition condition);
    
    public List<VendingMachineHistoryBean> saleNumNowGroupByVmCode(VendingMachineHistoryCondition condition);
    public List<VendingMachineHistoryBean> replenishNumNowGroupByVmCode(VendingMachineHistoryCondition condition);

}

