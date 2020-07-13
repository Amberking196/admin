package com.server.module.system.couponManager.couponProduct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.logging.LogFactory;

import java.util.List;

import com.server.util.ReturnDataUtil;

/**
 * author name: yjr
 * create time: 2018-07-10 10:28:12
 */
@Service
public class CouponProductServiceImpl implements CouponProductService {

    private static Log log = LogFactory.getLog(CouponProductServiceImpl.class);
    @Autowired
    private CouponProductDao couponProductDaoImpl;

    public ReturnDataUtil listPage(CouponProductCondition condition) {
        return couponProductDaoImpl.listPage(condition);
    }

    @Override
    public List<CouponProductVo> list(CouponProductForm condition) {
        return couponProductDaoImpl.list(condition);
    }

    public CouponProductBean add(CouponProductBean entity) {
        return couponProductDaoImpl.insert(entity);
    }

    public boolean update(CouponProductBean entity) {
        return couponProductDaoImpl.update(entity);
    }

    public boolean del(Object id) {
        return couponProductDaoImpl.delete(id);
    }

    public List<CouponProductBean> list(CouponProductCondition condition) {
        return null;
    }

    public CouponProductBean get(Object id) {
        return couponProductDaoImpl.get(id);
    }
}

