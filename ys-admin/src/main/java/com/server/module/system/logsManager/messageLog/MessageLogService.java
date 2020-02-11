package com.server.module.system.logsManager.messageLog;

import com.server.common.persistence.Page;
import com.server.util.ReturnDataUtil;

/**
 * author name: yjr
 * create time: 2018-03-24 14:45:24
 */ 
public interface  MessageLogService{
	public ReturnDataUtil listPage(MessageLogCondition condition);

}

