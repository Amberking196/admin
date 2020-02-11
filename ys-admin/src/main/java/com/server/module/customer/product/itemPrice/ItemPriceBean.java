package com.server.module.customer.product.itemPrice;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;
@Data
public class ItemPriceBean {

	private Long id;
	private Long basicItemId;
	private BigDecimal price;
	private Integer deleteFlag;
	private Date createTime;
	private Long createUser;
	private Date updateTime;
	private Long updateUser;
}
