package com.server.module.system.promotionManage.promotionActivity;

import com.server.module.commonBean.PageAssist;

import lombok.Data;
/**
 * table name:  promotion_activity
 * author name: why
 * create time: 2018-08-22 16:51:38
 */ 
@Data
public class PromotionActivityForm extends PageAssist{

	//活动名称
	String name;
	// 0 所有状态 1 已开始  2 未开始  3 已结束
	Integer state;
	//公司id
	Long companyId;
	//售货机编号
	String vmCode;
	
}

