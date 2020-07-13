package com.server.module.customer.product.shoppingGoodsSpellGroup;

import com.server.common.persistence.Entity;
import com.server.common.persistence.NotField;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
/**
 * table name:  shopping_goods_spellGroup
 * author name: why
 * create time: 2018-10-16 16:41:04
 */ 
@Data
@Entity(tableName="shopping_goods_spellGroup",id="id",idGenerate="auto")
public class ShoppingGoodsSpellGroupBean{

	//主键id
	private Long id;
	//商品id
	private Long goodsId;
	//拼图价
	private BigDecimal spellGroupPrice;
	//最低成团人数
	private Long minimumGroupSize;
	//团购开始时间
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date startTime;
	//团购结束时间
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date endTime;
	//成团时间限制
	private Integer timeLimit;
	//创建时间
	private Date createTime;
	//创建用户
	private Long createUser;
	//更新时间
	private Date updateTime;
	//更新用户
	private Long updateUser;
	//是否删除 0 未删除  1 已删除
	private Integer deleteFlag;
	
	
	// 主题内容
	private String theme;
	// 拼团显示取货地址
	private String vmCode;
	// 允许退款 商品是否支持客户申请退款  0 不支持  1 支持
	private Integer allowRefund;
	// 每次购买的件数限制 
	private Integer numberLimit;
	// 团购成功次数与每天团购次数 0代表不限制次数
	private Integer successLimit;
	//用户团购类型 0代表不限制 1新用户 2老用户  3.必须有一名新用户
	private Integer userType;
	
	
	//团购时间状态
	@NotField
	private String  stateLabel;
	
	
	//开始结束时间  不显示时分秒
	@NotField
	private Date startTimeLabel;
	@NotField
	private Date endTimeLabel;
	
	
	public String getStateLabel(){
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

}

