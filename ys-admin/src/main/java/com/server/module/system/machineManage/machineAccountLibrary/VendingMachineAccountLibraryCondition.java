package com.server.module.system.machineManage.machineAccountLibrary;

import com.server.module.commonBean.PageAssist;

import lombok.Data;

/**
 * 
 * author name: why 
 * create time: 2018-04-02 10:44:47
 */
@Data
public class VendingMachineAccountLibraryCondition extends PageAssist {

	String content; //内容
	String barCode; //条形码
	Integer state;  //状态
	String subordinateCompanies; //所属公司
		
		 
}
