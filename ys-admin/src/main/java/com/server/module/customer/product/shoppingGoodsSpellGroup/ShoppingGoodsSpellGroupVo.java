package com.server.module.customer.product.shoppingGoodsSpellGroup;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

/**
 * table name: shopping_goods_spellGroup author name: why create time:
 * 2018-10-16 16:41:04
 */
@Data
public class ShoppingGoodsSpellGroupVo {

	// 主键id
	private Long id;
	// 商品id
	private Long goodsId;
	// 拼团价
	private BigDecimal spellGroupPrice;
	// 最低成团人数
	private Long minimumGroupSize;
	// 团购开始时间
	private Date startTime;
	// 团购结束时间
	private Date endTime;
	// 成团时间限制
	private Integer timeLimit;
	// 创建时间
	private Date createTime;
	// 创建用户
	private Long createUser;
	// 更新时间
	private Date updateTime;
	// 更新用户
	private Long updateUser;
	// 是否删除 0 未删除 1 已删除
	private Integer deleteFlag;

	private Integer userType;

	
	// 主题内容
	private String theme;
	// 拼团显示取货地址
	private String vmCode;
	// 允许退款 商品是否支持客户申请退款  0 不支持  1 支持
	private Integer allowRefund;
	// 每次购买的件数限制 
	private Integer numberLimit;
	// 团购成功次数与每天团购次数 0代表不限制次数
	private Integer successLimit;

}
