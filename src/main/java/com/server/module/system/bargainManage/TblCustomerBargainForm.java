package com.server.module.system.bargainManage;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.server.module.commonBean.PageAssist;
import com.server.module.customer.product.goodsBargain.GoodsBargainBean;

import lombok.Data;
/**
 * table name:  tbl_customer_bargain
 * author name: hjc
 * create time: 2018-12-24 16:20:26
 */ 
@Data
public class TblCustomerBargainForm extends PageAssist{
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createStartTime;//砍价生成开始时间
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createEndTime;//砍价生成开始时间

	private Integer state;//订单状态
	private String ptCode;//交易单号
	private String shoppingGoodsName;//商品名称
	private String phone;//手机号

	private Integer sendMessage;//是发送短信成功 0未发送 1已发送
	private List<String> phoneList;//部分发送手机
	private List<Integer> idList;//活动ids
	private Integer id;//活动id

	private String content;//短信内容
	
	private Integer customerBargainId;//报名表id
}

