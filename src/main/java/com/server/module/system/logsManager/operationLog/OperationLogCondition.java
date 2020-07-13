package com.server.module.system.logsManager.operationLog;

import com.server.module.commonBean.PageAssist;

import lombok.Data;

/**
 * author name: why create time: 2018-05-07 13:49:31
 */
@Data
public class OperationLogCondition extends PageAssist {

	// 用户名
	String userName;
	// 售货机编号
	String vmCode;
	
}
