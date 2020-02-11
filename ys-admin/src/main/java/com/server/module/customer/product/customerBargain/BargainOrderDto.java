package com.server.module.customer.product.customerBargain;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;
@Data
public class BargainOrderDto {

	//地址名称
	private String addressName;
	//收货人名称
	private String receiverName;
	//收货人手机号码
	private String receiverPhone;
	//商品名称
	private String itemName;
	//商品图片
	private String pic;
	//商品数量
	private Integer num;
	//商品原价
	private BigDecimal price;
	//商品当前金额
	private BigDecimal nowPrice;
	//订单号
	private String payCode;	
	//支付方式
	private Integer payType;
	//支付方式名称
	private String payTypeName;
	//订单创建时间
	private Date orderCreateTime;
	//砍价开始时间
	private Date bargainStartTime;
	//订单状态
	private Integer state;
	//状态名称
	private String stateName;
	//订单id
	private Long orderId;
}
