package com.server.module.system.replenishManage.machineHistory;

import com.server.common.persistence.Entity;
import com.server.common.persistence.NotField;
import com.server.common.utils.excel.annotation.ExcelField;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * table name:  vending_machine_history
 * author name: yjr
 * create time: 2018-11-06 14:32:26
 */ 
@Data
@Entity(tableName="vending_machine_history",id="id",idGenerate="auto")
public class VendingMachineHistoryBean{


//@JsonIgnore	public String tableName="vending_machine_history";
//@JsonIgnore	public String selectSql="select * from vending_machine_history where 1=1 ";
//@JsonIgnore	public String selectSql1="select id,vmCode,wayNumber,itemId,num,endNum,saleNum,replenishNum,balanceNum,recordTime,createTime from vending_machine_history where 1=1 ";
	private Long id;
	@ExcelField(title = "机器编号",align=2)
	private String vmCode;//机器编号
	@NotField
	@ExcelField(title = "地址",align=2)
	private String address;//地址
	@NotField
	@ExcelField(title = "商品名",align=2)
	private String itemName;//商品名
	@ExcelField(title = "货道号",align=2)
	private Long wayNumber;//货道号
	private Long itemId;//商品id  页面不展示
	@ExcelField(title = "期初数量",align=2)
	private Long num;//期初数量
	@ExcelField(title = "期末数量",align=2)
	private Long endNum;//期末数量
	@ExcelField(title = "销售数量",align=2)
	private Long saleNum;//销售数量
	@ExcelField(title = "补货数量",align=2)
	private Long replenishNum;//补货数量
	@ExcelField(title = "差额",align=2)
	private Long balanceNum;//差额 为0即平衡
	private Date recordTime;//时间
	private Date createTime;
	@NotField
	@ExcelField(title = "日期",align=2)
	private String dayTime;


	public String getDayTime() {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sf.format(this.getRecordTime());
	}
}

