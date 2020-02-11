package com.server.module.system.couponManager.coupon;

import com.server.common.persistence.NotField;
import lombok.Data;

import java.util.Date;
@Data
public class CouponAddVo {
	private Long couponId;//区别shoppingGoods
    private String name;
    private Integer useWhere;// 1 机器优惠劵   2 商城优惠劵
    private Integer target;// 机器优惠劵的适用范围  1 公司  2 区域 3 机器
    private Long companyId;
    private Long areaId;
    private String companyName;
    private String areaName;
    private String vmCode;
    private Integer type;// 优惠券类型    1 满减券  2  固定券

    private Integer periodType;//有效时间类型：0 绝对时间 1 相对时间
    private Integer periodDay;// 有效天数 当periodType=1 时 该字段有意义，优惠的有效时间从领取当天now 到now+periodDay 为有效期
    //注:用户的所属优惠劵有效期统一保存到coupon_customer 的 startTime,endTime ，判断有效期统一从这里取数
    private Integer canSend;// 可以发送： 0 可以发送 1 不可以发送 不可以领取

    private Integer bindProduct;//0 不绑定  1 绑定
    private Integer way; //赠券方式 1：购买返券，2：自助领券，3：活动赠券   4：注册返券  6:关注赠券 7:会员赠券 8:邀请赠券
    private Integer sendMax;//购买返的劵数量
    private Double money;
    private Double deductionMoney;
    private Date startTime;
    private Date endTime;
    private String  remark;
    private String pic;
    private Integer formulaMode; //优惠券结算方式    0 按订单结算   1 按桶结算
  //最大优惠金额
  	private double maximumDiscount;
}
