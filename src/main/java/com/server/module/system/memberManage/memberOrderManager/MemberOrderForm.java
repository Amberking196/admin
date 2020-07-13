package com.server.module.system.memberManage.memberOrderManager;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.server.module.commonBean.PageAssist;

import lombok.Data;

/**
 * table name: member_order author name: why create time: 2018-09-26 14:36:43
 */
@Data
public class MemberOrderForm extends PageAssist {

	// 支付地址
	private String url;

	// 订单id
	private Long orderId;
	// 会员电话
	private String phone;
	// 状态 10001：已支付 10002：未支付
	private Long state;

	// 开始时间
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	Date startTime;
	// 结束时间
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	Date endTime;
}
