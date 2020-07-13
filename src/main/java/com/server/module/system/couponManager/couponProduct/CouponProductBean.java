package com.server.module.system.couponManager.couponProduct;

import com.server.common.persistence.Entity;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;

/**
 * table name:  coupon_product
 * author name: yjr
 * create time: 2018-07-10 10:28:12
 */ 
@Data
@Entity(tableName="coupon_product",id="id",idGenerate="auto")
public class CouponProductBean{


//@JsonIgnore	public String tableName="coupon_product";
//@JsonIgnore	public String selectSql="select * from coupon_product where 1=1 ";
//@JsonIgnore	public String selectSql1="select id,couponId,productId,createTime,createUser,updateTime,updateUser,deleteFlag from coupon_product where 1=1 ";
	private Long id;
	private Long couponId;
	private Long productId;
	private Date createTime;
	private Long createUser;
	private Date updateTime;
	private Long updateUser;
	private Integer deleteFlag;

}

