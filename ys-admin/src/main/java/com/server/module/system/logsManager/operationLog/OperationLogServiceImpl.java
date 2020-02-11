package com.server.module.system.logsManager.operationLog;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import com.server.util.ReturnDataUtil;

/**
 * author name: why create time: 2018-05-07 13:49:31
 */
@Service
public class OperationLogServiceImpl implements OperationLogService {
	
	public static Logger log = LogManager.getLogger(OperationLogServiceImpl.class);
	@Autowired
	private OperationLogDao operationLogDaoImpl;

	/**
	 * 查询 操作日志
	 */
	public ReturnDataUtil listPage(OperationLogCondition condition) {
		return operationLogDaoImpl.listPage(condition);
	}

	/**
	 * 增加 运营管理操作日志
	 */
	public OperationsManagementLogBean add(OperationsManagementLogBean entity) {
		return operationLogDaoImpl.insert(entity);
	}

	/**
	 * 价格修改日志 查询
	 * @param condition
	 * @return
	 */
	public ReturnDataUtil priceLogListPage(PriceLogCondition condition) {
		return operationLogDaoImpl.priceLogListPage(condition);

	}
}
