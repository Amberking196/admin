package com.server.module.system.promotionManage.activityProduct;

import com.server.module.commonBean.PageAssist;

import lombok.Data;

/**
 * table name: activity_product author name: why create time: 2018-08-23
 * 18:07:34
 */
@Data
public class ActivityProductForm extends PageAssist {

	//活动id
	private int activityId;
	// 活动应用场景  1 机器   2 商城
	private int useWhere;
	//是否绑定商品  0未绑定  1 已绑定
	private int isBind;
	//商品名称
	private String name;

}
