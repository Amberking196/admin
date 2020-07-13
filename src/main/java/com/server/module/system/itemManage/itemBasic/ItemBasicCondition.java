package com.server.module.system.itemManage.itemBasic;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.server.module.commonBean.PageAssist;

import lombok.Data;
/**
 * table name:  item_basic
 * author name: why
 * create time: 2018-04-10 14:22:54
 */ 
@Data
public class ItemBasicCondition extends PageAssist{
	
	Integer  id;
	String name;
	String barCode;
	Integer typeId;
	Long state;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	Date startDate;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	Date endDate;
	
	//标识  0 入库 1 出库
	Integer outPut;
	//仓库iD
	Integer warehouseId;

	private int isBind;//0 未绑定  1 已绑定 
	private int companyId;//公司id

}

