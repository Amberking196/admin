package com.server.module.customer.product.tblCustomerSpellGroup;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class SpellOrderVO {

    private String ptCode;//订单号
    private Long groupId;//团购活动id
    private Long spellId;//拼团id
    private Long orderId;//订单id
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;//开团时间
    private String goodsName;//商品名称
    private Double money;//订单金额
    private Integer spellGroupState;//订单状态
    private Integer isHelpOneself;//提货方式
    private String nickname;//参团人昵称
    private String phone;//参团人电话
    private String spellPhone;//拼主电话
    private String pickupWay;//提货方式
    private String stateLabel;//订单状态标识
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date orderTime;//订单时间
    private Double spellGroupPrice;//拼团价格
    private Integer timeLimit;//拼团时长
    private Integer spellState;//拼团状态
    private String spellStateLabel;//拼团状态
    public String getSpellStateLabel(){
        if(spellState==null){
            return "";
        }
        if(spellState==0){
            return "失败";
        }
        if(spellState==1){
            return "成功";
        }
        if(spellState==2){
            return "拼团中";
        }
        if(spellState==3){
            return "未开始";
        }

        return "";
    }
    public String getPickupWay(){
        if(isHelpOneself==null){
            return "";
        }
        if(isHelpOneself==0){
            return "不支持自提";
        }
        if(isHelpOneself==1){
            return "提水劵自提";
        }
        return "";
    }


    public String getStateLabel(){
        if(spellGroupState==null){
            return "待付款";
        }
        if(spellGroupState==1){
            return "待分享";
        }
        if(spellGroupState==2){
            return "待取货";
        }
        if(spellGroupState==3){
            return "已关闭";
        }
        if(spellGroupState==4){
            return "已完成";
        }
        return "";
    }
}
