package com.server.module.customer.product.shoppingGoodsProduct;

import com.server.module.commonBean.PageAssist;

import lombok.Data;

/**
 * table name: shopping_goods_product author name: why create time: 2018-09-14
 * 09:53:47
 */

@Data
public class ShoppingGoodsProductForm extends PageAssist {

	// 商城商品id
	private int goodsId;
	// 是否绑定商品 0未绑定 1 已绑定
	private int isBind;
	// 商品名称
	private String name;
	//公司id
	private Integer companyId;
	//售货机编号
	private String code;
}
