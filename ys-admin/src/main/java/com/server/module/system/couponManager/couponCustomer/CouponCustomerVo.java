package com.server.module.system.couponManager.couponCustomer;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.server.common.persistence.NotField;

import lombok.Data;


@Data
public class CouponCustomerVo{
  

	//主键id
	private Long id;
	//券id
	private Long ticketId;
	//用户id
	private Long customerId;
	//发送状态 0未发送 1 已发送
	private Integer state;
	//开始时间
	private Date startTime;
	//结束时间
	private Date endTime;
	//发券时间
	private Date createTime;
	//手机号
	private String phone;
	//售货机编号
	private String vmCode;
	//有效天数
	private Integer days;
	//券名称
	private String name;
	//剩余数量
	private Integer quantity;
	
	@JsonIgnore
	private String stateLabel;
	
	
	public String getStateLabel() {
		if(state==0) {
			stateLabel="未发送";
		}
		if(state==1) {
			stateLabel="已发送";
		}
		return stateLabel;
	}
	
	

}

