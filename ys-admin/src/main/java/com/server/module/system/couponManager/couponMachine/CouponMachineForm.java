package com.server.module.system.couponManager.couponMachine;

import com.server.module.commonBean.PageAssist;
import lombok.Data;

/**
 * table name:  coupon_machine
 * author name: yjr
 * create time: 2018-06-28 09:11:41
 */ 
@Data
public class CouponMachineForm extends PageAssist{

    public final static int Added=1;
    public final static int NOAdded=2;



    private String companyId;
    private String areaId;
    private String vmCode;
    private int addFlag;//0 无状态  1 已添加  2 未添加

    private Integer couponId;

}

