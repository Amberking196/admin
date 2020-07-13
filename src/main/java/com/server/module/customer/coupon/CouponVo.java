package com.server.module.customer.coupon;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.server.common.persistence.NotField;
import lombok.Data;

import java.util.Date;

@Data
public class CouponVo {

    private Long id;
    private String name;//券名称
    private Integer type;// 优惠券类型    1 满减券  2  固定券
    private Integer way;//赠券方式 1：购买返券，2：自助领券，3：活动赠券
    private Integer useWhere;// 1 机器优惠劵   2 商城优惠劵
    private Integer target;// 机器优惠劵的适用范围  1 公司  2 区域 3 机器
    private Long companyId;
    private String companyName;
    private Long areaId;
    private String areaName;
    private String vmCode;
    private Integer sendMax;//购买返的劵数量
    private Double money;//满X元  满减券不需要设置
    private Double deductionMoney;//优惠金额
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date startTime;//开始时间
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date endTime;//结束时间
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    private Long createUser;
    private Date updateTime;
    private Long updateUser;
    private Integer deleteFlag;
    private String  remark;
    private String pic;
    private Integer bindProduct;
    private Integer formulaMode; //优惠券结算方式    0 按订单结算   1 按桶结算
    @NotField
    private Integer state;//状态 用户优惠劵状态
    @NotField
    private String stateLabel;// 已开始 或 已结束 未开始
    @NotField
    private String typeLabel;
    @NotField
    private String wayLabel;
    @NotField
    private String useWhereLabel;
    @NotField
    private String targetLabel;
    @NotField
    private Long[] productIds;

    public String getUseWhereLabel() {
        if(useWhere==1)
            useWhereLabel="机器优惠劵";
        if(useWhere==2)
            useWhereLabel="商城优惠劵";
        return useWhereLabel;
    }

    public String getTargetLabel() {
        // 1 公司  2 区域 3 机器
        if(target==0)
            targetLabel="";
        if(target==1)
            targetLabel="公司";
        if(target==2)
            targetLabel="区域";
        if(target==3)
            targetLabel="机器";
        return targetLabel;
    }

    public String getStateLabel() {

        return getStateByDate(getStartTime(),getEndTime());
    }
    private String getStateByDate(Date startTime,Date endTime){
        Long now=System.currentTimeMillis();
        if(now <startTime.getTime()){
            return "未开始";
        }
        if(now >endTime.getTime()){
            return "已结束";
        }
        if(now >startTime.getTime() && now<endTime.getTime()){
            return "已开始";
        }
        return "";

    }
    public String getWayLabel() {
        if(way==1){
            return "购买返券";
        }
        if(way==2){
            return "自助领券";
        }
        if(way==3){
            return "活动赠券";
        }        
        if(way==4){
            return "注册返券";
        }
		if(way==6) {
			return "关注赠券";
		}
		if(way==7) {
			return "会员赠券";
		}
		if(way==8) {
			return "邀请赠券";
		}
        return "";
    }

    public String getTypeLabel() {
        if(type==1){
            typeLabel="满减券";
        }
        if(type==2){
            typeLabel="固定券";
        }
        return typeLabel;
    }

}
