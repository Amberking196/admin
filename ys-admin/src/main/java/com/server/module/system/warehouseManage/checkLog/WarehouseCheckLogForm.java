package com.server.module.system.warehouseManage.checkLog;

import com.server.module.commonBean.PageAssist;

import lombok.Data;
/**
 * table name:  warehouse_check_log
 * author name: yjr
 * create time: 2018-06-02 15:10:23
 */ 
@Data
public class WarehouseCheckLogForm extends PageAssist{
	private String lineId;
	private String areaId;
	private String companyId;
	
	private String startTime;
	
	private String endTime;

}

