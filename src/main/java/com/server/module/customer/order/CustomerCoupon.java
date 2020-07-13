package com.server.module.customer.order;

public class CustomerCoupon {
     //优惠券名称
	private String name;
	 //金额
	private double money;
	 //减扣金额
	private double deductionMoney;
	 //优惠券id
	private Integer id;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getMoney() {
		return money;
	}
	public void setMoney(double money) {
		this.money = money;
	}
	public double getDeductionMoney() {
		return deductionMoney;
	}
	public void setDeductionMoney(double deductionMoney) {
		this.deductionMoney = deductionMoney;
	}
	
}
