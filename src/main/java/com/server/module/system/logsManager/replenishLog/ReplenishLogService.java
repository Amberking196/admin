package com.server.module.system.logsManager.replenishLog;

import java.util.Date;

import com.server.common.persistence.Page;
import com.server.util.ReturnDataUtil;

/**
 * author name: yjr
 * create time: 2018-03-23 11:24:53
 */ 
public interface  ReplenishLogService{
	
	public ReturnDataUtil listPage(ReplenishLogCondition condition);

}

