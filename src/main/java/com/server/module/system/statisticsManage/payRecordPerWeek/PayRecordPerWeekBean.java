package com.server.module.system.statisticsManage.payRecordPerWeek;

import com.server.common.persistence.Entity;
import com.server.common.persistence.NotField;

import lombok.Data;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonIgnore;
/**
 * table name:  tbl_statistics_pay_per_week
 * author name: hjc
 * create time: 2018-07-14 14:38:10
 */ 
@Data
@Entity(tableName="tbl_statistics_pay_per_week",id="id",idGenerate="auto")
public class PayRecordPerWeekBean{

	private Long id;
	private Integer num;//商品销售数量
	private Integer companyId;//公司id
	private String reportStartDate;//每周周一
	private String reportDate;//日期
	private Date createTime;
	private Date updateTime;
	private Integer createUser;
	private Integer updateUser;
	
	@NotField
	private String companyName;	//公司名称
}

