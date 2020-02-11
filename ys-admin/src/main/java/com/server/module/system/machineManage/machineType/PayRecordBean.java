package com.server.module.system.machineManage.machineType;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.server.common.persistence.Entity;
import com.server.common.utils.excel.annotation.ExcelField;

import lombok.Data;
/**
 * table name:  pay_record
 * author name: yjr
 * create time: 2018-03-29 13:50:28
 */ 
@Data
@Entity(tableName="pay_record",id="id",idGenerate="auto")
public class PayRecordBean{


@JsonIgnore	public String tableName="pay_record";
@JsonIgnore	public String selectSql="select * from pay_record where 1=1 ";
@JsonIgnore	public String selectSql1="select id,customerId,basicItemId,itemId,payCode,vendingMachinesCode,payType,price,state,num,pickupNum,ptCode,createTime,payTime,refundTime,finishTime,costPrice,refundName,remark,itemName,itemTypeId,wayNumber from pay_record where 1=1 ";
	private BigInteger id;
	@ExcelField(title = "customerId")
	private BigInteger customerId;
	private BigInteger basicItemId;
	private BigInteger itemId;
	private String payCode;
	private String vendingMachinesCode;
	private Long payType;
	private BigDecimal price;
	private Long state;
	private Long num;
	private Long pickupNum;
	private String ptCode;
	private Date createTime;
	private Date payTime;
	private Date refundTime;
	private Date finishTime;
	private BigDecimal costPrice;
	private String refundName;
	private String remark;
	private String itemName;
	private Long itemTypeId;
	private Long wayNumber;

}

