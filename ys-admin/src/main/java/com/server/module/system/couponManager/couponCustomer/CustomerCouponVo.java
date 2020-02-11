package com.server.module.system.couponManager.couponCustomer;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.server.common.persistence.NotField;
import lombok.Data;

import java.util.Date;

@Data
public class CustomerCouponVo {
    //vmCode`,c.state,cp.name,cp.money,
    // cp.deductionMoney,cp.startTime,cp.endTime,cp.type,cp.way
    private Integer state;

    private String name;

    private Double money;

    private Double deductionMoney;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    private Integer type;
    private Double maximumDiscount; 

    private Integer way;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;


    @NotField
    private String stateLabel;// 使用状态
    @NotField
    private String typeLabel;
    @NotField
    private String wayLabel;

    @NotField
    private String couponStateLabel;
//	private Integer state;//状态：0：未领取，1：领取，2：已使用
    public String getStateLabel() {
        if(state==0){
            stateLabel="未领取";
        }
        if(state==1){
            stateLabel="已领取";
        }
        if(state==2){
            stateLabel="已使用";
        }
        return stateLabel;

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
        if(type==3){
            typeLabel="折扣券";
        }
        if(type==4){
            typeLabel="固定折扣券";
        }
        return typeLabel;
    }

    public String getCouponStateLabel() {


        return getStateByDate(startTime,endTime);
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
}
