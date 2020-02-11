package com.server.module.system.warehouseManage.warehouseList;

import com.server.module.commonBean.PageAssist;

import lombok.Data;
/**
 * table name:  warehouse_info
 * author name: why
 * create time: 2018-05-14 22:06:48
 */ 
@Data
public class WarehouseInfoForm extends PageAssist{

	Integer principal;

	// 公司(机构ID)
	Integer companyId;
	
	private Long userId;//用户id

}

