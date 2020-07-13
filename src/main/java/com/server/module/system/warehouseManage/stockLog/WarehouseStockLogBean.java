package com.server.module.system.warehouseManage.stockLog;

import com.server.common.persistence.Entity;
import com.server.common.persistence.NotField;

import lombok.Data;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
/**
 * table name:  warehouse_stock_log
 * author name: yjr
 * create time: 2018-05-22 11:00:53
 */ 
@Data
@Entity(tableName="warehouse_stock_log",id="id",idGenerate="auto")
public class WarehouseStockLogBean{


//@JsonIgnore	public String tableName="warehouse_stock_log";
//@JsonIgnore	public String selectSql="select * from warehouse_stock_log where 1=1 ";
//@JsonIgnore	public String selectSql1="select id,stockId,billItemId,billId,itemId,itemName,warehouseName,quantity,preQuantity,num,type,createTime from warehouse_stock_log where 1=1 ";
	private Long id;
	private Long stockId;//库存id
	private Long warehouseId;//仓库id
	private Long billItemId;//单据项id
	private Long billId;//单据id
	private Long itemId;//商品id
	private String itemName;//商品名称
	private String warehouseName;//仓库名
	private Long quantity;//变更后数量
	private Long preQuantity;//之前的数量
	private Long num;//数量
	private Integer type;//类型
	private Integer output;//进出库类型
	private Date createTime;
	@NotField
	private String typeLabel;

}

