package com.server.module.customer.order.storeGroupOrderManage;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class GroupOrderBean {

	//订单id
	private Long id;
	//用户拼团表主键id
	private Long customerGroupId;
	//用户id
	private Long customerId;
	//用户openId
	private String openid;
	//价格
	private BigDecimal price;
	////最小成团人数
	private Integer minimumGroupSize;
	//参与者ids
	private String participationCustomerId;
	//成团时间限制
	private Integer timeLimit;
	//商品
	private String product;
	//团长id
	private Long startCustomerId;
	//商品名称
	private String goodsName;
	//商品类型
	private Integer typeId;
	//单张提水券
	private Long vouchersId;
	//多张提水券
	private String vouchersIds;
	private Integer num;
	private String payCode;
}
