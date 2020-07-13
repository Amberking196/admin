package com.server.module.system.shoppingManager.customerOrder;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.server.module.commonBean.PageAssist;

public class CustomerOrderForm extends PageAssist{
	
        //订单类型
	    private Integer type;
	    //支付类型
	    private Integer payType;
		//开始时间
	    @DateTimeFormat(pattern="yyyy-MM-dd")
		private Date startDate;
		//结束时间
	    @DateTimeFormat(pattern="yyyy-MM-dd")
		private Date endDate;
		//商城订单的product
		private String product;
		//支付状态
		private Long state;
		//手机号
		private String phone;
		//收货手机号
		private String consigneePhone;
		//商品名称
		private String itemName;
		
		private Integer companyId;
		
		
		public Integer getCompanyId() {
			return companyId;
		}
		public void setCompanyId(Integer companyId) {
			this.companyId = companyId;
		}
		public String getProduct() {
			return product;
		}
		public void setProduct(String product) {
			this.product = product;
		}
		public Date getStartDate() {
			return startDate;
		}
		public void setStartDate(Date startDate) {
			this.startDate = startDate;
		}
		public Date getEndDate() {
			return endDate;
		}
		public void setEndDate(Date endDate) {
			this.endDate = endDate;
		}
		
		public Integer getType() {
			return type;
		}
		public void setType(Integer type) {
			this.type = type;
		}
		public Integer getPayType() {
			return payType;
		}
		public void setPayType(Integer payType) {
			this.payType = payType;
		}
		public Long getState() {
			return state;
		}
		public void setState(Long state) {
			this.state = state;
		}
		public String getPhone() {
			return phone;
		}
		public void setPhone(String phone) {
			this.phone = phone;
		}
		public String getConsigneePhone() {
			return consigneePhone;
		}
		public void setConsigneePhone(String consigneePhone) {
			this.consigneePhone = consigneePhone;
		}
		public String getItemName() {
			return itemName;
		}
		public void setItemName(String itemName) {
			this.itemName = itemName;
		}
		
		
}
