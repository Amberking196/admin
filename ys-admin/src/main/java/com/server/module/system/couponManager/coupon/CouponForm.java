package com.server.module.system.couponManager.coupon;

import com.server.module.commonBean.PageAssist;
import lombok.Data;

/**
 * table name:  coupon
 * author name: yjr
 * create time: 2018-06-28 09:01:06
 */ 
@Data
public class CouponForm extends PageAssist{

   private String name;

   private Integer state;// 0 所有状态 1 已开始  2 未开始  3 已结束

   private Integer useWhere;

   private Integer customerId;
   
   private String couponIds;
   
   private Integer way;//优惠券类型
}

