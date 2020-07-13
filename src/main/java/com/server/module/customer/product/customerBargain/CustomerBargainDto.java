package com.server.module.customer.product.customerBargain;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;
@Data
public class CustomerBargainDto {

	//用户id
	private Long customerId;
	//用户openId
	private String openId;
	//用户头像
	private String headimgurl;
	//商品名称
	private String itemName;
	//商品id
	private Long itemId;
	//商品图片
	private String pic;
	//商品最低价
	private BigDecimal lowestPrice;
	//商品正常售卖价
	private BigDecimal salesPrice;
	//砍价结束时间
	private Date endTime;
	//已售卖数量
	private Long buyNum;
	//当前已砍至金额
	private BigDecimal currPrice;
	//砍价商品的goods_bargain表id
	private Long goodsBargainId;
}
