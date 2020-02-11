package com.server.module.system.logsManager.itemLog;

import java.util.Date;

import com.server.common.persistence.Page;
import com.server.util.ReturnDataUtil;

/**
 * author name: yjr
 * create time: 2018-03-24 09:24:43
 */ 
public interface  ItemLogService{
	public ReturnDataUtil listPage(ItemLogCondition condition);

}

