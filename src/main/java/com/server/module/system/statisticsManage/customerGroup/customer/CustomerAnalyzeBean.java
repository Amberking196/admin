package com.server.module.system.statisticsManage.customerGroup.customer;

import com.server.common.persistence.Entity;
import lombok.Data;
import java.util.Date;
/**
 * table name:  customer_analyze
 * author name: yjr
 * create time: 2018-10-18 09:08:12
 */ 
@Data
@Entity(tableName="customer_analyze",id="customerId",idGenerate="auto")
public class CustomerAnalyzeBean{
//@JsonIgnore	public String tableName="customer_analyze";
//@JsonIgnore	public String selectSql="select * from customer_analyze where 1=1 ";
//@JsonIgnore	public String selectSql1="select customerId,mobile,state,buyTime,buyMoney,registerTime,createTime,updateTime from customer_analyze where 1=1 ";
	private Long customerId;
	private String mobile;
	private Integer state;
	private Long buyTime;
	private Double buyMoney;
	private Date registerTime;
	private Date createTime;
	private Date updateTime;

}

