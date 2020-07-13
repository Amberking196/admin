package com.server.module.customer.coupon;

import com.server.module.commonBean.PageAssist;

import lombok.Data;

/**
 * table name:  coupon
 * author name: yjr
 * create time: 2018-07-06 14:05:39
 */ 
@Data
public class CouponCondition extends PageAssist{

     // private int useWhere;
      private int state;// 0 待领取  1 未使用  2 已使用

}

