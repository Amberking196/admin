package com.server.module.system.warehouseManage.warehouseReplenish;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.server.module.commonBean.PageAssist;

import lombok.Data;
/**
 * table name:  warehouse_output_bill
 * author name: hjc
 * create time: 2018-05-28 17:58:52
 */ 
@Data
public class WarehouseReplenishForm extends PageAssist{
	Integer companyId; // 所属公司ID
	Integer areaId; // 所属地区ID
	Integer lineId; // 线路ID
	//开始时间
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	Date startDate;
	//结束时间
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    Date endDate;
	
	Integer type;//类型
	Integer output;//出入库类型 0 入库 1 出库

}

