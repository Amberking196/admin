package com.server.module.system.logsManager.errorLog;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.google.common.collect.Lists;
import com.server.common.persistence.Page;
import com.server.common.service.BaseService;
import com.server.module.system.adminUser.AdminUserServiceImp;
import com.server.module.system.baseManager.stateInfo.StateInfoService;
import com.server.module.system.companyManage.CompanyServiceImpl;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;

/**
 * author name: yjr create time: 2018-03-24 09:57:07
 */
@Service
public class ErrorLogServiceImpl implements ErrorLogService {


	public static Logger log = LogManager.getLogger(AdminUserServiceImp.class); 	
	@Autowired
	private ErrorLogDao errorLogDaoImpl;
	@Autowired
	private StateInfoService stateInfoServiceImpl;

	public ReturnDataUtil listPage(ErrorLogCondition condition) {
		ReturnDataUtil data = errorLogDaoImpl.listPage(condition);
		@SuppressWarnings("unchecked")
		List<ErrorLogBean> list = (List<ErrorLogBean>) data.getReturnObject();
		for (ErrorLogBean errorLogBean : list) {
			if (errorLogBean.getSolveState() != 0)
				errorLogBean.setStateLable(stateInfoServiceImpl.getNameByState(errorLogBean.getSolveState() * 1l));
			else
				errorLogBean.setStateLable("未处理");
		}
		return data;
	}

	public boolean update(ErrorLogBean entity) {
		return errorLogDaoImpl.update(entity);
	}

}