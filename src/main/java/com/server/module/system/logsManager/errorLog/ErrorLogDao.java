package com.server.module.system.logsManager.errorLog;

import java.util.List;

import com.server.common.persistence.Page;
import com.server.util.ReturnDataUtil;

/**
 * author name: yjr
 * create time: 2018-03-24 09:57:07
 */ 
public interface  ErrorLogDao{
	public ReturnDataUtil listPage(ErrorLogCondition condition);
	public boolean update(ErrorLogBean entity);

}

