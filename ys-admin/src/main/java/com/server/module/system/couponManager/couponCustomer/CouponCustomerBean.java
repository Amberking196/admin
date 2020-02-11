package com.server.module.system.couponManager.couponCustomer;

import com.server.common.persistence.Entity;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;

/**
 * table name:  coupon_customer
 * author name: yjr
 * create time: 2018-06-28 09:15:49
 */ 
@Data
@Entity(tableName="coupon_customer",id="id",idGenerate="auto")
public class CouponCustomerBean{
   public final static int STATE_NEW=0;
	public final static int STATE_GET=1;
	public final static int STATE_USED=2;


	//@JsonIgnore	public String tableName="coupon_customer";
//@JsonIgnore	public String selectSql="select * from coupon_customer where 1=1 ";
//@JsonIgnore	public String selectSql1="select id,couponId,customerId,vmCode,state,createTime,createUser,updateTime,updateUser,deleteFlag from coupon_customer where 1=1 ";
	private Long id;
	private Long couponId;
	private Long customerId;
	private Integer state;//状态：0：未领取，1：领取，2：已使用
	private Date startTime;
	private Date endTime;
	private Date createTime;
	private Long createUser;
	private Date updateTime;
	private Long updateUser;
	private Integer deleteFlag;
	private Long quantity;  //优惠券数量

}

