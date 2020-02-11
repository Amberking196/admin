package com.server.module.customer.product.bargainDetail;

import java.math.BigDecimal;

import lombok.Data;
@Data
public class BargainDetailDto {

	private String customerName;
	private String pic;
	private BigDecimal cutPrice;
	private Long customerId;
}
