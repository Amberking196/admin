package com.server.module.system.memberManage.memberManager;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class MemberUseLog {

	//主键id
	private Long id;
	//用户id
	private Long customerId;
	//订单id
	private Long orderId;
	//使用余额
	private BigDecimal useMoney;
	//创建时间
	private Date createTime;
	//订单类型(1:机器订单,2:商城订单)
	private Integer orderType;
	
	
	
}
