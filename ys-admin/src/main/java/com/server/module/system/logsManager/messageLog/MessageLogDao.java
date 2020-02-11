package com.server.module.system.logsManager.messageLog;

import java.util.List;

import com.server.common.persistence.Page;
import com.server.util.ReturnDataUtil;

/**
 * author name: yjr
 * create time: 2018-03-24 14:45:24
 */ 
public interface  MessageLogDao{

public Page listPage(Page page, String sql,List<Object> list);
public List listEntity(String sql,List<Object> list);
public MessageLogBean getEntity(Object id);
public boolean updateEntity(MessageLogBean entity);

public ReturnDataUtil listPage(MessageLogCondition condition);

}

