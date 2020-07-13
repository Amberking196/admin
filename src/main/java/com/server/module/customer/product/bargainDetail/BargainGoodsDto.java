package com.server.module.customer.product.bargainDetail;

import java.math.BigDecimal;

import lombok.Data;
@Data
public class BargainGoodsDto {

	private Integer companyId;
	private BigDecimal salesPrice;
	private Long id;
	private String itemName;
}
