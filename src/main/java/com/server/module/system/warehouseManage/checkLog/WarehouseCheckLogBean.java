package com.server.module.system.warehouseManage.checkLog;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.server.common.persistence.Entity;
import com.server.common.persistence.NotField;

import com.server.common.utils.excel.annotation.ExcelField;
import lombok.Data;
/**
 * table name:  warehouse_check_log
 * author name: yjr
 * create time: 2018-06-02 15:10:23
 */ 
@Data
@Entity(tableName="warehouse_check_log",id="id",idGenerate="auto")
public class WarehouseCheckLogBean{


//@JsonIgnore	public String tableName="warehouse_check_log";
//@JsonIgnore	public String selectSql="select * from warehouse_check_log where 1=1 ";
//@JsonIgnore	public String selectSql1="select id,basicItemId,itemName,machinesNum,replenishNum,sellNum,outWarehouseNum,lineId,areaId,companyId,userId,startTime,endTime,createTime from warehouse_check_log where 1=1 ";
   // @ExcelField(title = "公司")
	@NotField
    private String companyName;
	//@ExcelField(title = "区域")
	@NotField
	private String areaName;
	//@ExcelField(title = "线路")
	@NotField
	private String lineName;





	private Long id;
	private Long basicItemId;
	@ExcelField(title = "商品名")
	private String itemName;
	@ExcelField(title = "差额")
	private Long balance;//差额 preMachinesNum+outWarehouseNum-sellNum-machinesNum
	@ExcelField(title = "上次机器内数量")
	private Long preMachinesNum;
	@ExcelField(title = "机器内数量")
	private Integer machinesNum;
	@ExcelField(title = "领取数量")
	private Long outWarehouseNum;
	@ExcelField(title = "销售数量")
	private Long sellNum;
	@ExcelField(title = "补货数量")
	private Long replenishNum;

	private Long lineId;
	private Long areaId;
	private Long companyId;
	private Long userId;
	private Integer time;//次数盘点  1 开始
	//@ExcelField(title = "开始时间")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date startTime;
	//@ExcelField(title = "结束时间")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date endTime;
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date createTime;



}

