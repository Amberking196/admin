package com.server.module.customer.userInfo.userWxInfo;

import com.server.common.persistence.Entity;
import com.server.common.persistence.NotField;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * table name: tbl_customer_wx author name: why create time: 2018-11-15 15:05:14
 */
@Data
@Entity(tableName = "tbl_customer_wx", id = "id", idGenerate = "auto")
public class TblCustomerWxBean {

	//主键id
	private Long id;
	//公司id
	private Long companyId;
	//用户id
	private Long customerId;
	//openID
	private String openId;
	//微信名
	private String nickname;
	//性别
	private Long sex;
	//省
	private String province;
	//市
	private String city;
	//国家
	private String country;
	//微信头像
	private String headimgurl;
	private String privilege;
	private String unionid;
	private Date createTime;
	private Date updateTime;
	

	@NotField
	private String phone;
	@NotField
	private BigDecimal userBalance;
	@NotField
	private Integer integral;
	

}
