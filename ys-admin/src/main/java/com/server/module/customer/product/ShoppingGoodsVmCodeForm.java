package com.server.module.customer.product;

import com.server.module.commonBean.PageAssist;

import lombok.Data;
@Data
public class ShoppingGoodsVmCodeForm extends PageAssist{

	private Integer areaId;
	private Integer companyId;
	private Integer isBind;//0 取消绑定  1 绑定
	private Integer id;//商品 id
	private String vmCode;
	private String address;
	private Integer target;
	private Integer type;//0 商品 1提水券 2团购机器地址
	private Integer spellGroupId;//拼团活动id
	
	private String name;//提水券名称
	private Integer typeId;//商品类型 优惠券/提水券/套餐类型
	private String carryWaterId;//提水券id  用于商品绑定提水券
}
