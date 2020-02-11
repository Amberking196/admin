package com.server.module.customer.order;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.server.common.persistence.NotField;
import com.server.module.customer.product.spellGroupShare.SpellGroupCustomerBean;

import lombok.Data;

/**
 * 
 * @author why
 * @date: 2019年1月14日 下午5:14:56
 */
@Data
public class ShoppingDto {

	//订单id
	private Long orderId;
	//订单号
	private String payCode;
	//实付价
	private BigDecimal newPrice;
	//销售价
	private BigDecimal price;
	//用户拼团id
	private Long customerGroupId;
	//商品id
	private Long itemId;
	//商品名称
	private String itemName;
	//数量
	private Integer num;
	//商品图片
	private String pic;
	//商品团购活动id
	private Long spellGroupId;
	//是否支持退款  0 不支持  1 支持
	private Integer allowRefund;
	//拼团状态
	private Integer spellgroupState;
	//状态
	private Long state;
	//团购最多人数
	private Integer minimumGroupSize;
	//平台订单号
	private String ptCode;
	//支付类型
	private Integer payType;
	
	
	private List<SpellGroupCustomerBean> customerSpellGroupList=new ArrayList<>();
	
	//判断是否是拼团发起人  0是发起人  1 参与者
	private Integer isInitiator;
}
