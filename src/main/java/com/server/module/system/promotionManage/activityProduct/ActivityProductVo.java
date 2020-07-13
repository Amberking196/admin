package com.server.module.system.promotionManage.activityProduct;

import lombok.Data;

@Data
public class ActivityProductVo {

	//主键id
	Integer id;
	//活动ID
	Integer activityId;
	//商品ID
	private Long productId;
	//商品名称
    private String productName;
    //是否绑定
    private String bindLabel;
}
