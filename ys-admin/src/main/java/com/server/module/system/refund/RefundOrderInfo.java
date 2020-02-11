package com.server.module.system.refund;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class RefundOrderInfo {

	private String ptCode;
	private BigDecimal price;
	private Integer payType;
	private String vmCode;
	private Integer companyId;
	private Long customerId;
	private Long friendCustomerId;
	private Integer num;

}
