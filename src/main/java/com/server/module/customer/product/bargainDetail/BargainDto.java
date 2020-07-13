package com.server.module.customer.product.bargainDetail;

import java.math.BigDecimal;

import lombok.Data;
@Data
public class BargainDto {

	private BigDecimal lowestPrice;
	private BigDecimal oneBargainPrice;
	private BigDecimal currPrice;
	
}
