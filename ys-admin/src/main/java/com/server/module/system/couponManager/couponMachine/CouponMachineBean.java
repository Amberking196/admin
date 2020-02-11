package com.server.module.system.couponManager.couponMachine;

import com.server.common.persistence.Entity;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;

/**
 * table name:  coupon_machine
 * author name: yjr
 * create time: 2018-06-28 09:11:41
 */
@Data
@Entity(tableName = "coupon_machine", id = "id", idGenerate = "auto")
public class CouponMachineBean {


    //@JsonIgnore	public String tableName="coupon_machine";
//@JsonIgnore	public String selectSql="select * from coupon_machine where 1=1 ";
//@JsonIgnore	public String selectSql1="select id,couponId,vmCode,createTime,createUser,updateTime,updateUser,deleteFlag from coupon_machine where 1=1 ";
    private Long id;
    private Long couponId;
    private String vmCode;
    private Date createTime;
    private Long createUser;
    private Date updateTime;
    private Long updateUser;
    private Integer deleteFlag;

}

