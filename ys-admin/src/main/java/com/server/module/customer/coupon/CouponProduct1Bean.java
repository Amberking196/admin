package com.server.module.customer.coupon;

import java.util.Date;

import com.server.common.persistence.Entity;

import lombok.Data;

/**
 * table name:  coupon_product
 * author name: yjr
 * create time: 2018-07-10 19:12:50
 */ 
@Data
@Entity(tableName="coupon_product",id="",idGenerate="auto")
public class CouponProduct1Bean {


	private Long id;
	private Long couponId;
	private Long productId;
	private Date createTime;
	private Long createUser;
	private Date updateTime;
	private Long updateUser;
	private Integer deleteFlag;

}

