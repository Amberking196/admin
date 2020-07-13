package com.server.module.system.logsManager.operationLog;

import com.server.module.commonBean.PageAssist;

import lombok.Data;

@Data
public class PriceLogCondition extends PageAssist{

	// 用户名
	String userName;
	// 售货机编号
	String vmCode;
	
	String companyIds;
}
