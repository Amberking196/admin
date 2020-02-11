package com.server.module.system.couponManager.couponMachine;

import java.util.List;

import com.server.util.ReturnDataUtil;

/**
 * author name: yjr
 * create time: 2018-06-28 09:11:41
 */
public interface CouponMachineDao {

    public ReturnDataUtil listPage(CouponMachineForm condition);

    public List<CouponMachineBean> list(CouponMachineForm condition);

    public boolean update(CouponMachineBean entity);

    public boolean delete(Integer id);

    public CouponMachineBean get(Integer id);

    public CouponMachineBean insert(CouponMachineBean entity);

    List<String> selectAllVmCodeByCoupon(Long couponId);

    ReturnDataUtil listPageForAddMachine(CouponMachineForm condition);

    List<String> allVmCodeByCompanyIdOrAreaId(String companyId, String areaId);
}

