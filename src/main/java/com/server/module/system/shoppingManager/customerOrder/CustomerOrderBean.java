package com.server.module.system.shoppingManager.customerOrder;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.netflix.discovery.converters.jackson.builder.StringInterningAmazonInfoBuilder;
import com.server.common.persistence.NotField;

public class CustomerOrderBean {
	
	//序号
	private Integer num;
	// 订单Id
	private Integer orderId;
	// 平台订单号
	private String ptCode;
	// 类型 1=商城订单 2=华发商城订单
	private Integer type;
	// 订单类型名称
	private String orderName;
	// 商品名称
	private String product;
	// 商品当前价格
	private double nowprice;
	// 收货地址
	private String location;
	// 订单创建时间
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	// 订单支付时间
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date payTime;
	// 售货机编号
	private String vmCode;
	// 支付类型
	private Integer payType;
	// 支付类型
	private String payName;
	// 优惠卷
	private Integer coupon;
	// 优惠卷扣减金额
	private double couponprice;
	// 确认收货时间
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date affirmTime;
	// 支付单号
	private String payCode;
	// 代金券使用数量
	private Integer wxCouponCount;
	// 单个代金券支付金额
	private Integer wxCouponOneFee;
	// 电话
	private String consigneePhone;
	// 支付状态
	private String stateName;
	// 收货人姓名
	private String receiver;
	
	private String time;
	//订单详情
	private List<ShoppingBean> list;
	@NotField
	private String companyName;
	
	
	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public List<ShoppingBean> getList() {
		return list;
	}

	public void setList(List<ShoppingBean> list) {
		this.list = list;
	}

	public String getOrderName() {
		if (type == 1) {
			orderName = "商城订单";
		}
		if (type == 2) {
			orderName = "华发订单";
		}
		if (type == 3) {
			orderName = "商城团购订单";
		}
		return orderName;
	}

	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}

	public String getPayName() {
		if (payType == 1) {
			payName = "微信支付";
		}
		if (payType == 2) {
			payName = "支付宝支付";
		}
		if (payType == 3) {
			payName = "余额支付";
		}
		return payName;
	}

	public void setPayName(String payName) {
		this.payName = payName;
	}

	public Integer getWxCouponCount() {
		return wxCouponCount;
	}

	public void setWxCouponCount(Integer wxCouponCount) {
		this.wxCouponCount = wxCouponCount;
	}

	public Integer getWxCouponOneFee() {
		return wxCouponOneFee;
	}

	public void setWxCouponOneFee(Integer wxCouponOneFee) {
		this.wxCouponOneFee = wxCouponOneFee;
	}

	public String getPayCode() {
		return payCode;
	}

	public void setPayCode(String payCode) {
		this.payCode = payCode;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public String getPtCode() {
		return ptCode;
	}

	public void setPtCode(String ptCode) {
		this.ptCode = ptCode;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public double getNowprice() {
		return nowprice;
	}

	public void setNowprice(double nowprice) {
		this.nowprice = nowprice;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getPayTime() {
		return payTime;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

	public String getVmCode() {
		return vmCode;
	}

	public void setVmCode(String vmCode) {
		this.vmCode = vmCode;
	}

	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
	}

	public Integer getCoupon() {
		return coupon;
	}

	public void setCoupon(Integer coupon) {
		this.coupon = coupon;
	}

	public double getCouponprice() {
		return couponprice;
	}

	public void setCouponprice(double couponprice) {
		this.couponprice = couponprice;
	}

	public Date getAffirmTime() {
		return affirmTime;
	}

	public void setAffirmTime(Date affirmTime) {
		this.affirmTime = affirmTime;
	}

	

	public String getConsigneePhone() {
		return consigneePhone;
	}

	public void setConsigneePhone(String consigneePhone) {
		this.consigneePhone = consigneePhone;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	
}
