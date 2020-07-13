package com.server.module.system.logsManager.replenishLog;

import java.util.Date;
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

import org.apache.commons.logging.LogFactory;


@Service
public class ReplenishLogServiceImpl implements ReplenishLogService {

	public static Logger log = LogManager.getLogger(AdminUserServiceImp.class);
	@Autowired
	private ReplenishLogDaoImpl replenishLogDaoImpl;
	@Override
	public ReturnDataUtil listPage(ReplenishLogCondition condition) {
		
		return replenishLogDaoImpl.listPage(condition);
	}
}
