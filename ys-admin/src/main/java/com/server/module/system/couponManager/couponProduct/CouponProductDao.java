package com.server.module.system.couponManager.couponProduct;

import java.util.List;

import com.server.util.ReturnDataUtil;

/**
 * author name: yjr
 * create time: 2018-07-10 10:28:12
 */
public interface CouponProductDao {

    public ReturnDataUtil listPage(CouponProductCondition condition);

    public List<CouponProductBean> list(CouponProductCondition condition);

    public boolean update(CouponProductBean entity);

    public boolean delete(Object id);

    public CouponProductBean get(Object id);

    public CouponProductBean insert(CouponProductBean entity);

    List<CouponProductVo> list(CouponProductForm condition);
}

