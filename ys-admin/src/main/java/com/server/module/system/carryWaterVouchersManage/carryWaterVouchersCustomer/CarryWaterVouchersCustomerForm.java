package com.server.module.system.carryWaterVouchersManage.carryWaterVouchersCustomer;

import com.server.module.commonBean.PageAssist;

import lombok.Data;

/**
 * table name: carry_water_vouchers_customer author name: why create time:
 * 2018-11-03 16:14:12
 */
@Data
public class CarryWaterVouchersCustomerForm extends PageAssist {

	//是否发送  1 已发送  2 未发送
	 public final static int ISSend=1;
	 public final static int NOSend=2;
	
	
	//提水券id
	private Long carryId;
	//用户手机号
	private String phone;
	
	//状态 1 未使用  2 已使用   3 已过期
	private Integer state;
	
	private int isSend;//0 无状态  1 已发送  2 未发送
	private Long customerId;
}
