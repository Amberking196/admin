package com.server.module.customer.product.itemPrice;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;
@Data
public class ItemPriceDto {

	private Long id;
	private Long basicItemId;
	private String itemName;
	private BigDecimal price;
	private Integer deleteFlag;
	private Date createTime;
	private String createName;
	private Date updateTime;
	private String updateName;
}
