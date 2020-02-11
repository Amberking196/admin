package com.server.module.system.logsManager.messageLog;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.server.common.persistence.Page;
import com.server.module.system.adminUser.AdminUserServiceImp;
import com.server.module.system.companyManage.CompanyServiceImpl;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;


/**
 * author name: yjr
 *
 * create time: 2018-03-24 14:45:24
 */
@Service
public class MessageLogServiceImpl implements MessageLogService {


	public static Logger log = LogManager.getLogger(AdminUserServiceImp.class); 
	@Autowired
	private MessageLogDao messageLogDaoImpl;

	public ReturnDataUtil listPage(MessageLogCondition condition) {
		return messageLogDaoImpl.listPage(condition);
	}
}
