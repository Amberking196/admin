package com.server.module.system.statisticsManage.machinesSaleStatistics;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.server.module.system.statisticsManage.payRecord.PayRecordBean;

public class PerMachinesCustomerConsumeDto {
	  //消费者的名字
      private String UserName;
      //消费金额
      private BigDecimal money;
	  //价格（元）
	  private BigDecimal price;
	  //数量
	  private Integer num;
	  //订单支付时间
	  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08")
	  private Date payTime;
	  //商品名称
	  private String itemName;
	  //售货机标识
	  private String vendingMachinesCode;
	  //售货机所属公司
	  private String companyName;
      
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	public Date getPayTime() {
		return payTime;
	}
	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getVendingMachinesCode() {
		return vendingMachinesCode;
	}
	public void setVendingMachinesCode(String vendingMachinesCode) {
		this.vendingMachinesCode = vendingMachinesCode;
	}
	public BigDecimal getMoney() {
		return money;
	}
	public void setMoney(BigDecimal money) {
		this.money = money;
	}
	public String getUserName() {
		return UserName;
	}
	public void setUserName(String userName) {
		UserName = userName;
	}
}
