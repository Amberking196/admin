package com.server.module.system.machineManage.advertisingBindMachine;

import java.util.List;

import com.server.util.ReturnDataUtil;

/**
 * author name: yjr
 * create time: 2018-12-24 15:45:31
 */
public interface VendingAdvertisingMachinesDao {

    public ReturnDataUtil listPage(VendingAdvertisingMachinesCondition condition);

    public List<VendingAdvertisingMachinesBean> list(VendingAdvertisingMachinesCondition condition);

    public boolean update(VendingAdvertisingMachinesBean entity);

    public boolean delete(Object id);

    public VendingAdvertisingMachinesBean get(Object id);

    public VendingAdvertisingMachinesBean insert(VendingAdvertisingMachinesBean entity);

    public void addAll(Long advertisingId,List<String> codeList);
    public void deleteAll(Long[] ids);
}

