package com.server.module.customer.order;

import lombok.Data;

@Data
public class ShoppingBean {
	
     //商品id
	private Integer itemId;
	 //商品名称
	private String itemName;
	 //商品价格
	private Double price;
	 //商品数量
	private Integer num;
	 //商品图片
	private String pic;
	private Long orderId;
	
}
