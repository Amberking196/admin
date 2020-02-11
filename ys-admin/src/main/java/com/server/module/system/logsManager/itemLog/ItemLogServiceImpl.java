package com.server.module.system.logsManager.itemLog;

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


/**
 * author name: yjr create time: 2018-03-24 09:24:43
 */
@Service
public class ItemLogServiceImpl implements ItemLogService {


	public static Logger log = LogManager.getLogger(AdminUserServiceImp.class);   
	@Autowired
	private ItemLogDao itemLogDaoImpl;

    //分页查询
	public ReturnDataUtil listPage(ItemLogCondition condition) {
		return itemLogDaoImpl.listPage(condition);
	}

}
