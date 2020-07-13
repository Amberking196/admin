package com.server.module.system.logsManager.messageLog;

import com.server.module.commonBean.PageAssist;

import lombok.Data;

/**
 * table name:  message_log
 * author name: yjr
 * create time: 2018-03-27 17:26:01
 */ 
@Data
public class MessageLogCondition extends PageAssist{

	Integer companyId;
	String vmCode;
	String payCode;
	String content;
}

