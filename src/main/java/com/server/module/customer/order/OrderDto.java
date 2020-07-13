package com.server.module.customer.order;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

public class OrderDto {

	
	private Long orderId;
	private Long customerId;
	private String payCode;
	// 支付宝订单号
	private String ptCode;
	private String stateName;
	// 支付状态
	private Integer state;
	// 订单创建时间
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	// 商品名
	private String itemName;
	// 单价
	private BigDecimal price;
	// 总价
	private BigDecimal totalPrice;
	// 数量
	private Integer num;
	//用户手机号
	private String phone ;
	
	private String companyName;
	
	private String time;
	private BigDecimal nowprice;
	private Integer type;
	private BigDecimal useMoney;
	//详情集合
	private List<ShoppingBean> list=new ArrayList<ShoppingBean>();
	
	
	public String getStateName() {
		return stateName;
	}
	public void setStateName(String stateName) {
		this.stateName = stateName;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public Long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	public String getPayCode() {
		return payCode;
	}
	public void setPayCode(String payCode) {
		this.payCode = payCode;
	}
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	public String getPtCode() {
		return ptCode;
	}
	public void setPtCode(String ptCode) {
		this.ptCode = ptCode;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public BigDecimal getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	public List<ShoppingBean> getList() {
		return list;
	}
	public void setList(List<ShoppingBean> list) {
		this.list = list;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public BigDecimal getNowprice() {
		return nowprice;
	}
	public void setNowprice(BigDecimal nowprice) {
		this.nowprice = nowprice;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public BigDecimal getUseMoney() {
		return useMoney;
	}
	public void setUseMoney(BigDecimal useMoney) {
		this.useMoney = useMoney;
	}
	
	
	
}
