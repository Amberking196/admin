package com.server.module.customer.product.customerBargain;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;
@Data
public class CustomerBargainBean {

	//id
	private Long id;
	//活动id（goods_bargain表id）
	private Long goodsBargainId;
	//报名人id
	private Long customerId;
	//砍后价格
	private BigDecimal currPrice;
	//是否砍价成功 0失败 1成功 2砍价中
	private Integer state;
	//地址表id
	private Long addressId;
	//结束时间
	private Date endTime;
	//创建时间
	private Date createTime;
	//更新时间
	private Date updateTime;
	//是否发送
	private Integer sendMessage;
	
}
