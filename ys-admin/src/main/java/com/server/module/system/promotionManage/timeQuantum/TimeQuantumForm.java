package com.server.module.system.promotionManage.timeQuantum;

import com.server.module.commonBean.PageAssist;

import lombok.Data;
/**
 * table name:  time_quantum
 * author name: why
 * create time: 2018-08-22 10:56:46
 */ 
@Data
public class TimeQuantumForm extends PageAssist{

	//公司id
	Integer companyId;
	//售货机编号
	String vmCode;
}

