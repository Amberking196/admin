package com.server.module.system.warehouseManage.supplierManage;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.server.module.commonBean.PageAssist;

import lombok.Data;
/**
 * table name:  supplier
 * author name: hjc
 * create time: 2018-05-21 16:58:08
 */ 
@Data
public class SupplierForm extends PageAssist{
	//联系人姓名
    String name;
    //公司名
    String companyName;
    //删除标志
    Integer delFlag; 
	//开始时间
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	Date startDate;
	// 结束时间
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	Date endDate;
	
}

