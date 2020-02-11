package com.server.module.system.couponManager.couponCustomer;

import com.server.module.commonBean.PageAssist;
import lombok.Data;

/**
 * table name:  coupon_customer
 * author name: yjr
 * create time: 2018-06-28 09:15:49
 */ 
@Data
public class CouponCustomerForm extends PageAssist{
    public final static int Added=1;
    public final static int NOAdded=2;
   /* private String companyId;
    private String areaId;*/
    private String phone;
   // private String vmCode;
    private String couponId;
    private int addFlag;//0 无状态  1 已添加  2 未添加

}

