package com.server.module.system.couponManager.couponMachine;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import com.server.util.ReturnDataUtil;

/**
 * author name: yjr
 * create time: 2018-06-28 09:11:41
 */
@Service
public class CouponMachineServiceImpl implements CouponMachineService {

    private static Log log = LogFactory.getLog(CouponMachineServiceImpl.class);
    @Autowired
    private CouponMachineDao couponMachineDaoImpl;

    public ReturnDataUtil listPage(CouponMachineForm condition) {
        return couponMachineDaoImpl.listPage(condition);
    }

    public CouponMachineBean add(CouponMachineBean entity) {
        return couponMachineDaoImpl.insert(entity);
    }

    public boolean update(CouponMachineBean entity) {
        return couponMachineDaoImpl.update(entity);
    }

    public boolean del(Integer id) {
        return couponMachineDaoImpl.delete(id);
    }

    public List<CouponMachineBean> list(CouponMachineForm condition) {
        return null;
    }

    public CouponMachineBean get(Integer id) {
        return couponMachineDaoImpl.get(id);
    }

    @Override
    public ReturnDataUtil listPageForAddMachine(CouponMachineForm condition) {
        return couponMachineDaoImpl.listPageForAddMachine(condition);
    }
}

