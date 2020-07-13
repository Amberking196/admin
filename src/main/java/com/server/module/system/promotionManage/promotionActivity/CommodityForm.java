package com.server.module.system.promotionManage.promotionActivity;

import lombok.Data;

@Data
public class CommodityForm {

	 	private int activityId; //活动id
	    private int useWhere; //活动应用范围 1 .机器活动 2 .商城活动
	    private int isBind; //是否绑定商品
	    private String name; //商品名
}
