package com.server.module.system.bargainManage;

import com.server.common.persistence.Entity;
import com.server.common.persistence.NotField;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
/**
 * table name:  tbl_customer_bargain
 * author name: hjc
 * create time: 2018-12-21 16:20:26
 */ 
@Data
@Entity(tableName="tbl_customer_bargain_detail",id="",idGenerate="auto")
public class TblCustomerBargainDetailBean{
	  private Integer id;
	  private Integer customerBargainId;
	  private Long customerId;
	  private BigDecimal cutPrice;//每次砍价的金额
	  @JsonFormat(pattern ="yyyy-MM-dd HH:mm:ss")
	  private Date createTime;
	  
	  @NotField
	  private String phone;
	  @NotField
	  private String name;

}

