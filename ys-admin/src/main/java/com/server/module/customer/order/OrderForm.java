package com.server.module.customer.order;

import com.server.module.commonBean.PageAssist;

import lombok.Data;
@Data
public class OrderForm extends PageAssist{
	//商品名称
	private String product;
	//收货地址
	private String location;
	//配送方式
	private Integer distributionModel;
	//优惠券id
	private Integer coupon;
	//我的订单查询方式
	private Integer findType;
	//拼接用的
	private String ss;
	//支付地址
	private String url;
	//支付方式
	private Integer payType;
	//订单id
	private Integer orderId;
	//用户Id
	private Long customerId;
	//商城订单列表
	private String orderIdList;
	//订单金额
	private Double price;
	//购买数量
	private Integer quantity;
	//商品名称
	private String itemName;
	//参与拼团的id
	private Long customerGroupId;
	//拼团活动的id
	private Long spellGroupId;
	//用户地址id
	private Long addressId;
	///订单类型  1.商城订单  2.机器订单  用于充值
	private Integer orderType;
	///订单类型  1.商城订单  2.珠海商城订单 
	private Integer type;
	

}
