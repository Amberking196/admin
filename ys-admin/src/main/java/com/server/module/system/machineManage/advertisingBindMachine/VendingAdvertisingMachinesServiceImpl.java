package com.server.module.system.machineManage.advertisingBindMachine;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.logging.LogFactory;

import java.util.List;

import com.server.util.ReturnDataUtil;

/**
 * author name: yjr
 * create time: 2018-12-24 15:45:31
 */
@Service
public class VendingAdvertisingMachinesServiceImpl implements VendingAdvertisingMachinesService {

    private static Log log = LogFactory.getLog(VendingAdvertisingMachinesServiceImpl.class);
    @Autowired
    private VendingAdvertisingMachinesDao vendingAdvertisingMachinesDaoImpl;

    public ReturnDataUtil listPage(VendingAdvertisingMachinesCondition condition) {
        return vendingAdvertisingMachinesDaoImpl.listPage(condition);
    }

    public VendingAdvertisingMachinesBean add(VendingAdvertisingMachinesBean entity) {
        return vendingAdvertisingMachinesDaoImpl.insert(entity);
    }

    public boolean update(VendingAdvertisingMachinesBean entity) {
        return vendingAdvertisingMachinesDaoImpl.update(entity);
    }

    public boolean del(Object id) {
        return vendingAdvertisingMachinesDaoImpl.delete(id);
    }

    public List<VendingAdvertisingMachinesBean> list(VendingAdvertisingMachinesCondition condition) {
        return vendingAdvertisingMachinesDaoImpl.list(condition);
    }

    public VendingAdvertisingMachinesBean get(Object id) {
        return vendingAdvertisingMachinesDaoImpl.get(id);
    }


    @Override
    public void addAll(Long advertisingId, List<String> codeList) {
        vendingAdvertisingMachinesDaoImpl.addAll(advertisingId,codeList);
    }

    @Override
    public void deleteAll(Long[] ids) {
        vendingAdvertisingMachinesDaoImpl.deleteAll(ids);
    }
}

