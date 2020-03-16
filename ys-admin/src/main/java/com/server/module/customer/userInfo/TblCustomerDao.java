package com.server.module.customer.userInfo;

public interface TblCustomerDao {
    /**
     * 根据id获取用户
     * @author hjc
     * @date 2018年10月8日下午5:10:15
     * @param customerId
     * @return
     */
    public TblCustomerBean getCustomerById(Long customerId);
}
