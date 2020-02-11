package com.server.module.system.couponManager.couponCustomer;

import java.util.List;

import com.google.common.collect.Lists;

import lombok.Data;

@Data
public class CouponCustomerDto {

	private Long customerId;
	private String vmCode;
	private String phone;
	private String stateLabel;
	private List<CouponCustomerVo> list=Lists.newArrayList();;
}
