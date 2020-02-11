package com.server.module.customer.coupon;

import java.util.Date;

import com.server.common.persistence.Entity;

import lombok.Data;

/**
 * table name:  coupon_customer
 * author name: yjr
 * create time: 2018-07-07 14:49:52
 */ 
@Data
@Entity(tableName="coupon_customer",id="id",idGenerate="auto")
public class CouponCustomerBean{


//@JsonIgnore	public String tableName="coupon_customer";
//@JsonIgnore	public String selectSql="select * from coupon_customer where 1=1 ";
//@JsonIgnore	public String selectSql1="select id,couponId,customerId,state,createTime,createUser,updateTime,updateUser,deleteFlag from coupon_customer where 1=1 ";
	private Long id;
	private Long couponId;
	private Long customerId;
	private Integer state;
	private Date createTime;
	private Long createUser;
	private Date updateTime;
	private Long updateUser;
	private Integer deleteFlag;

	private Date startTime;
	private Date endTime;
	private Long quantity;//用户拥有优惠券数量
	private Long sumQuantity;//下发给用户的优惠券数量
}

