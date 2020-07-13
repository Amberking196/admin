package com.server.module.customer.product;

import com.server.module.commonBean.PageAssist;

import lombok.Data;

/**
 * table name: shopping_goods author name: why create time: 2018-06-29 11:17:55
 */

public class ShoppingGoodsForm extends PageAssist {
	//商品名称
	String name;
	
	//类型  1 首页商品  2 广告商品  3 全部商品
	Integer type;
	
	//商品活动类型  0拼团 1普通 2砍价
	Integer  itemType;
	
	//价格 排序
	Integer price;
	//新品 排序
	Integer newProduct;
	
	//商品类型
	Integer typeId;
	
	//商品id
	Long	id;
	
	//销售状态  5100 正常销售  5101 暂停销售
	Long state;
	
	
	//商品显示位置  0 首页商品  1 广告商品 
	private Integer isHomeShow;

	// 所属公司
	private Long companyId;

	private Long goodsId;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getItemType() {
		return itemType;
	}

	public void setItemType(Integer itemType) {
		this.itemType = itemType;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public Integer getNewProduct() {
		return newProduct;
	}

	public void setNewProduct(Integer newProduct) {
		this.newProduct = newProduct;
	}

	public Integer getTypeId() {
		return typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getState() {
		return state;
	}

	public void setState(Long state) {
		this.state = state;
	}

	public Integer getIsHomeShow() {
		return isHomeShow;
	}

	public void setIsHomeShow(Integer isHomeShow) {
		this.isHomeShow = isHomeShow;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public Long getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}
	
	
}
