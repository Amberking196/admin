package com.server.module.customer.product.bargainDetail;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class BargainDetailBean {

	private Long id;
	private Long customerBargainId;
	private Long customerId;
	private BigDecimal cutPrice;
	private Date createTime;
	
	//昵称
	private String nickName;
	//头像
	private String headimgurl;
	
}
