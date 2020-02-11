package com.server.module.system.machineManage.machinesWayItem;

import com.server.common.persistence.Entity;
import com.server.common.persistence.NotField;
import com.server.common.persistence.NullField;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;

/**
 * table name:  vending_machines_way_item
 * author name: yjr
 * create time: 2018-08-31 14:03:10
 */ 
@Data
@Entity(tableName="vending_machines_way_item",id="id",idGenerate="auto")
public class VendingMachinesWayItemBean{


//@JsonIgnore	public String tableName="vending_machines_way_item";
//@JsonIgnore	public String selectSql="select * from vending_machines_way_item where 1=1 ";
//@JsonIgnore	public String selectSql1="select id,machineWayId,vmCode,wayNumber,basicItemId,price,num,fullNum,updateTime,createTime from vending_machines_way_item where 1=1 ";
	private Long id;
	private Long machineWayId;
	private String vmCode;
	private Long wayNumber;
	private Long basicItemId;
	private Integer weight;//商品重量
	private Integer orderNumber;//排序号
	private Double price;
	private Double promotionPrice;
	private Long num;
	private Long fullNum;
	private Date updateTime;
	private Date createTime;
	@NullField
	private Long picId;
	@NotField
	private String aisleConfiguration;

	private Integer maxCapacity;

	@NotField
	private String address;
}

