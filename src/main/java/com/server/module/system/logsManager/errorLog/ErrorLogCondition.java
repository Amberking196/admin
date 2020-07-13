package com.server.module.system.logsManager.errorLog;

import com.server.module.commonBean.PageAssist;

import lombok.Data;

/**
 * table name:  error_log
 * author name: yjr
 * create time: 2018-03-27 16:18:38
 */ 
@Data
public class ErrorLogCondition extends PageAssist{

	String vmCode;
	Long state;
	Integer companyId;
}

