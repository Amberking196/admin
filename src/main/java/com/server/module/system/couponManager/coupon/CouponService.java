package com.server.module.system.couponManager.coupon;

import java.util.List;

import com.server.util.ReturnDataUtil;

/**
 * author name: yjr
 * create time: 2018-06-28 09:01:06
 */
public interface CouponService {


    public ReturnDataUtil listPage(CouponForm condition);

    public List<CouponBean> list(CouponForm condition);

    public ReturnDataUtil update(CouponBean entity);

    public boolean del(Object id);

    public CouponBean get(Object id);

    public ReturnDataUtil add(CouponAddVo vo);

    public ReturnDataUtil addMachine(String[] codes, Long couponId);

    public ReturnDataUtil addAllMachine(AddAllMachineForm condition);

    ReturnDataUtil addCustomer(Long[] customerIds, Long couponId);

    ReturnDataUtil addAllCustomer(AddAllCustomerForm condition);
    
    /**
     * 获取优惠券信息
     * @author why
     * @date 2018年10月9日 上午10:39:27 
     * @param couponForm
     * @return
     */
    public List<CouponBean> getPresentCoupon(CouponForm couponForm);
    
    /**
     * 亚运城用户下发优惠券
     * @author why
     * @date 2018年11月23日 上午10:06:59 
     * @return
     */
    public ReturnDataUtil addAsianCustomer();
}

