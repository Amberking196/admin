package com.server.module.customer.order;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.server.common.persistence.Entity;
import com.server.common.persistence.NotField;
import com.server.module.customer.product.spellGroupShare.SpellGroupCustomerBean;

import lombok.Data;

@Data
@Entity(tableName = "store_order", id = "id", idGenerate = "auto")
public class OrderBean {
	// 订单id
	private long id;
	// 平台订单号
	private String ptCode;
	// 订单类型 1：商城订单；2：售货机订单
	private Integer type;
	// 状态
	private Integer state;
	// 商品
	private String product;
	// 原价格
	private BigDecimal price;
	// 现在价格
	private BigDecimal nowprice;
	// 收货地址
	private String location;
	// 订单创建时间
	private Date createTime;
	// 订单支付时间
	private Date payTime;
	// 发货时间
	private Date deliverTime;
	// 确认收货时间
	private Date affirmTime;
	// 退款时间
	private Date refundTime;
	// 售货机编号
	private String vendingMachinesCode;
	// 用户id
	private String openid;
	// 配送方式
	private Integer distributionModel;
	// 优惠券
	private Integer coupon;
	// 优惠券扣减金额
	private BigDecimal couponPrice;
	// 支付类型
	private Integer payType;
	// ip
	private String ip;
	// 微信优惠券Fee
	private Integer wxCouponFee;
	// 微信代金券使用数量
	private Integer wxCouponCount;
	// 微信充值代金券
	private Integer wxCouponType;
	// 微信代金券id
	private String wxCouponId;
	// 单个代金券支付金额
	private Integer wxCouponOneFee;
	// 支付订单号
	private String payCode;
	// 顾客id
	private Long customerId;
	// 参与拼团的id
	private Long customerGroupId;
	// 用户地址id
	private Long addressId;
	//公司id
	private Long companyId;
	//支付状态
	@NotField
	private String stateName;
	//支付状态
	@NotField
	private String time;
	private BigDecimal useMoney;
	//商品详情集合
	@NotField
	private List<ShoppingBean> list;
	//砍价id
	private Long customerBargainId;
	//拼团状态 1.待分享 2.待取货  3.已关闭 4.已完成
	private  Integer spellgroupState;
	//几人团
	@NotField
	private Integer peopleNum;
	//还差几人成团
	@NotField
	private Integer residueNum;

	//数量
	private Integer num;
	
	//是否支持退款 0 不支持  1 支持
	@NotField
	private Integer allowRefund;
	
	//拼团用户集合
	@NotField
	private List<SpellGroupCustomerBean> customerSpellGroupList=new ArrayList<>();
	//判断是否是拼团发起人  0是发起人  1 参与者
 	@NotField
	private Integer isInitiator;
}
