package com.server.module.customer.complain;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.server.module.commonBean.PageAssist;

import lombok.Data;
/**
 * table name:  customer_message
 * author name: why
 * create time: 2018-08-17 08:48:16
 */ 

@Data
public class TblCustomerComplainForm extends PageAssist{

	//售货机编号
	private String vmCode;
	//手机号
	private String phone;
	//投诉类型
	private Integer type;
	//是否回复  0 未回复 1已回复
	private Integer state;
	// 开始时间
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	Date startTime;
	// 结束时间
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	Date endTime;
}

