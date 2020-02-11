package com.server.module.system.statisticsManage.payRecordPerHour;

import com.server.common.persistence.Entity;
import com.server.common.persistence.NotField;

import lombok.Data;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonIgnore;
/**
 * table name:  tbl_statistics_pay_per_hour
 * author name: hjc
 * create time: 2018-07-13 09:23:01
 */ 
@Data
@Entity(tableName="tbl_statistics_pay_per_hour",id="id",idGenerate="auto")
public class PayRecordPerHourBean{

	private Long id;
	private Integer zero;//0点
	private Integer one;//1点
	private Integer two;
	private Integer three;
	private Integer four;
	private Integer five;
	private Integer six;
	private Integer seven;
	private Integer eight;
	private Integer nine;
	private Integer ten;
	private Integer eleven;
	private Integer twelve;
	private Integer thirteen;
	private Integer fourteen;
	private Integer fifteen;
	private Integer sixteen;
	private Integer seventeen;
	private Integer eighteen;
	private Integer nineteen;
	private Integer twenty;
	private Integer twentyone;
	private Integer twentytwo;
	private Integer twentythree;
	private String reportDate;//报表日期
	private Date createTime;
	private Date updateTime;
	private Integer createUser;
	private Integer updateUser;

	
	@NotField
	private String companyName;	//公司名称
}

