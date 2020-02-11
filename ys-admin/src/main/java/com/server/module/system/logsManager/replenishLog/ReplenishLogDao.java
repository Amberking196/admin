package com.server.module.system.logsManager.replenishLog;

import java.util.List;

import com.server.common.persistence.Page;
import com.server.util.ReturnDataUtil;

/**
 * author name: yjr
 * create time: 2018-03-23 11:24:53
 */ 
public interface  ReplenishLogDao{
	public ReturnDataUtil listPage(ReplenishLogCondition condition);
	public List<ReplenishLogBean> listEntity(ReplenishLogCondition condition);
	public ReplenishLogBean getEntity(Object id);
	public boolean delEntity(Object id);
	public boolean updateEntity(ReplenishLogBean entity);


}

