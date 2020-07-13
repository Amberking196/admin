package com.server.module.customer.product.tblCustomerSpellGroup;

import com.server.module.commonBean.PageAssist;

import lombok.Data;

/**
 * table name: tbl_customer_spellGroup author name: why create time: 2018-10-17
 * 11:06:09
 */
@Data
public class TblCustomerSpellGroupForm extends PageAssist {

	//商品id
	private Long shoppingGoodsId;
	//用户手机号
	private String phone;
	//商品名称
	private  String productName;
	//拼团结果
	private Integer state;
	//团购活动id
	private Long spellgroupId;
	//用户id
	private Long customerId;
}
