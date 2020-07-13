package com.server.module.system.couponManager.couponProduct;

import java.util.List;

import com.server.util.ReturnDataUtil;

/**
 * author name: yjr
 * create time: 2018-07-10 10:28:12
 */
public interface CouponProductService {


    public ReturnDataUtil listPage(CouponProductCondition condition);

    public List<CouponProductBean> list(CouponProductCondition condition);

    public List<CouponProductVo> list(CouponProductForm condition);


    public boolean update(CouponProductBean entity);

    public boolean del(Object id);

    public CouponProductBean get(Object id);

    public CouponProductBean add(CouponProductBean entity);
}

