package com.server.module.customer.car;

import com.server.common.persistence.Entity;
import com.server.common.persistence.NotField;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonIgnore;
/**
 * table name:  shopping_car
 * author name: hjc
 * create time: 2018-06-29 11:03:25
 */ 
@Data
@Entity(tableName="shopping_car",id="id",idGenerate="auto")
public class ShoppingCarBean{

	private Long id;
	// 顾客id
	private Long customerId;
	// 商品id
	private Integer itemId;
	// 商品名称
	private String itemName;
	// 商品单价
	private BigDecimal price;
	// 商品数量
	private Integer num;
	// 创建时间
	private Date createTime;
	// 更新时间
	private Date updateTime;
	// 删除标识
	private Integer deleteFlag;
	// 创建者id
	private Long createUser;
	// 更新者id
	private Long updateUser;
	@NotField
	private String pic;
	//商品是否支持自取  0 不支持  1  支持
	private Integer isHelpOneself;

}

