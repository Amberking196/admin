package com.server.module.customer.product.shoppingGoodsProduct;

import lombok.Data;

@Data
public class ShoppingGoodsProductVo {

	//主键id
	Integer id;
	//商城商品ID
	Integer goodId;
	//商品ID
	private Long productId;
	//商品名称
    private String productName;
    //商品数量
    private Integer quantity;
    //是否绑定
    private String bindLabel;
}
